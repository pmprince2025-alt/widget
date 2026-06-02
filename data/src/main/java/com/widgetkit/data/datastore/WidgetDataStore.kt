package com.widgetkit.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.widgetkit.data.mapper.WidgetMapper.toDomainWidget
import com.widgetkit.data.mapper.WidgetMapper.toProtoWidget
import com.widgetkit.data.proto.WidgetConfigProto
import com.widgetkit.domain.model.WidgetConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

private object WidgetConfigSerializer : androidx.datastore.core.Serializer<WidgetConfigProto> {
    override val defaultValue: WidgetConfigProto = WidgetConfigProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): WidgetConfigProto {
        try {
            return WidgetConfigProto.parseFrom(input)
        } catch (e: Exception) {
            return WidgetConfigProto.getDefaultInstance()
        }
    }

    override suspend fun writeTo(t: WidgetConfigProto, output: OutputStream) {
        t.writeTo(output)
    }
}

@Singleton
class WidgetDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore: DataStore<WidgetConfigProto> = DataStoreFactory.create(
        serializer = WidgetConfigSerializer,
        produceFile = { context.filesDir?.let { File(it, "datastore/widget_config.pb") } ?: File(context.cacheDir, "datastore/widget_config.pb") }
    )

    val widgets: Flow<List<WidgetConfig>> = dataStore.data.map { proto ->
        if (proto.widgetId.isNotEmpty()) {
            listOf(proto.toDomainWidget())
        } else {
            emptyList()
        }
    }

    suspend fun getWidget(id: String): WidgetConfig? {
        return dataStore.data.map { proto ->
            if (proto.widgetId == id) proto.toDomainWidget() else null
        }.first()
    }

    suspend fun saveWidget(config: WidgetConfig) {
        dataStore.updateData { config.toProtoWidget() }
    }

    suspend fun deleteWidget(id: String) {
        dataStore.updateData { WidgetConfigProto.getDefaultInstance() }
    }
}

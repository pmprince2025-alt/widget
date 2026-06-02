package com.widgetkit.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import com.widgetkit.data.proto.WidgetConfigProto
import com.widgetkit.domain.model.WidgetConfig
import com.widgetkit.data.mapper.toDomain
import com.widgetkit.data.mapper.toProto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore: DataStore<WidgetConfigProto> = context.createDataStore(
        fileName = "widget_config.pb"
    )

    val widgets: Flow<List<WidgetConfig>> = dataStore.data.map { proto ->
        if (proto.widgetId.isNotEmpty()) {
            listOf(proto.toDomain())
        } else {
            emptyList()
        }
    }

    suspend fun getWidget(id: String): WidgetConfig? {
        val proto = dataStore.data.map { it }.let { flow ->
            flow.value.takeIf { it.widgetId == id }
        }
        return proto?.toDomain()
    }

    suspend fun saveWidget(config: WidgetConfig) {
        dataStore.updateData { config.toProto() }
    }

    suspend fun deleteWidget(id: String) {
        dataStore.updateData { WidgetConfigProto.getDefaultInstance() }
    }
}

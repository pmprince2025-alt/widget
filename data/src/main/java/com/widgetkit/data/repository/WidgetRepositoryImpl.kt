package com.widgetkit.data.repository

import com.widgetkit.data.datastore.WidgetDataStore
import com.widgetkit.domain.model.WidgetConfig
import com.widgetkit.domain.repository.WidgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetRepositoryImpl @Inject constructor(
    private val dataStore: WidgetDataStore
) : WidgetRepository {

    override fun getAllWidgets(): Flow<List<WidgetConfig>> = dataStore.widgets

    override suspend fun getWidget(id: String): WidgetConfig? = dataStore.getWidget(id)

    override suspend fun saveWidget(config: WidgetConfig) = dataStore.saveWidget(config)

    override suspend fun deleteWidget(id: String) = dataStore.deleteWidget(id)
}

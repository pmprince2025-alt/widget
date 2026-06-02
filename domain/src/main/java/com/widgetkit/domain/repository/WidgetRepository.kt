package com.widgetkit.domain.repository

import com.widgetkit.domain.model.WidgetConfig
import kotlinx.coroutines.flow.Flow

interface WidgetRepository {
    fun getAllWidgets(): Flow<List<WidgetConfig>>
    suspend fun getWidget(id: String): WidgetConfig?
    suspend fun saveWidget(config: WidgetConfig)
    suspend fun deleteWidget(id: String)
}

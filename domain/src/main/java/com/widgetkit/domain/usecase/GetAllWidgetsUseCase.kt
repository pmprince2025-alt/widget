package com.widgetkit.domain.usecase

import com.widgetkit.domain.model.WidgetConfig
import com.widgetkit.domain.repository.WidgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllWidgetsUseCase @Inject constructor(
    private val repository: WidgetRepository
) {
    operator fun invoke(): Flow<List<WidgetConfig>> = repository.getAllWidgets()
}

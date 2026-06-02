package com.widgetkit.domain.usecase

import com.widgetkit.domain.model.WidgetConfig
import com.widgetkit.domain.repository.WidgetRepository
import javax.inject.Inject

class SaveWidgetUseCase @Inject constructor(
    private val repository: WidgetRepository
) {
    suspend operator fun invoke(config: WidgetConfig) {
        repository.saveWidget(config)
    }
}

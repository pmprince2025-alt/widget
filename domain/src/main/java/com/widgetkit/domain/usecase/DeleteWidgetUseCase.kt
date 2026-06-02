package com.widgetkit.domain.usecase

import com.widgetkit.domain.repository.WidgetRepository
import javax.inject.Inject

class DeleteWidgetUseCase @Inject constructor(
    private val repository: WidgetRepository
) {
    suspend operator fun invoke(widgetId: String) {
        repository.deleteWidget(widgetId)
    }
}

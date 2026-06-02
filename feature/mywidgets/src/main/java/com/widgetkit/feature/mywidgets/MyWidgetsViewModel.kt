package com.widgetkit.feature.mywidgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widgetkit.domain.model.WidgetConfig
import com.widgetkit.domain.usecase.DeleteWidgetUseCase
import com.widgetkit.domain.usecase.GetAllWidgetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWidgetsViewModel @Inject constructor(
    getAllWidgetsUseCase: GetAllWidgetsUseCase,
    private val deleteWidgetUseCase: DeleteWidgetUseCase
) : ViewModel() {

    val widgets: StateFlow<List<WidgetConfig>> = getAllWidgetsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteWidget(widgetId: String) {
        viewModelScope.launch {
            deleteWidgetUseCase(widgetId)
        }
    }
}

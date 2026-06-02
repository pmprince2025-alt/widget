package com.widgetkit.feature.config

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widgetkit.domain.model.ClockConfig
import com.widgetkit.domain.model.CountdownConfig
import com.widgetkit.domain.model.NoteConfig
import com.widgetkit.domain.model.NoteFontSize
import com.widgetkit.domain.model.WidgetConfig
import com.widgetkit.domain.model.WidgetType
import com.widgetkit.domain.usecase.SaveWidgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConfigUiState(
    val clockConfig: ClockConfig = ClockConfig(),
    val noteConfig: NoteConfig = NoteConfig(),
    val countdownConfig: CountdownConfig = CountdownConfig(),
    val isValid: Boolean = false,
    val validationError: String? = null
)

@HiltViewModel
class ConfigViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val saveWidgetUseCase: SaveWidgetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConfigUiState())
    val state: StateFlow<ConfigUiState> = _state.asStateFlow()

    private var widgetType: WidgetType = WidgetType.CLOCK

    fun initialize(type: String, existingId: String?) {
        widgetType = when (type) {
            "clock" -> WidgetType.CLOCK
            "note" -> WidgetType.NOTE
            "countdown" -> WidgetType.COUNTDOWN
            else -> WidgetType.CLOCK
        }
        validate()
    }

    fun updateClockConfig(config: ClockConfig) {
        _state.update { it.copy(clockConfig = config) }
        validate()
    }

    fun updateNoteConfig(config: NoteConfig) {
        _state.update { it.copy(noteConfig = config) }
        validate()
    }

    fun updateCountdownConfig(config: CountdownConfig) {
        _state.update { it.copy(countdownConfig = config) }
        validate()
    }

    private fun validate() {
        val current = _state.value
        when (widgetType) {
            WidgetType.CLOCK -> _state.update { it.copy(isValid = true, validationError = null) }
            WidgetType.NOTE -> {
                val content = current.noteConfig.content
                if (content.isBlank()) {
                    _state.update { it.copy(isValid = false, validationError = "Content is required") }
                } else if (content.length > 200) {
                    _state.update { it.copy(isValid = false, validationError = "Content must be 200 characters or fewer") }
                } else {
                    _state.update { it.copy(isValid = true, validationError = null) }
                }
            }
            WidgetType.COUNTDOWN -> {
                val target = current.countdownConfig.targetEpochMs
                if (target <= System.currentTimeMillis()) {
                    _state.update { it.copy(isValid = false, validationError = "Target must be in the future") }
                } else {
                    _state.update { it.copy(isValid = true, validationError = null) }
                }
            }
        }
    }

    fun saveAndPlace(existingId: String?) {
        val current = _state.value
        val config = WidgetConfig(
            widgetId = existingId ?: java.util.UUID.randomUUID().toString(),
            widgetType = widgetType,
            clockConfig = if (widgetType == WidgetType.CLOCK) current.clockConfig else null,
            noteConfig = if (widgetType == WidgetType.NOTE) current.noteConfig else null,
            countdownConfig = if (widgetType == WidgetType.COUNTDOWN) current.countdownConfig else null
        )
        viewModelScope.launch {
            saveWidgetUseCase(config)
            writeToWidgetPrefs(config)
        }
    }

    private fun writeToWidgetPrefs(config: WidgetConfig) {
        when (config.widgetType) {
            WidgetType.CLOCK -> {
                val prefs = context.getSharedPreferences("widget_clock", Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putBoolean("use12Hour", config.clockConfig?.use12Hour ?: false)
                    putBoolean("showSeconds", config.clockConfig?.showSeconds ?: true)
                    putBoolean("showDate", config.clockConfig?.showDate ?: true)
                    apply()
                }
            }
            WidgetType.NOTE -> {
                val prefs = context.getSharedPreferences("widget_note", Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putString("content", config.noteConfig?.content ?: "")
                    putInt("fontSize", when (config.noteConfig?.fontSize) {
                        NoteFontSize.SMALL -> 0
                        NoteFontSize.MEDIUM -> 1
                        NoteFontSize.LARGE -> 2
                        else -> 1
                    })
                    apply()
                }
            }
            WidgetType.COUNTDOWN -> {
                val prefs = context.getSharedPreferences("widget_countdown", Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putLong("targetEpochMs", config.countdownConfig?.targetEpochMs ?: 0L)
                    putString("label", config.countdownConfig?.label ?: "")
                    apply()
                }
            }
        }
    }
}

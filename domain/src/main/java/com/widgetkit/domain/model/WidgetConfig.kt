package com.widgetkit.domain.model

import java.util.UUID

data class WidgetConfig(
    val widgetId: String = UUID.randomUUID().toString(),
    val widgetType: WidgetType,
    val createdAt: Long = System.currentTimeMillis(),
    val clockConfig: ClockConfig? = null,
    val noteConfig: NoteConfig? = null,
    val countdownConfig: CountdownConfig? = null
)

enum class WidgetType { CLOCK, NOTE, COUNTDOWN }

data class ClockConfig(
    val use12Hour: Boolean = false,
    val showSeconds: Boolean = true,
    val showDate: Boolean = true
)

data class NoteConfig(
    val content: String = "",
    val fontSize: NoteFontSize = NoteFontSize.MEDIUM
)

enum class NoteFontSize { SMALL, MEDIUM, LARGE }

data class CountdownConfig(
    val targetEpochMs: Long = 0L,
    val label: String = ""
)

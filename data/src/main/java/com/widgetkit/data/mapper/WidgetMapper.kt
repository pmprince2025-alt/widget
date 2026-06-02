package com.widgetkit.data.mapper

import com.widgetkit.data.proto.ClockConfigProto
import com.widgetkit.data.proto.CountdownConfigProto
import com.widgetkit.data.proto.NoteConfigProto
import com.widgetkit.data.proto.NoteFontSizeProto
import com.widgetkit.data.proto.WidgetConfigProto
import com.widgetkit.data.proto.WidgetTypeProto
import com.widgetkit.domain.model.ClockConfig
import com.widgetkit.domain.model.CountdownConfig
import com.widgetkit.domain.model.NoteConfig
import com.widgetkit.domain.model.NoteFontSize
import com.widgetkit.domain.model.WidgetConfig
import com.widgetkit.domain.model.WidgetType

fun WidgetConfig.toProto(): WidgetConfigProto {
    return WidgetConfigProto.newBuilder()
        .setWidgetId(widgetId)
        .setWidgetType(widgetType.toProto())
        .setCreatedAt(createdAt)
        .apply {
            clockConfig?.let { setClockConfig(it.toProto()) }
            noteConfig?.let { setNoteConfig(it.toProto()) }
            countdownConfig?.let { setCountdownConfig(it.toProto()) }
        }
        .build()
}

fun WidgetConfigProto.toDomain(): WidgetConfig {
    return WidgetConfig(
        widgetId = widgetId,
        widgetType = widgetType.toDomain(),
        createdAt = createdAt,
        clockConfig = if (hasClockConfig()) clockConfig.toDomain() else null,
        noteConfig = if (hasNoteConfig()) noteConfig.toDomain() else null,
        countdownConfig = if (hasCountdownConfig()) countdownConfig.toDomain() else null
    )
}

fun WidgetType.toProto(): WidgetTypeProto = when (this) {
    WidgetType.CLOCK -> WidgetTypeProto.CLOCK
    WidgetType.NOTE -> WidgetTypeProto.NOTE
    WidgetType.COUNTDOWN -> WidgetTypeProto.COUNTDOWN
}

fun WidgetTypeProto.toDomain(): WidgetType = when (this) {
    WidgetTypeProto.CLOCK -> WidgetType.CLOCK
    WidgetTypeProto.NOTE -> WidgetType.NOTE
    WidgetTypeProto.COUNTDOWN -> WidgetType.COUNTDOWN
    WidgetTypeProto.UNRECOGNIZED -> WidgetType.CLOCK
}

fun ClockConfig.toProto(): ClockConfigProto = ClockConfigProto.newBuilder()
    .setUse12Hour(use12Hour)
    .setShowSeconds(showSeconds)
    .setShowDate(showDate)
    .build()

fun ClockConfigProto.toDomain(): ClockConfig = ClockConfig(
    use12Hour = use12Hour,
    showSeconds = showSeconds,
    showDate = showDate
)

fun NoteConfig.toProto(): NoteConfigProto = NoteConfigProto.newBuilder()
    .setContent(content)
    .setFontSize(fontSize.toProto())
    .build()

fun NoteConfigProto.toDomain(): NoteConfig = NoteConfig(
    content = content,
    fontSize = fontSize.toDomain()
)

fun NoteFontSize.toProto(): NoteFontSizeProto = when (this) {
    NoteFontSize.SMALL -> NoteFontSizeProto.SMALL
    NoteFontSize.MEDIUM -> NoteFontSizeProto.MEDIUM
    NoteFontSize.LARGE -> NoteFontSizeProto.LARGE
}

fun NoteFontSizeProto.toDomain(): NoteFontSize = when (this) {
    NoteFontSizeProto.SMALL -> NoteFontSize.SMALL
    NoteFontSizeProto.MEDIUM -> NoteFontSize.MEDIUM
    NoteFontSizeProto.LARGE -> NoteFontSize.LARGE
    NoteFontSizeProto.UNRECOGNIZED -> NoteFontSize.MEDIUM
}

fun CountdownConfig.toProto(): CountdownConfigProto = CountdownConfigProto.newBuilder()
    .setTargetEpochMs(targetEpochMs)
    .setLabel(label)
    .build()

fun CountdownConfigProto.toDomain(): CountdownConfig = CountdownConfig(
    targetEpochMs = targetEpochMs,
    label = label
)

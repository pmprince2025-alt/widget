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

object WidgetMapper {

    fun WidgetConfig.toProtoWidget(): WidgetConfigProto {
        return WidgetConfigProto.newBuilder()
            .setWidgetId(widgetId)
            .setWidgetType(widgetType.toProtoType())
            .setCreatedAt(createdAt)
            .apply {
                clockConfig?.let { setClockConfig(it.toProtoClock()) }
                noteConfig?.let { setNoteConfig(it.toProtoNote()) }
                countdownConfig?.let { setCountdownConfig(it.toProtoCountdown()) }
            }
            .build()
    }

    fun WidgetConfigProto.toDomainWidget(): WidgetConfig {
        return WidgetConfig(
            widgetId = widgetId,
            widgetType = widgetType.toDomainType(),
            createdAt = createdAt,
            clockConfig = if (hasClockConfig()) clockConfig.toDomainClock() else null,
            noteConfig = if (hasNoteConfig()) noteConfig.toDomainNote() else null,
            countdownConfig = if (hasCountdownConfig()) countdownConfig.toDomainCountdown() else null
        )
    }

    private fun WidgetType.toProtoType(): WidgetTypeProto = when (this) {
        WidgetType.CLOCK -> WidgetTypeProto.CLOCK
        WidgetType.NOTE -> WidgetTypeProto.NOTE
        WidgetType.COUNTDOWN -> WidgetTypeProto.COUNTDOWN
    }

    private fun WidgetTypeProto.toDomainType(): WidgetType = when (this) {
        WidgetTypeProto.CLOCK -> WidgetType.CLOCK
        WidgetTypeProto.NOTE -> WidgetType.NOTE
        WidgetTypeProto.COUNTDOWN -> WidgetType.COUNTDOWN
        WidgetTypeProto.UNRECOGNIZED -> WidgetType.CLOCK
    }

    private fun ClockConfig.toProtoClock(): ClockConfigProto = ClockConfigProto.newBuilder()
        .setUse12Hour(use12Hour)
        .setShowSeconds(showSeconds)
        .setShowDate(showDate)
        .build()

    private fun ClockConfigProto.toDomainClock(): ClockConfig = ClockConfig(
        use12Hour = use12Hour,
        showSeconds = showSeconds,
        showDate = showDate
    )

    private fun NoteConfig.toProtoNote(): NoteConfigProto = NoteConfigProto.newBuilder()
        .setContent(content)
        .setFontSize(fontSize.toProtoFontSize())
        .build()

    private fun NoteConfigProto.toDomainNote(): NoteConfig = NoteConfig(
        content = content,
        fontSize = fontSize.toDomainFontSize()
    )

    private fun NoteFontSize.toProtoFontSize(): NoteFontSizeProto = when (this) {
        NoteFontSize.SMALL -> NoteFontSizeProto.SMALL
        NoteFontSize.MEDIUM -> NoteFontSizeProto.MEDIUM
        NoteFontSize.LARGE -> NoteFontSizeProto.LARGE
    }

    private fun NoteFontSizeProto.toDomainFontSize(): NoteFontSize = when (this) {
        NoteFontSizeProto.SMALL -> NoteFontSize.SMALL
        NoteFontSizeProto.MEDIUM -> NoteFontSize.MEDIUM
        NoteFontSizeProto.LARGE -> NoteFontSize.LARGE
        NoteFontSizeProto.UNRECOGNIZED -> NoteFontSize.MEDIUM
    }

    private fun CountdownConfig.toProtoCountdown(): CountdownConfigProto = CountdownConfigProto.newBuilder()
        .setTargetEpochMs(targetEpochMs)
        .setLabel(label)
        .build()

    private fun CountdownConfigProto.toDomainCountdown(): CountdownConfig = CountdownConfig(
        targetEpochMs = targetEpochMs,
        label = label
    )
}

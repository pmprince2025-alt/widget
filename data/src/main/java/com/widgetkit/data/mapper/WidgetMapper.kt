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
        val cfg = this
        return WidgetConfigProto.newBuilder()
            .setWidgetId(cfg.widgetId)
            .setWidgetType(cfg.widgetType.toProtoType())
            .setCreatedAt(cfg.createdAt)
            .apply {
                cfg.clockConfig?.let { setClockConfig(clockToProto(it)) }
                cfg.noteConfig?.let { setNoteConfig(noteToProto(it)) }
                cfg.countdownConfig?.let { setCountdownConfig(countdownToProto(it)) }
            }
            .build()
    }

    fun WidgetConfigProto.toDomainWidget(): WidgetConfig {
        return WidgetConfig(
            widgetId = widgetId,
            widgetType = widgetType.toDomainType(),
            createdAt = createdAt,
            clockConfig = if (hasClockConfig()) clockToDomain(clockConfig) else null,
            noteConfig = if (hasNoteConfig()) noteToDomain(noteConfig) else null,
            countdownConfig = if (hasCountdownConfig()) countdownToDomain(countdownConfig) else null
        )
    }
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

private fun clockToProto(config: ClockConfig): ClockConfigProto = ClockConfigProto.newBuilder()
    .setUse12Hour(config.use12Hour)
    .setShowSeconds(config.showSeconds)
    .setShowDate(config.showDate)
    .build()

private fun clockToDomain(proto: ClockConfigProto): ClockConfig = ClockConfig(
    use12Hour = proto.use12Hour,
    showSeconds = proto.showSeconds,
    showDate = proto.showDate
)

private fun noteToProto(config: NoteConfig): NoteConfigProto = NoteConfigProto.newBuilder()
    .setContent(config.content)
    .setFontSize(config.fontSize.toProtoFontSize())
    .build()

private fun noteToDomain(proto: NoteConfigProto): NoteConfig = NoteConfig(
    content = proto.content,
    fontSize = proto.fontSize.toDomainFontSize()
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

private fun countdownToProto(config: CountdownConfig): CountdownConfigProto = CountdownConfigProto.newBuilder()
    .setTargetEpochMs(config.targetEpochMs)
    .setLabel(config.label)
    .build()

private fun countdownToDomain(proto: CountdownConfigProto): CountdownConfig = CountdownConfig(
    targetEpochMs = proto.targetEpochMs,
    label = proto.label
)

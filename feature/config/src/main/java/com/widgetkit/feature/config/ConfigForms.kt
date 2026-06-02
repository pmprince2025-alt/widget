package com.widgetkit.feature.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.widgetkit.core.ui.components.DateTimePicker
import com.widgetkit.core.ui.components.SectionHeader
import com.widgetkit.domain.model.ClockConfig
import com.widgetkit.domain.model.CountdownConfig
import com.widgetkit.domain.model.NoteConfig
import com.widgetkit.domain.model.NoteFontSize

@Composable
fun ClockConfigForm(
    config: ClockConfig,
    onConfigChange: (ClockConfig) -> Unit
) {
    SectionHeader(text = "Clock Settings")
    Spacer(Modifier.height(12.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = config.use12Hour,
            onCheckedChange = { onConfigChange(config.copy(use12Hour = it)) }
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = if (config.use12Hour) "12-hour format" else "24-hour format",
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = config.showSeconds,
            onCheckedChange = { onConfigChange(config.copy(showSeconds = it)) }
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Show seconds",
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = config.showDate,
            onCheckedChange = { onConfigChange(config.copy(showDate = it)) }
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Show date",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NoteConfigForm(
    config: NoteConfig,
    error: String?,
    onConfigChange: (NoteConfig) -> Unit
) {
    SectionHeader(text = "Note Settings")
    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
        value = config.content,
        onValueChange = { onConfigChange(config.copy(content = it)) },
        label = { Text("Note content") },
        placeholder = { Text("Type your note here\u2026") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 5,
        supportingText = {
            Text("${config.content.length} / 200")
        },
        isError = error != null
    )

    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

    Spacer(Modifier.height(16.dp))

    Text("Font size", style = MaterialTheme.typography.bodyLarge)
    Spacer(Modifier.height(8.dp))
    Row {
        NoteFontSize.entries.forEach { size ->
            FilterChip(
                selected = config.fontSize == size,
                onClick = { onConfigChange(config.copy(fontSize = size)) },
                label = {
                    Text(
                        when (size) {
                            NoteFontSize.SMALL -> "Small"
                            NoteFontSize.MEDIUM -> "Medium"
                            NoteFontSize.LARGE -> "Large"
                        }
                    )
                },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountdownConfigForm(
    config: CountdownConfig,
    error: String?,
    onConfigChange: (CountdownConfig) -> Unit
) {
    SectionHeader(text = "Countdown Settings")
    Spacer(Modifier.height(12.dp))

    DateTimePicker(
        label = "Target",
        selectedDate = config.targetEpochMs.takeIf { it > 0 }
            ?: (System.currentTimeMillis() + 86400000L),
        onDateSelected = { onConfigChange(config.copy(targetEpochMs = it)) }
    )

    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
        value = config.label,
        onValueChange = { onConfigChange(config.copy(label = it.take(40))) },
        label = { Text("Label") },
        placeholder = { Text("My event\u2026") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        supportingText = {
            Text("${config.label.length} / 40")
        }
    )
}

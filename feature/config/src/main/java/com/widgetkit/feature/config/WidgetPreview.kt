package com.widgetkit.feature.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.widgetkit.domain.model.NoteFontSize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun WidgetPreview(
    state: ConfigUiState,
    widgetType: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (widgetType) {
            "clock" -> ClockPreview(state.clockConfig)
            "note" -> NotePreview(state.noteConfig)
            "countdown" -> CountdownPreview(state.countdownConfig)
        }
    }
}

@Composable
private fun ClockPreview(config: com.widgetkit.domain.model.ClockConfig) {
    val now = remember { Calendar.getInstance() }
    val timeFormat = if (config.use12Hour) {
        SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    } else {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    }
    val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (config.showSeconds) timeFormat.format(now.time) else {
                val fmt = if (config.use12Hour) "hh:mm a" else "HH:mm"
                SimpleDateFormat(fmt, Locale.getDefault()).format(now.time)
            },
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        if (config.showDate) {
            Text(
                text = dateFormat.format(now.time),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NotePreview(config: com.widgetkit.domain.model.NoteConfig) {
    val fontSize = when (config.fontSize) {
        NoteFontSize.SMALL -> 14.sp
        NoteFontSize.MEDIUM -> 16.sp
        NoteFontSize.LARGE -> 20.sp
    }

    Text(
        text = config.content.ifBlank { "Your note will appear here" },
        fontSize = fontSize,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
private fun CountdownPreview(config: com.widgetkit.domain.model.CountdownConfig) {
    val remaining = config.targetEpochMs - System.currentTimeMillis()
    val displayText = if (remaining > 0) {
        val days = (remaining / 86400000).toInt()
        val hours = ((remaining % 86400000) / 3600000).toInt()
        val minutes = ((remaining % 3600000) / 60000).toInt()
        val seconds = ((remaining % 60000) / 1000).toInt()
        "${days}d ${hours}h ${minutes}m ${seconds}s"
    } else {
        "Expired"
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (config.label.isNotBlank()) {
            Text(
                text = config.label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = displayText,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

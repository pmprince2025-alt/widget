package com.widgetkit.widget.clock

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ClockGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val prefs = context.getSharedPreferences("widget_clock", Context.MODE_PRIVATE)
                val use12Hour = prefs.getBoolean("use12Hour", false)
                val showSeconds = prefs.getBoolean("showSeconds", true)
                val showDate = prefs.getBoolean("showDate", true)
                val now = Calendar.getInstance()

                val timeText = if (showSeconds) {
                    val fmt = if (use12Hour) "hh:mm:ss a" else "HH:mm:ss"
                    SimpleDateFormat(fmt, Locale.getDefault()).format(now.time)
                } else {
                    val fmt = if (use12Hour) "hh:mm a" else "HH:mm"
                    SimpleDateFormat(fmt, Locale.getDefault()).format(now.time)
                }

                val dateText = SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(now.time)

                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.surface)
                        .padding(8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = timeText,
                        style = TextStyle(fontSize = 28, fontWeight = FontWeight.Bold)
                    )
                    if (showDate) {
                        Text(
                            text = dateText,
                            style = TextStyle(
                                fontSize = 14,
                                color = GlanceTheme.colors.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}

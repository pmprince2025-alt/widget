package com.widgetkit.widget.countdown

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
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class CountdownGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val prefs = context.getSharedPreferences("widget_countdown", Context.MODE_PRIVATE)
                val targetEpochMs = prefs.getLong("targetEpochMs", 0L)
                val label = prefs.getString("label", "") ?: ""
                val remaining = targetEpochMs - System.currentTimeMillis()

                val displayText = if (remaining > 0) {
                    val days = (remaining / 86400000).toInt()
                    val hours = ((remaining % 86400000) / 3600000).toInt()
                    val minutes = ((remaining % 3600000) / 60000).toInt()
                    val seconds = ((remaining % 60000) / 1000).toInt()
                    "${days}d ${hours}h ${minutes}m ${seconds}s"
                } else {
                    "Expired"
                }

                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.secondaryContainer)
                        .padding(8f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (label.isNotBlank()) {
                        Text(
                            text = label,
                            style = TextStyle(
                                fontSize = 14f,
                                color = GlanceTheme.colors.onSecondaryContainer
                            )
                        )
                    }
                    Text(
                        text = displayText,
                        style = TextStyle(
                            fontSize = 22f,
                            fontWeight = FontWeight.Bold,
                            color = GlanceTheme.colors.onSecondaryContainer
                        )
                    )
                }
            }
        }
    }
}

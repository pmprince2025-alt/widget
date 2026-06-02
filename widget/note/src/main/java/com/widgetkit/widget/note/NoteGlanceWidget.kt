package com.widgetkit.widget.note

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
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class NoteGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val prefs = context.getSharedPreferences("widget_note", Context.MODE_PRIVATE)
                val content = prefs.getString("content", "Empty note") ?: "Empty note"
                val fontSize = prefs.getInt("fontSize", 1)

                val sp = when (fontSize) {
                    0 -> 14
                    1 -> 16
                    2 -> 20
                    else -> 16
                }

                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.primaryContainer)
                        .padding(12),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = content,
                        style = TextStyle(fontSize = sp),
                        maxLines = 5
                    )
                }
            }
        }
    }
}

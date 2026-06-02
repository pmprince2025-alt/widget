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
import androidx.glance.text.TextOverflow
import androidx.glance.unit.dp
import androidx.glance.unit.sp

class NoteGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val prefs = context.getSharedPreferences("widget_note", Context.MODE_PRIVATE)
                val content = prefs.getString("content", "Empty note") ?: "Empty note"
                val fontSize = prefs.getInt("fontSize", 1)

                val sp = when (fontSize) {
                    0 -> 14.sp
                    1 -> 16.sp
                    2 -> 20.sp
                    else -> 16.sp
                }

                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.primaryContainer)
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = content,
                        style = TextStyle(fontSize = sp),
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

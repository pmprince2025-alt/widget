package com.widgetkit.feature.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.widgetkit.core.ui.components.PrimaryButton
import com.widgetkit.core.ui.components.SectionHeader
import com.widgetkit.domain.model.WidgetType

data class WidgetTypeInfo(
    val type: WidgetType,
    val name: String,
    val description: String,
    val icon: ImageVector
)

val widgetTypes = listOf(
    WidgetTypeInfo(
        type = WidgetType.CLOCK,
        name = "Digital Clock",
        description = "A clean digital clock with format options",
        icon = Icons.Outlined.AccessTime
    ),
    WidgetTypeInfo(
        type = WidgetType.NOTE,
        name = "Text Note",
        description = "A sticky note for quick reminders",
        icon = Icons.Outlined.EditNote
    ),
    WidgetTypeInfo(
        type = WidgetType.COUNTDOWN,
        name = "Countdown Timer",
        description = "Count down to any date and time",
        icon = Icons.Outlined.Timer
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    onConfigure: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("WidgetKit") }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                SectionHeader(text = "Available Widgets")
                Spacer(Modifier.height(8.dp))
            }
            items(widgetTypes) { widget ->
                WidgetTypeCard(
                    info = widget,
                    onConfigure = { onConfigure(widget.type.name.lowercase()) }
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun WidgetTypeCard(
    info: WidgetTypeInfo,
    onConfigure: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = info.icon,
                    contentDescription = info.name,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = info.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = info.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            PrimaryButton(
                text = "Configure",
                onClick = onConfigure
            )
        }
    }
}

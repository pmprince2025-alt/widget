package com.widgetkit.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1B6CA8),
    onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFD0E4FF),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF001D36),
    secondary = androidx.compose.ui.graphics.Color(0xFF535F70),
    onSecondary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFD7E3F7),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF101C2B),
    surface = androidx.compose.ui.graphics.Color(0xFFF7F9FC),
    onSurface = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFE8EFF7),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF44464F),
    error = androidx.compose.ui.graphics.Color(0xFFB3261E),
    onError = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    outline = androidx.compose.ui.graphics.Color(0xFF79747E)
)

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF9ECAFF),
    onPrimary = androidx.compose.ui.graphics.Color(0xFF003258),
    surface = androidx.compose.ui.graphics.Color(0xFF1A1C1E),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE3E2E6),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF2C3036),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFC4C6CF)
)

@Composable
fun WidgetKitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

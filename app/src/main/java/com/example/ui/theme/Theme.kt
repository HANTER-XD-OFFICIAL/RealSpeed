package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF04202C),
    primaryContainer = Color(0xFF003830),
    onPrimaryContainer = NeonCyan,
    secondary = ElectricBlue,
    onSecondary = Color(0xFF001F3F),
    secondaryContainer = Color(0xFF003554),
    onSecondaryContainer = ElectricBlue,
    tertiary = GlowPink,
    onTertiary = Color.White,
    background = CyberBackground,
    onBackground = TextPrimary,
    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CyberCardBorder,
    error = DangerRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to stunning dark theme for speedtest
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

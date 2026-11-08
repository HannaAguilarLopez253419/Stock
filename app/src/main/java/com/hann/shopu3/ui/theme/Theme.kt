package com.hann.shopu3.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Violet600,
    onPrimary = Color.White,
    primaryContainer = Violet100,
    onPrimaryContainer = Violet700,
    secondary = Violet400,
    onSecondary = Color.White,
    secondaryContainer = Violet100,
    onSecondaryContainer = Violet700,
    tertiary = Violet700,
    onTertiary = Color.White,
    background = Canvas,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Violet100,
    onSurfaceVariant = InkSoft,
    outline = Violet200,
    outlineVariant = Violet100,
    error = DangerRed,
    onError = Color.White,
    errorContainer = DangerSoft,
    onErrorContainer = DangerRed,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Violet50,
    surfaceContainer = Violet50,
    surfaceContainerHigh = Violet100,
    surfaceContainerHighest = Violet100
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun ShopU3Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
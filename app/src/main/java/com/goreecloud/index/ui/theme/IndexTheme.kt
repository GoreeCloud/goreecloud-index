package com.goreecloud.index.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val IndexShapes = Shapes(
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
)

private val GlazeLightScheme = lightColorScheme(
    primary = Color(GlazeV11Contract.DEEP_TEAL),
    onPrimary = Color.White,
    primaryContainer = Color(GlazeV11Contract.SOFT_AQUA),
    onPrimaryContainer = Color(GlazeV11Contract.DEEP_GRAPHITE),
    secondary = Color(GlazeV11Contract.SOFT_AMBER),
    onSecondary = Color(GlazeV11Contract.DEEP_GRAPHITE),
    secondaryContainer = Color(GlazeV11Contract.CHAMPAGNE_GOLD),
    onSecondaryContainer = Color(GlazeV11Contract.DEEP_GRAPHITE),
    tertiary = Color(GlazeV11Contract.MINERAL_TEAL),
    onTertiary = Color.White,
    background = Color(0xFFF6FAFB),
    onBackground = Color(GlazeV11Contract.DEEP_GRAPHITE),
    surface = Color(0xFFFBFDFD),
    onSurface = Color(GlazeV11Contract.DEEP_GRAPHITE),
    surfaceVariant = Color(0xFFE7EFF0),
    onSurfaceVariant = Color(GlazeV11Contract.SLATE_GRAPHITE),
    outline = Color(0xFF65767D),
)

private val GlazeDarkScheme = darkColorScheme(
    primary = Color(GlazeV11Contract.SOFT_AQUA),
    onPrimary = Color(GlazeV11Contract.CANVAS_BLACK),
    primaryContainer = Color(GlazeV11Contract.DEEP_TEAL),
    onPrimaryContainer = Color(0xFFE4FFFF),
    secondary = Color(GlazeV11Contract.CHAMPAGNE_GOLD),
    onSecondary = Color(GlazeV11Contract.CANVAS_BLACK),
    secondaryContainer = Color(0xFF60461F),
    onSecondaryContainer = Color(0xFFFFE9C6),
    tertiary = Color(GlazeV11Contract.MINERAL_TEAL),
    onTertiary = Color(GlazeV11Contract.CANVAS_BLACK),
    background = Color(GlazeV11Contract.DEEP_GRAPHITE),
    onBackground = Color(0xFFE7EEF0),
    surface = Color(GlazeV11Contract.SLATE_GRAPHITE),
    onSurface = Color(0xFFE7EEF0),
    surfaceVariant = Color(0xFF233239),
    onSurfaceVariant = Color(0xFFC6D1D5),
    outline = Color(0xFF8B9BA2),
)

private val GlazeDeepDarkScheme = darkColorScheme(
    primary = Color(GlazeV11Contract.SOFT_AQUA),
    onPrimary = Color(GlazeV11Contract.CANVAS_BLACK),
    primaryContainer = Color(0xFF0B4F52),
    onPrimaryContainer = Color(0xFFE4FFFF),
    secondary = Color(GlazeV11Contract.CHAMPAGNE_GOLD),
    onSecondary = Color(GlazeV11Contract.CANVAS_BLACK),
    secondaryContainer = Color(0xFF4E3818),
    onSecondaryContainer = Color(0xFFFFE9C6),
    tertiary = Color(GlazeV11Contract.MINERAL_TEAL),
    onTertiary = Color(GlazeV11Contract.CANVAS_BLACK),
    background = Color(GlazeV11Contract.CANVAS_BLACK),
    onBackground = Color(0xFFE7EEF0),
    surface = Color(GlazeV11Contract.DEEP_GRAPHITE),
    onSurface = Color(0xFFE7EEF0),
    surfaceVariant = Color(GlazeV11Contract.SLATE_GRAPHITE),
    onSurfaceVariant = Color(0xFFC6D1D5),
    outline = Color(0xFF91A2A9),
)

@Composable
fun GoreeCloudIndexTheme(
    appearance: GlazeV11Contract.Appearance? = null,
    content: @Composable () -> Unit,
) {
    val resolvedAppearance = appearance ?: if (isSystemInDarkTheme()) {
        GlazeV11Contract.Appearance.Dark
    } else {
        GlazeV11Contract.Appearance.Light
    }

    val colorScheme = when (resolvedAppearance) {
        GlazeV11Contract.Appearance.Light -> GlazeLightScheme
        GlazeV11Contract.Appearance.Dark -> GlazeDarkScheme
        GlazeV11Contract.Appearance.DeepDark -> GlazeDeepDarkScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = IndexShapes,
        content = content,
    )
}

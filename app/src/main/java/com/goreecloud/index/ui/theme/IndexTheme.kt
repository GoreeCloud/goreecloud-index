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
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
)

private val GlazeLightColorScheme = lightColorScheme(
    primary = Color(GlazeV14Contract.DEEP_TEAL),
    onPrimary = Color.White,
    primaryContainer = Color(GlazeV14Contract.SOFT_AQUA),
    onPrimaryContainer = Color(GlazeV14Contract.DEEP_GRAPHITE),
    secondary = Color(GlazeV14Contract.MINERAL_TEAL),
    onSecondary = Color.White,
    background = Color(GlazeV14Contract.FROST_WHITE),
    onBackground = Color(GlazeV14Contract.COOL_GRAPHITE),
    surface = Color(GlazeV14Contract.CRYSTAL_WHITE),
    onSurface = Color(GlazeV14Contract.COOL_GRAPHITE),
    surfaceVariant = Color(GlazeV14Contract.ICE_BLUE),
    onSurfaceVariant = Color(GlazeV14Contract.SLATE_GRAY),
    outline = Color(GlazeV14Contract.CLOUD_GRAY),
)

private val GlazeDarkColorScheme = darkColorScheme(
    primary = Color(GlazeV14Contract.SOFT_AQUA),
    onPrimary = Color(GlazeV14Contract.BLUE_BLACK),
    primaryContainer = Color(GlazeV14Contract.DEEP_TEAL),
    onPrimaryContainer = Color(GlazeV14Contract.CRYSTAL_WHITE),
    secondary = Color(GlazeV14Contract.MINERAL_TEAL),
    onSecondary = Color(GlazeV14Contract.BLUE_BLACK),
    background = Color(GlazeV14Contract.BLUE_BLACK),
    onBackground = Color(GlazeV14Contract.CRYSTAL_WHITE),
    surface = Color(GlazeV14Contract.DEEP_GRAPHITE),
    onSurface = Color(GlazeV14Contract.CRYSTAL_WHITE),
    surfaceVariant = Color(GlazeV14Contract.COOL_GRAPHITE),
    onSurfaceVariant = Color(GlazeV14Contract.CLOUD_GRAY),
    outline = Color(GlazeV14Contract.SLATE_GRAY),
)

/**
 * GoreeCloud Index's deterministic native GLAZE UI V1.4 theme projection.
 *
 * The shared web Optical Engine is not executed inside Compose. Native optical effects remain
 * separately acceptance-gated; the shipped theme uses stable semantic color/shape mappings and
 * therefore stays on the solid, legible path until a reviewed native optical adapter exists.
 */
@Composable
fun GoreeCloudIndexTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) {
        GlazeDarkColorScheme
    } else {
        GlazeLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = IndexShapes,
        content = content,
    )
}

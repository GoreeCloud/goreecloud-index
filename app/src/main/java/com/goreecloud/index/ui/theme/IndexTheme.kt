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
    primary = Color(GlazeV15Contract.DEEP_TEAL),
    onPrimary = Color.White,
    primaryContainer = Color(GlazeV15Contract.SOFT_AQUA),
    onPrimaryContainer = Color(GlazeV15Contract.DEEP_GRAPHITE),
    secondary = Color(GlazeV15Contract.MINERAL_TEAL),
    onSecondary = Color.White,
    background = Color(GlazeV15Contract.FROST_WHITE),
    onBackground = Color(GlazeV15Contract.COOL_GRAPHITE),
    surface = Color(GlazeV15Contract.CRYSTAL_WHITE),
    onSurface = Color(GlazeV15Contract.COOL_GRAPHITE),
    surfaceVariant = Color(GlazeV15Contract.ICE_BLUE),
    onSurfaceVariant = Color(GlazeV15Contract.SLATE_GRAY),
    outline = Color(GlazeV15Contract.CLOUD_GRAY),
)

private val GlazeDarkColorScheme = darkColorScheme(
    primary = Color(GlazeV15Contract.SOFT_AQUA),
    onPrimary = Color(GlazeV15Contract.BLUE_BLACK),
    primaryContainer = Color(GlazeV15Contract.DEEP_TEAL),
    onPrimaryContainer = Color(GlazeV15Contract.CRYSTAL_WHITE),
    secondary = Color(GlazeV15Contract.MINERAL_TEAL),
    onSecondary = Color(GlazeV15Contract.BLUE_BLACK),
    background = Color(GlazeV15Contract.BLUE_BLACK),
    onBackground = Color(GlazeV15Contract.CRYSTAL_WHITE),
    surface = Color(GlazeV15Contract.DEEP_GRAPHITE),
    onSurface = Color(GlazeV15Contract.CRYSTAL_WHITE),
    surfaceVariant = Color(GlazeV15Contract.COOL_GRAPHITE),
    onSurfaceVariant = Color(GlazeV15Contract.CLOUD_GRAY),
    outline = Color(GlazeV15Contract.SLATE_GRAY),
)

/**
 * GoreeCloud Index's deterministic native GLAZE UI V1.5 theme projection.
 *
 * V1.5 inherits the reviewed V1.4.1 visual baseline. The shared web runtime is
 * not executed inside Compose; context/capability state must be supplied by the
 * owning Index/platform authority and is handled by `GlazeV15Contract` without
 * granting authorization or operational authority. Native optical effects remain
 * separately acceptance-gated, so the shipped theme stays on the solid, legible
 * path until reviewed consumer evidence exists.
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

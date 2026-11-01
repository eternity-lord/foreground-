package com.forgebound.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.dynamicColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkForgePalette = darkColorScheme(
    surface = ForgeColors.Surface,
    surfaceVariant = ForgeColors.SurfaceVariant,
    background = ForgeColors.Background,
    onSurface = ForgeColors.TextPrimary,
    onSurfaceVariant = ForgeColors.TextSecondary,
    primary = ForgeColors.EmberOrange,
    secondary = ForgeColors.MoltenGold,
    tertiary = ForgeColors.SteelBlue,
)

@Composable
fun ForgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (!darkTheme) {
        dynamicLightColorScheme()
    } else {
        dynamicColorScheme(dark = true)
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = ForgeTypography,
        content = content
    )
}

/** Convenience: apply ForgeTheme with the forge palette. */
@Composable
fun ForgeSurface(
    content: @Composable () -> Unit
) {
    ForgeTheme { content }
}

/** Forge brand colors for use across UI. */
val ForgeBrand = ForgeColors

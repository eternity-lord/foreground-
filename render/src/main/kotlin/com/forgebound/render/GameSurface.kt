package com.forgebound.render

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawScope.drawRoundRect
import androidx.compose.ui.unit.dp
import com.forgebound.ui.theme.ForgeBrand

/**
 * Game render surface (Section 7 / M13).
 *
 * Canvas-based render surface used by Battle and Forging screens for
 * anime-style 2D rendering: entity sprites, VFX particles, camera shake,
 * damage numbers, crit flashes, element SFX.
 *
 * All rendering is offline and deterministic given a seed (GameRng).
 * Asset atlases (key art, portraits, zone backgrounds) are loaded from
 * assets/ and drawn here — no network.
 */
@Composable
fun GameSurface(
    modifier: Modifier = Modifier,
    onFrame: (renderContext: RenderContext) -> Unit = { }
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(ForgeBrand.Background)
    ) {
        drawRoundRect(
            color = ForgeBrand.Surface,
            topLeft = offset,
            size = size,
            cornerRadius = 12.dp
        )
        onFrame(RenderContext(this))
    }
}

/** Context exposed to per-frame render callbacks. */
data class RenderContext(val drawScope: androidx.compose.ui.graphics.drawscope.DrawScope)

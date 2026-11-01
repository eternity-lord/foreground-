package com.forgebound.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * ForgeBound visual identity (Section 7 / STYLE_BIBLE.md).
 *
 * Warm forge palette (ember orange, molten gold, steel blue) against moody
 * fantasy zones. UI: brushed-metal, parchment and rune motifs.
 *
 * Rarity colors are color-blind safe: each rarity also has an icon and border
 * shape in the UI layer.
 */
object ForgeColors {
    // --- Core palette ---
    val EmberOrange = Color(0xFFD97B29)
    val MoltenGold = Color(0xFFC9A227)
    val SteelBlue = Color(0xFF3B5B7A)
    val Parchment = Color(0xFFEAD8C0)
    val DarkForge = Color(0xFF2A1E18)

    // --- Surface ---
    val Background = DarkForge
    val Surface = Color(0xFF3A2E26)
    val SurfaceVariant = Color(0xFF4A3E34)

    // --- Text ---
    val TextPrimary = Parchment
    val TextSecondary = Color(0xFFD4C4B4)
    val TextOnEmber = Color(0xFFFFFFFF)

    // --- Rarity colors (color-blind safe: color + icon + shape) ---
    val RarityCommon = Color(0xFF9E9E9E)        // circle
    val RarityUncommon = Color(0xFF4CAF50)      // diamond
    val RarityRare = Color(0xFF2196F3)          // hexagon
    val RarityEpic = Color(0xFF9C27B0)          // shield
    val RarityLegendary = Color(0xFFFF9800)     // crown
    val RarityMythic = Color(0xFFE91E63)        // star

    fun rarityColor(rarity: String): Color = when (rarity) {
        "Common" -> RarityCommon
        "Uncommon" -> RarityUncommon
        "Rare" -> RarityRare
        "Epic" -> RarityEpic
        "Legendary" -> RarityLegendary
        "Mythic" -> RarityMythic
        else -> RarityCommon
    }
}

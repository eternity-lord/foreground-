package com.forgebound.game.loot

import com.forgebound.core.GameRng

/**
 * Loot drops (Section 4 core loop). Enemies drop materials per their drop table;
 * bosses drop guaranteed tier materials.
 */
data class DropEntry(val itemId: String, val minQty: Int, val maxQty: Int, val chance: Double)

object LootTables {
    fun rollDrops(dropTable: List<DropEntry>, rng: GameRng): List<String> {
        val drops = mutableListOf<String>()
        for (entry in dropTable) {
            if (rng.chance(entry.chance)) {
                val qty = rng.nextIntInclusive(entry.minQty, entry.maxQty)
                repeat(qty) { drops.add(entry.itemId) }
            }
        }
        return drops
    }
}

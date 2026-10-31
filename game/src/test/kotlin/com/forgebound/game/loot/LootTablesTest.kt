package com.forgebound.game.loot

import com.forgebound.core.GameRng
import kotlin.test.Test
import kotlin.test.assertTrue

class LootTablesTest {

    @Test
    fun zeroChanceDropsNothing() {
        val rng = GameRng.withSeed(1)
        val drops = LootTables.rollDrops(listOf(DropEntry("mat_2001", 1, 1, 0.0)), rng)
        assertTrue { drops.isEmpty() }
    }

    @Test
    fun hundredPercentAlwaysDrops() {
        val rng = GameRng.withSeed(2)
        val drops = LootTables.rollDrops(listOf(DropEntry("mat_2001", 2, 2, 1.0)), rng)
        assertEquals(listOf("mat_2001", "mat_2001"), drops)
    }
}

package com.forgebound.core.stats

import com.forgebound.core.BalanceConstants
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlayerStatsTest {

    @Test
    fun xpCurveIsMonotonicAndPositive() {
        var prev = 0L
        for (l in 2..120) {
            val xp = BalanceConstants.xpForLevel(l)
            assertTrue { xp > 0 }
            assertTrue { xp > prev }
            prev = xp
        }
    }

    @Test
    fun xpForLevelMatchesFormula() {
        // round(80 * L^1.65): L=1 -> 80, L=3 -> round(490.1) = 490
        assertEquals(490L, BalanceConstants.xpForLevel(3))
        assertEquals(80L, BalanceConstants.xpForLevel(1))
    }

    @Test
    fun derivedStatsScaleWithLevelAndVit() {
        val base = BaseStats(STR = 20, DEX = 10, VIT = 20, INT = 10, LUCK = 10)
        val low = PlayerStats.derived(base, 1)
        val high = PlayerStats.derived(base, 120)
        assertTrue { high.HP > low.HP }
        assertTrue { high.ATK > low.ATK }
        assertTrue { high.FORGE_SKILL >= low.FORGE_SKILL }
    }

    @Test
    fun respecCostScalesWithLevelAndIsCapped() {
        assertTrue { PlayerStats.respecCost(120) >= PlayerStats.respecCost(1) }
        assertEquals(250_000L, PlayerStats.respecCost(120))
        assertEquals(250L, PlayerStats.respecCost(1))
    }
}

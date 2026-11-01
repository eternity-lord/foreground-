package com.forgebound.core.forging

import com.forgebound.core.GameRng
import com.forgebound.core.BalanceConstants.ForgeGrade
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ForgingTest {

    @Test
    fun qualityScoreIsAlwaysInRange() {
        val rng = GameRng.withSeed(5)
        for (_ in 1..500) {
            val r = Forging.score(PhaseInput(0.5,
                    listOf(TimingGrade.PERFECT, TimingGrade.PERFECT),
                    1.0, 1.0, 1.0), 20, 10, rng)
            assertTrue { r.qualityScore in 0..100 }
        }
    }

    @Test
    fun gradeMapping() {
        assertEquals(ForgeGrade.CRUDE, Forging.gradeFor(0))
        assertEquals(ForgeGrade.COMMON, Forging.gradeFor(30))
        assertEquals(ForgeGrade.LEGENDARY, Forging.gradeFor(95))
    }

    @Test
    fun autoForgeIsLowerThanSkilledPlay() {
        val rng = GameRng.withSeed(42)
        val skilled = Forging.score(PhaseInput(0.55,
                listOf(TimingGrade.PERFECT, TimingGrade.PERFECT, TimingGrade.PERFECT),
                1.0, 1.0, 1.0), 30, 20, rng)
        val auto = Forging.autoForgeQuality(20, GameRng.withSeed(42))
        assertTrue { skilled.qualityScore > auto }
    }
}

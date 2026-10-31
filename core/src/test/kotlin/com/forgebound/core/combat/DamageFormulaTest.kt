package com.forgebound.core.combat

import com.forgebound.core.GameRng
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DamageFormulaTest {

    @Test
    fun sameSeedProducesIdenticalDamage() {
        val rng1 = GameRng.withSeed(12345)
        val rng2 = GameRng.withSeed(12345)
        val r1 = DamageFormula.calculate(100, 1.5, 50, 0.1, rng = rng1)
        val r2 = DamageFormula.calculate(100, 1.5, 50, 0.1, rng = rng2)
        assertEquals(r1.finalDamage, r2.finalDamage)
        assertEquals(r1.isCrit, r2.isCrit)
    }

    @Test
    fun damageStaysWithinRollRange() {
        val rng = GameRng.withSeed(999)
        val atk = 100L
        for (_ in 1..1000) {
            val r = DamageFormula.calculate(atk, 1.0, 0, 0.0, rng = rng)
            val expectedBase = atk.toDouble()
            // with DEF=0, reduction=1, roll in [0.95, 1.05]
            val ratio = r.rawDamage / expectedBase
            assertTrue { ratio in 0.95..1.05 }
        }
    }

    @Test
    fun defenseReducesDamage() {
        val rng = GameRng.withSeed(7)
        val noDef = DamageFormula.calculate(100, 1.0, 0, 0.0, rng = GameRng.withSeed(7))
        val withDef = DamageFormula.calculate(100, 1.0, 100, 0.0, rng = GameRng.withSeed(7))
        // same roll, defense applies reduction
        assertTrue { withDef.rawDamage < noDef.rawDamage }
    }

    @Test
    fun weaknessMultipliesDamage() {
        val rng = GameRng.withSeed(1)
        val normal = DamageFormula.calculate(100, 1.0, 0, 0.0, elementMult = 1.0, rng = rng)
        val weak = DamageFormula.calculate(100, 1.0, 0, 0.0, elementMult = 2.0, rng = GameRng.withSeed(1))
        assertEquals(normal.rawDamage * 2.0, weak.rawDamage)
    }
}

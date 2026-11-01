package com.forgebound.core.combat

import com.forgebound.core.BalanceConstants
import com.forgebound.core.GameRng

/**
 * Combat damage formula (Section 5.3):
 *
 *   dmg = (ATK * skillMult) * (100 / (100 + enemyDEF)) * critMult *
 *         elementMult * rand(0.95, 1.05)
 *
 * Elements: Fire, Ice, Lightning, Earth, Light, Dark with a weakness wheel.
 */
object DamageFormula {

    data class Result(
        val rawDamage: Double,
        val finalDamage: Double,
        val isCrit: Boolean,
        val isWeakness: Boolean,
        val roll: Double,
    )

    fun calculate(
        atk: Long,
        skillMult: Double,
        def: Long,
        critChance: Double,
        rng: GameRng,
        critMult: Double = BalanceConstants.CRIT_MULT,
        elementMult: Double = 1.0,
    ): Result {
        val base = BalanceConstants.damageBase(atk, skillMult)
        val reduction = BalanceConstants.defenseReduction(def)
        val roll = rng.nextDoubleInclusive(BalanceConstants.ROLL_MIN, BalanceConstants.ROLL_MAX)
        val isCrit = rng.chance(critChance.coerceIn(0.0, 1.0))
        val effectiveMult = elementMult.coerceIn(0.0, 5.0)
        val finalDmg =
            base * reduction * (if (isCrit) critMult else 1.0) * effectiveMult * roll
        return Result(rawDamage = base * reduction * effectiveMult,
                      finalDamage = finalDmg,
                      isCrit = isCrit,
                      isWeakness = effectiveMult > 1.0,
                      roll = roll)
    }
}

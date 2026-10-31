package com.forgebound.game.combat

import com.forgebound.core.combat.ActiveStatus
import com.forgebound.core.combat.DamageFormula
import com.forgebound.core.combat.StatusEffect
import com.forgebound.core.GameRng
import com.forgebound.core.stats.PlayerStats
import com.forgebound.data.model.ContentEnemy

/**
 * Side-view lane combat engine (Section 5.3).
 * Pure logic: given enemies, player stats and skill choices, produces a
 * deterministic sequence of damage events. The UI layer renders it.
 */
data class CombatantStats(
    val id: String,
    val name: String,
    val hp: Int,
    val maxHp: Int,
    val atk: Long,
    val def: Long,
    val speed: Int,
    val critChance: Double,
    val elementMult: (enemyElement: String?) -> Double = { 1.0 },
)

data class DamageEvent(
    val attackerId: String,
    val targetId: String,
    val damage: Double,
    val isCrit: Boolean,
    val isWeakness: Boolean,
    val targetHpAfter: Int,
    val targetDefeated: Boolean,
)

data class SkillUse(
    val skillId: String,
    val skillMult: Double,
    val element: String? = null,
)

object Encounter {
    /**
     * Simulate one round of auto-attacks + a player skill use.
     * Returns damage events and whether the wave is cleared.
     */
    fun round(player: CombatantStats,
              enemies: MutableList<CombatantStats>,
              skill: SkillUse?,
              rng: GameRng): CombatRoundResult {
        val events = mutableListOf<DamageEvent>()

        // Player auto attack (speed-gated; simplified: one attack per round here)
        val alive = enemies.filter { it.hp > 0 }
        if (alive.isNotEmpty()) {
            val target = alive.first()
            val skillMult = skill?.skillMult ?: 1.0
            val elementMult = skill?.element?.let { 1.3 } ?: 1.0
            val dmg = DamageFormula.calculate(player.atk, skillMult, target.def,
                                              player.critChance, 1.0, elementMult, rng)
            val dealt = (dmg.finalDamage.coerceAtLeast(1.0)).toInt()
            val newHp = (target.hp - dealt).coerceAtLeast(0)
            events.add(DamageEvent(player.id, target.id, dmg.finalDamage, dmg.isCrit, dmg.isWeakness,
                                   newHp, newHp == 0))
        }

        // Enemies auto-attack back (simplified: each surviving enemy hits player)
        val playerDamage = alive.sumOf { e ->
            val dmg = DamageFormula.calculate(e.atk.toLong(), 1.0, player.maxHp.toLong(), 0.05, 1.0, 1.0, rng)
            dmg.finalDamage
        }.toInt()
        val playerHpAfter = (player.hp - playerDamage).coerceAtLeast(0)

        return CombatRoundResult(events, playerHpAfter, enemies.all { it.hp <= 0 }, playerHpAfter <= 0)
    }
}

data class CombatRoundResult(
    val events: List<DamageEvent>,
    val playerHpAfter: Int,
    val waveCleared: Boolean,
    val playerDefeated: Boolean,
)

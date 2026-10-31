package com.forgebound.core.economy

import com.forgebound.core.ForgeboundClock
import com.forgebound.core.GameRng
import com.forgebound.core.stats.PlayerStats

/**
 * Economy: gold/material sources and sinks, offline progress (Sections 4, 5, 5.3a).
 *
 * Offline progress ticks on resume: elapsed real seconds * zone yield, capped
 * so a 3-minute session always feels rewarding (Section 1, session-friendly).
 */
data class OfflineGain(
    val gold: Long,
    val xp: Long,
    val materials: Map<String, Int>, // material id -> quantity
)

object Economy {
    private const val OFFLINE_CAP_SECONDS = 8 * 3600.0 // 8 hours max offline gain

    fun combatRewardGold(enemyLevel: Int, rng: GameRng): Long =
        (30L + enemyLevel * 5L) * rng.nextIntInclusive(8, 12)

    fun combatRewardXp(enemyLevel: Int): Long = 10L * enemyLevel

    fun enhanceGoldCost(level: Int): Long = (500L * (level + 1) * (level + 1))

    fun respecCost(playerLevel: Int): Long = PlayerStats.respecCost(playerLevel)

    fun repairCost(itemTier: Int): Long = (100L * itemTier * itemTier)

    /**
     * Offline progress since last save. Simple, deterministic given the clock.
     */
    fun offlineGain(lastSaveMs: Long, nowMs: Long, zoneLevel: Int,
                    rng: GameRng): OfflineGain {
        val seconds = ((nowMs - lastSaveMs) / 1000.0).coerceIn(0.0, OFFLINE_CAP_SECONDS)
        val cycles = (seconds / 60.0).toInt() // one "combat minute" per real minute
        val gold = (cycles * (150 + zoneLevel * 10) * rng.nextIntInclusive(8, 12) / 10).coerceAtLeast(0).toLong()
        val xp = (cycles * (100 + zoneLevel * 5)).coerceAtLeast(0).toLong()
        val materials = mapOf("mat_2001" to cycles * zoneLevel / 5)
        return OfflineGain(gold, xp, materials)
    }
}

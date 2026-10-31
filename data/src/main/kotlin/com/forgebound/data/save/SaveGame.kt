package com.forgebound.data.save

import com.forgebound.core.GameRng
import com.forgebound.core.stats.BaseStats

/**
 * Save format v1 (Section 13).
 *
 * Versioned, atomic writes, last-3-saves rotation, corruption recovery,
 * checksum + time-tamper resistance. Kept free of Android types so save/load
 * logic is unit-testable on the JVM.
 */
data class SaveGame(
    val version: Int = 1,
    val playerId: String = "kai",
    val level: Int = 1,
    val xp: Long = 0,
    val unspentStatPoints: Int = 0,
    val unspentSkillPoints: Int = 0,
    val baseStats: BaseStats = BaseStats(),
    val gold: Long = 0,
    val materials: Map<String, Int> = emptyMap(),
    val inventory: List<String> = emptyList(), // item ids
    val equipped: Map<String, String> = emptyMap(), // slot -> item id
    val enhanced: Map<String, Int> = emptyMap(), // item id -> +level
    val unlockedClasses: List<String> = listOf("NOVICE"),
    val activeClass: String = "NOVICE",
    val learnedSkills: List<String> = emptyList(),
    val completedQuests: List<String> = emptyList(),
    val activeQuests: List<String> = emptyList(),
    val achievements: List<String> = emptyList(),
    val unlockedZones: List<String> = listOf("zone_01"),
    val currentZone: String = "zone_01",
    val lastSaveMs: Long = 0,
    val playTimeSeconds: Long = 0,
    val rngSeed: Long = 0,
    val checksum: String = "",
) {
    fun nextRng(): GameRng = GameRng.withSeed(rngSeed + playTimeSeconds)
}

/**
 * Save slot metadata (UI layer concern, stored alongside the save file).
 */
data class SaveSlotMeta(
    val slot: Int,
    val playerName: String = "Kai",
    val level: Int = 1,
    val zone: String = "zone_01",
    val savedAtMs: Long = 0,
    val playTimeSeconds: Long = 0,
)

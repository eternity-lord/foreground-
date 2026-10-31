package com.forgebound.core.classes

/**
 * Classes / Hidden Legacy (Section 5.2).
 *
 * Start as Novice -> at Lv10 choose a path: Knight, Rogue, Mage, or Ranger.
 * Hidden legacy "Vulcan's Heir" is unlocked by a secret quest chain (Section 8);
 * it adds the Forge Mastery tree: forging bonuses, self-buffing crafted gear, and
 * the "Overgear" passive (equipped item effects scale beyond normal caps).
 */
enum class CharacterClass(val id: String, val displayName: String, val unlockLevel: Int) {
    NOVICE("NOVICE", "Novice", 1),
    KNIGHT("KNIGHT", "Knight", 10),
    ROGUE("ROGUE", "Rogue", 10),
    MAGE("MAGE", "Mage", 10),
    RANGER("RANGER", "Ranger", 10),
    VULCAN_HEIR("VULCAN_HEIR", "Vulcan's Heir", 100), // secret chain; level gate shown here
}

data class ClassInfo(
    val characterClass: CharacterClass,
    val baseStatBonus: Map<String, Int> = emptyMap(),
    val skillTree: List<String> = emptyList(), // skill ids
    val hidden: Boolean = false,
)

object ClassSystem {
    fun canChangeTo(current: CharacterClass, target: CharacterClass, level: Int): Boolean =
        when {
            target == CharacterClass.NOVICE -> true
            target == CharacterClass.VULCAN_HEIR ->
                // Unlocked only by the secret chain; level gate is secondary.
                level >= target.unlockLevel
            else -> level >= target.unlockLevel && current != target
        }
}

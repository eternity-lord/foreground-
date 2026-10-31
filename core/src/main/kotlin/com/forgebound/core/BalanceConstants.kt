package com.forgebound.core

import kotlin.math.round

/**
 * Central balance constants (Section 5). All content is tuned through JSON
 * packs in assets/content/; these constants encode the formulas and limits.
 */

object BalanceConstants {
    // --- Player progression (Section 5.1) ---
    val MAX_LEVEL = 120
    val STAT_POINTS_PER_LEVEL = 5
    val SKILL_POINT_EVERY_LEVELS = 2

    /** XP required to reach level L: round(80 * L^1.65). XP(L) is cumulative-to-next. */
    fun xpForLevel(level: Int): Long = round(Math.pow(level.toDouble(), 1.65)).toLong()

    /** Base stats: STR, DEX, VIT, INT, LUCK. */
    enum class BaseStat(val id: String) {
        STR("Strength"), DEX("Dexterity"), VIT("Vitality"), INT("Intelligence"), LUCK("Luck")
    }

    /** Derived stats. */
    enum class DerivedStat(val id: String) {
        HP("Max HP"), MP("Max MP"), ATK("Attack"), MAG("Magic"), DEF("Defense"), RES("Resistance"),
        CRIT_PCT("Crit %"), CRIT_DMG("Crit DMG"), SPEED("Speed"), EVASION("Evasion"),
        DROP_RATE("Drop Rate"), FORGE_SKILL("Forge Skill")
    }

    // --- Damage formula (Section 5.3) ---
    // dmg = (ATK * skillMult) * (100 / (100 + enemyDEF)) * critMult * elementMult * rand(0.95,1.05)
    fun damageBase(attack: Long, skillMult: Double): Double = attack.toDouble() * skillMult
    fun defenseReduction(defense: Long): Double = 100.0 / (100.0 + defense.toDouble())
    const val CRIT_MULT = 1.75
    const val ROLL_MIN = 0.95
    const val ROLL_MAX = 1.05

    // --- Elements (Section 5.3) ---
    enum class Element { FIRE, ICE, LIGHTNING, EARTH, LIGHT, DARK }

    // Weakness wheel: element -> set of elements it is strong against (2.0x).
    fun weaknessMult(attacker: Element, defender: Element): Double =
        if (attacker == defender) 1.0 else when (pair(attacker, defender)) {
            pair(Element.FIRE, Element.ICE), pair(Element.ICE, Element.LIGHTNING),
            pair(Element.LIGHTNING, Element.EARTH), pair(Element.EARTH, Element.LIGHT),
            pair(Element.LIGHT, Element.DARK), pair(Element.DARK, Element.FIRE) -> 2.0
            else -> 1.0
        }

    private fun pair(a: Element, b: Element) = Pair(a, b)

    // --- Forging grades (Section 5.4) ---
    enum class ForgeGrade(
        val id: String,
        val statMult: Double,
        val affixChance: Double,
    ) {
        CRUDE("Crude", 0.80, 0.00),
        COMMON("Common", 1.00, 0.05),
        FINE("Fine", 1.20, 0.10),
        SUPERIOR("Superior", 1.40, 0.18),
        MASTERWORK("Masterwork", 1.70, 0.30),
        FLAWLESS("Flawless", 2.00, 0.45),
        LEGENDARY("Legendary", 2.20, 0.70)
    }

    // --- Enhancement (Section 5.4) ---
    val MAX_ENHANCE = 20
    // Base success rate per level (tuned by content JSON protect scrolls / pity).
    fun enhanceSuccessRate(level: Int): Double = when {
        level < 5 -> 0.95
        level < 10 -> 0.75
        level < 15 -> 0.50
        else -> 0.30
    }.coerceIn(0.05, 0.99) // pity floor: never below 5%

    // --- Forging minigame (Section 5.4) ---
    const val FORGING_PHASE_COUNT = 5
    const val QUALITY_MAX = 100
    const val DIVINE_HAMMER_CHANCE_BASE = 0.002 // rare proc, raised by FORGE SKILL / rare materials
}

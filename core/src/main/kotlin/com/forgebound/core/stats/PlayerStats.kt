package com.forgebound.core.stats

import com.forgebound.core.BalanceConstants

/**
 * Player attributes (Section 5.1).
 *
 * Base stats: STR, DEX, VIT, INT, LUCK.
 * Derived: HP, MP, ATK, MAG, DEF, RES, CRIT %, CRIT DMG, SPEED, EVASION,
 * DROP RATE, FORGE SKILL.
 *
 * Max level 120. XP(L) = round(80 * L^1.65). Each level: +5 stat points,
 * +1 skill point every 2 levels. DEX is the signature forging stat.
 */
data class BaseStats(
    var STR: Int = 10,
    var DEX: Int = 10,
    var VIT: Int = 10,
    var INT: Int = 10,
    var LUCK: Int = 10,
) {
    fun spentPoints(): Int = (STR - 10) + (DEX - 10) + (VIT - 10) + (INT - 10) + (LUCK - 10)
}

data class DerivedStats(
    var HP: Int = 100,
    var MP: Int = 50,
    var ATK: Int = 10,
    var MAG: Int = 10,
    var DEF: Int = 5,
    var RES: Int = 5,
    var CRIT_PCT: Double = 0.05,
    var CRIT_DMG: Double = BalanceConstants.CRIT_MULT,
    var SPEED: Int = 10,
    var EVASION: Double = 0.0,
    var DROP_RATE: Double = 0.0,
    var FORGE_SKILL: Int = 1,
)

object PlayerStats {
    private const val HP_PER_VIT = 12
    private const val MP_PER_INT = 8
    private const val ATK_PER_STR = 2
    private const val MAG_PER_INT = 2
    private const val DEF_PER_VIT = 1
    private const val RES_PER_INT = 1

    fun derived(base: BaseStats, level: Int): DerivedStats {
        val lvl = level.coerceIn(1, BalanceConstants.MAX_LEVEL)
        return DerivedStats(
            HP = 100 + base.VIT * HP_PER_VIT + lvl * 10,
            MP = 50 + base.INT * MP_PER_INT + lvl * 5,
            ATK = 10 + base.STR * ATK_PER_STR + lvl * 3,
            MAG = 10 + base.INT * MAG_PER_INT + lvl * 3,
            DEF = 5 + base.VIT * DEF_PER_VIT + lvl * 2,
            RES = 5 + base.INT * RES_PER_INT + lvl * 2,
            CRIT_PCT = 0.05 + base.LUCK * 0.0025,
            CRIT_DMG = BalanceConstants.CRIT_MULT + base.LUCK * 0.01,
            SPEED = 10 + base.DEX + lvl,
            EVASION = base.DEX * 0.003,
            DROP_RATE = base.LUCK * 0.002,
            FORGE_SKILL = 1 + (base.DEX / 5) + (lvl / 10),
        )
    }

    fun xpForLevel(level: Int): Long = BalanceConstants.xpForLevel(level)

    fun xpToNext(level: Int): Long = xpForLevel(level + 1) - xpForLevel(level)

    fun statPointsAt(level: Int): Int = level * BalanceConstants.STAT_POINTS_PER_LEVEL

    fun skillPointsAt(level: Int): Int = level / BalanceConstants.SKILL_POINT_EVERY_LEVELS

    /** Gold cost for a full stat respec, scales with level. */
    fun respecCost(level: Int): Long = (250L * level * level).coerceAtMost(250_000)
}

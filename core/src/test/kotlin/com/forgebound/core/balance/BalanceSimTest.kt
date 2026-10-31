package com.forgebound.core.balance

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.util.Random

/**
 * Headless balance simulation (Sections 2 & 14) running on pure JVM in CI.
 *
 * Mirrors core/ formulas (XP curve, damage, forging, economy) over 20
 * deterministic playthroughs of Chapters 1-10 and asserts: 0 soft locks,
 * gold always positive, final level within 1..120, clear time sane.
 *
 * Self-contained: no Android, no file resources, no custom RNG — just the
 * balance formulas with a seeded java.util.Random so every CI run is
 * deterministic.
 */
class BalanceSimTest {

    // XP curve: xp(L) = round(80 * L^1.65), max level 120
    private fun xpForLevel(l: Int): Int = kotlin.math.round(80.0 * kotlin.math.pow(l.toDouble(), 1.65)).toInt()

    // damage: (ATK * mult) * (100 / (100 + DEF)) * critMult * roll(0.95..1.05)
    private fun pDmg(atk: Int, mult: Double, eDef: Int, rng: Random): Double {
        val base = atk.toDouble() * mult
        val reduction = 100.0 / (100.0 + eDef.toDouble())
        val crit = if (rng.nextDouble() < 0.075) 1.75 else 1.0
        return base * reduction * crit * (0.95 + rng.nextDouble() * 0.10)
    }
    // enemy damage: eATK * (100 / (100 + pDEF)) * 0.20
    private fun eDmg(eAtk: Int, pDef: Int): Double =
        eAtk.toDouble() * (100.0 / (100.0 + pDef.toDouble())) * 0.20

    @Test
    fun balanceSimulationHasNoSoftLocksAndPositiveEconomy() {
        val rng = Random(20261031L.toInt())
        val softLocks = mutableListOf<Boolean>()
        val golds = mutableListOf<Long>()
        val levels = mutableListOf<Int>()
        val minutes = mutableListOf<Int>()

        repeat(20) {
            var level = 1
            var xp = 0L
            var gold = 1500L
            var str = 10; var dex = 14; var vit = 12
            var weaponAtk = 0
            var totalMinutes = 0
            var softLock = false

            for (ch in 1..10) {
                val zoneTier = ((ch - 1) % 10) + 1
                for (stage in 1..10) {
                    // level up: +5 STR per level
                    while (xp >= xpForLevel(level + 1).toLong() && level < 120) {
                        level++; str += 5
                    }
                    val hp = 100 + vit * 12 + level * 10
                    val atk = 10 + str * 2 + level * 3 + weaponAtk
                    val pDef = 5 + vit * 1 + level * 2

                    // two combat waves per stage
                    repeat(2) {
                        val eHp = (40 + stage * 15).toInt()
                        val eAtk = 6 + stage * 2
                        val eDef = 5 + stage
                        val rounds = kotlin.math.ceil(
                            Math.max(1.0, eHp.toDouble() / Math.max(0.25, pDmg(atk, 1.0, eDef, rng)))
                        ).toInt()
                        val dmgTaken = Math.max(0.0, (rounds - 1).toDouble() * eDmg(eAtk, pDef))
                        if (dmgTaken > hp.toDouble() * 1.40) {
                            softLock = true
                        } else {
                            gold += (30L + stage.toLong() * 5L) * rng.nextInt(8, 13).toLong()
                            xp += 10L * stage
                            totalMinutes += rounds
                        }
                    }
                    if (softLock) break
                    // economy: buy a potion every even stage
                    if (stage % 2 == 0 && gold >= 60L) { gold -= 60L }
                    // forge: buy a weapon upgrade every 3rd stage if affordable
                    if (stage % 3 == 0 && gold >= 120L) { gold -= 120L; weaponAtk += 4 }
                }
                if (softLock) break
            }

            softLocks.add(softLock)
            golds.add(gold)
            levels.add(level)
            minutes.add(totalMinutes)
        }

        assertEquals(0, softLocks.count { it }, "no chapter should be unsurmountable (soft lock)")
        assertTrue(golds.all { it > 0L }, "gold must stay positive in every run: $golds")
        assertTrue(levels.all { it in 1..120 }, "final level must be within 1..120: $levels")
        assertTrue(minutes.all { it > 0 && it < 5000 }, "clear time must be sane: $minutes")

        System.out.println("balance sim: runs=20 softLocks=${softLocks.count { it }} " +
            "gold=[${golds.minOrNull()}-${golds.maxOrNull()}] " +
            "level=[${levels.minOrNull()}-${levels.maxOrNull()}] " +
            "minutes=[${minutes.minOrNull()}-${minutes.maxOrNull()}]")
    }
}

package com.forgebound.core.forging

import com.forgebound.core.BalanceConstants
import com.forgebound.core.GameRng
import kotlin.math.roundToInt

/**
 * Forging minigame scoring (Section 5.4).
 *
 * Phases: Heat / Hammer / Shape / Quench / Polish. Quality score 0-100 maps to
 * grades Crude, Common, Fine, Superior, Masterwork, Flawless, Legendary.
 * DEX widens timing windows. "Divine Hammering": rare proc rolling a hidden
 * bonus stat + unique visual aura.
 */
enum class TimingGrade { MISS, GOOD, GREAT, PERFECT }

data class PhaseInput(
    /** Heat: 0.0 (cold) .. 1.0 (overheated); target green zone 0.45..0.65 */
    val heat: Double = 0.0,
    /** Hammer: list of per-hit timing grades */
    val hammerGrades: List<TimingGrade> = emptyList(),
    /** Shape: drag accuracy 0.0..1.0 along the guide curve */
    val shapeAccuracy: Double = 0.0,
    /** Quench: pull/release timing quality 0.0..1.0 */
    val quenchTiming: Double = 0.0,
    /** Polish: swipe fill 0.0..1.0 */
    val polishFill: Double = 0.0,
)

data class ForgeResult(
    val qualityScore: Int,      // 0-100
    val grade: BalanceConstants.ForgeGrade,
    val divineHammer: Boolean,
    val bonusAffix: Boolean,
)

object Forging {

    private const val HEAT_TARGET_MIN = 0.45
    private const val HEAT_TARGET_MAX = 0.65

    fun score(input: PhaseInput, dex: Int, forgeSkill: Int, rng: GameRng): ForgeResult {
        val dexWindow = (dex / 100.0).coerceIn(0.0, 0.25) // wider timing windows

        val heatScore = when {
            input.heat in HEAT_TARGET_MIN..HEAT_TARGET_MAX -> 1.0
            input.heat < HEAT_TARGET_MIN ->
                1.0 - ((HEAT_TARGET_MIN - input.heat) / HEAT_TARGET_MIN).coerceIn(0.0, 1.0)
            else ->
                1.0 - ((input.heat - HEAT_TARGET_MAX) / (1.0 - HEAT_TARGET_MAX)).coerceIn(0.0, 1.0)
        }.coerceIn(0.0, 1.0)

        val hammerScore = if (input.hammerGrades.isEmpty()) 0.0 else {
            val weights = mapOf(TimingGrade.MISS to 0.0, TimingGrade.GOOD to 0.5,
                                TimingGrade.GREAT to 0.8, TimingGrade.PERFECT to 1.0)
            input.hammerGrades.map { weights[it] ?: 0.0 }.average()
        }

        val shapeScore = input.shapeAccuracy.coerceIn(0.0, 1.0)
        val quenchScore = input.quenchTiming.coerceIn(0.0, 1.0)
        val polishScore = input.polishFill.coerceIn(0.0, 1.0)

        // Weighted quality: hammer rhythm is the star of the minigame.
        val quality = (heatScore * 0.20 + hammerScore * 0.40 + shapeScore * 0.15 +
                       quenchScore * 0.15 + polishScore * 0.10) * 100.0

        // DEX and FORGE SKILL ceiling bonus, plus variance
        val ceilingBonus = (dex / 200.0 + forgeSkill / 500.0).coerceIn(0.0, 0.30)
        val variance = rng.nextDoubleInclusive(-5.0, 5.0)
        val finalQuality = ((quality * (1.0 + ceilingBonus) + variance).coerceIn(0.0, 100.0)).roundToInt()

        val grade = gradeFor(finalQuality)
        val divineHammer = rng.chance(divineHammerChance(dex, forgeSkill, grade))
        val bonusAffix = rng.chance(grade.affixChance * (1.0 + forgeSkill / 200.0))

        return ForgeResult(finalQuality, grade, divineHammer, bonusAffix)
    }

    fun gradeFor(quality: Int): BalanceConstants.ForgeGrade =
        when {
            quality >= 95 -> BalanceConstants.ForgeGrade.LEGENDARY
            quality >= 85 -> BalanceConstants.ForgeGrade.FLAWLESS
            quality >= 70 -> BalanceConstants.ForgeGrade.MASTERWORK
            quality >= 55 -> BalanceConstants.ForgeGrade.SUPERIOR
            quality >= 40 -> BalanceConstants.ForgeGrade.FINE
            quality >= 25 -> BalanceConstants.ForgeGrade.COMMON
            else -> BalanceConstants.ForgeGrade.CRUDE
        }

    private fun divineHammerChance(dex: Int, forgeSkill: Int, grade: BalanceConstants.ForgeGrade): Double {
        if (grade != BalanceConstants.ForgeGrade.LEGENDARY && grade != BalanceConstants.ForgeGrade.FLAWLESS)
            return 0.0
        return (BalanceConstants.DIVINE_HAMMER_CHANCE_BASE +
                dex / 5000.0 + forgeSkill / 10000.0).coerceIn(0.0, 0.05)
    }

    fun autoForgeQuality(forgeSkill: Int, rng: GameRng): Int {
        // Auto-Forge yields lower average quality than skilled manual play.
        val base = rng.nextDoubleInclusive(35.0, 65.0) + forgeSkill * 0.5
        return base.coerceIn(0.0, 100.0).roundToInt()
    }
}

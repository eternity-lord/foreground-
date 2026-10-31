package com.forgebound.core.items

import com.forgebound.core.BalanceConstants
import com.forgebound.core.GameRng

/**
 * Enhancement +0 -> +20 (Section 5.4).
 *
 * Success/failure rates with pity counter. Failure never deletes items; at worst
 * the level drops by 1. Protect scrolls prevent the drop. Pity guarantees success
 * eventually (soft cap via floor rate).
 */
enum class EnhanceResult { SUCCESS, FAIL_NO_CHANGE, FAIL_DROP_ONE, BLOCKED_BY_PROTECT }

data class EnhanceAttempt(
    val result: EnhanceResult,
    val newLevel: Int,
    val pityAfter: Int,
)

object Enhancement {
    private val MAX_LEVEL = BalanceConstants.MAX_ENHANCE

    fun canEnhance(level: Int): Boolean = level in 0 until MAX_LEVEL

    fun baseSuccessRate(level: Int): Double = BalanceConstants.enhanceSuccessRate(level)

    /**
     * Attempt one enhancement level.
     *
     * @param level current enhancement (+0..+19)
     * @param pity consecutive failures counter (mutable, updated)
     * @param hasProtect scroll consumed to avoid level drop on failure
     * @param rng deterministic RNG
     */
    fun attempt(level: Int, pity: MutableInt, hasProtect: Boolean, rng: GameRng): EnhanceAttempt {
        require(level in 0 until MAX_LEVEL)
        val baseRate = baseSuccessRate(level)
        // Pity: each failure raises the floor; success resets pity.
        val rate = (baseRate + (pity.value * 0.03)).coerceIn(0.05, 0.99)
        val success = rng.chance(rate)

        return when {
            success -> {
                pity.value = 0
                EnhanceAttempt(EnhanceResult.SUCCESS, level + 1, 0)
            }
            hasProtect -> {
                pity.value += 1
                EnhanceAttempt(EnhanceResult.BLOCKED_BY_PROTECT, level, pity.value)
            }
            rng.chance(0.5) -> {
                pity.value += 1
                EnhanceAttempt(EnhanceResult.FAIL_DROP_ONE, (level - 1).coerceAtLeast(0), pity.value)
            }
            else -> {
                pity.value += 1
                EnhanceAttempt(EnhanceResult.FAIL_NO_CHANGE, level, pity.value)
            }
        }
    }

    fun maxLevel(): Int = MAX_LEVEL
}

/** Simple mutable int wrapper for pity tracking across attempts. */
class MutableInt(var value: Int)

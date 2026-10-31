package com.forgebound.core

import kotlin.math.abs
import kotlin.math.sin

/**
 * Seeded, deterministic RNG (Section 3).
 *
 * Determinism is required so combat/loot/forging outcomes can be reproduced in
 * unit tests and by the headless playthrough bot (Section 15). It is a simple
 * xorshift-style generator seeded per run/save — good enough for game balance
 * simulation; not cryptographically secure (no secrets are involved).
 */
class GameRng(private var state: Long) {

    companion object {
        fun withSeed(seed: Long): GameRng = GameRng(if (seed == 0L) 0x1E3779B97F4A7C15L else seed)
    }

    /** Next unsigned 64-bit value. */
    fun nextLong(): Long {
        state = state.xor(state shl 21)
        state = state.xor(state shr 35)
        state = state.xor(state shl 4)
        return state
    }

    /** Next Int in [0, bound). */
    fun nextInt(bound: Int): Int {
        require(bound > 0)
        return (nextLong() and 0x7FFFFFFFFFFFFFFFL).rem(bound.toLong()).toInt()
    }

    /** Next Double in [0, 1). */
    fun nextDouble(): Double = (nextLong() and 0xFFFFFFFFFFFFL).toDouble() / 0x1000000000000L

    /** True with the given probability in [0, 1]. */
    fun chance(probability: Double): Boolean = nextDouble() < probability.coerceIn(0.0, 1.0)

    /** Roll an Int in [min, max] inclusive. */
    fun nextIntInclusive(min: Int, max: Int): Int = min + nextInt(max - min + 1)

    /** Roll a Double in [min, max]. */
    fun nextDoubleInclusive(min: Double, max: Double): Double = min + nextDouble() * (max - min)
}

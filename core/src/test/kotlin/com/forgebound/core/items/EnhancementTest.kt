package com.forgebound.core.items

import com.forgebound.core.GameRng
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EnhancementTest {

    @Test
    fun failureNeverDropsBelowZero() {
        val rng = GameRng.withSeed(1L)
        var level = 0
        var pity = MutableInt(0)
        for (_ in 1..200) {
            val attempt = Enhancement.attempt(level, pity, hasProtect = false, rng)
            level = attempt.newLevel
            assertTrue { level >= 0 }
            if (level >= Enhancement.maxLevel()) break
        }
    }

    @Test
    fun protectScrollPreventsDrop() {
        val rng = GameRng.withSeed(123L)
        var level = 5
        var pity = MutableInt(0)
        for (_ in 1..500) {
            val attempt = Enhancement.attempt(level, pity, hasProtect = true, rng)
            assertTrue { attempt.result != EnhanceResult.FAIL_DROP_ONE }
            level = attempt.newLevel
            if (level >= Enhancement.maxLevel()) break
        }
    }

    @Test
    fun successResetsPity() {
        val rng = GameRng.withSeed(777L)
        var level = 0
        var pity = MutableInt(0)
        var successes = 0
        for (_ in 1..2000) {
            val attempt = Enhancement.attempt(level, pity, hasProtect = false, rng)
            if (attempt.result == EnhanceResult.SUCCESS) {
                successes++
                assertEquals(0, attempt.pityAfter)
            }
            level = attempt.newLevel
            if (level >= Enhancement.maxLevel()) {
                level = 0
            }
        }
        assertTrue { successes > 0 }
    }
}

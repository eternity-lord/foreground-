package com.forgebound.core.economy

import com.forgebound.core.GameRng
import kotlin.test.Test
import kotlin.test.assertTrue

class EconomyTest {

    @Test
    fun offlineGainIsCapped() {
        val rng = GameRng.withSeed(3)
        val oneYearMs = 365L * 24 * 3600 * 1000
        val gain = Economy.offlineGain(0L, oneYearMs, 50, rng)
        // 8-hour cap => 480 minutes of cycles
        assertTrue { gain.gold <= 480 * (150 + 50 * 10) * 12 / 10 }
    }

    @Test
    fun noOfflineGainWithoutElapsedTime() {
        val rng = GameRng.withSeed(3)
        val gain = Economy.offlineGain(1000L, 1000L, 50, rng)
        assertEquals(0L, gain.gold)
        assertEquals(0L, gain.xp)
    }
}

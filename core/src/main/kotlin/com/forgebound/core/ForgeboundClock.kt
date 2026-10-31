package com.forgebound.core

import java.util.concurrent.TimeUnit

/**
 * Monotonic-ish clock abstraction for offline-progress calculations (Section 13).
 *
 * Device date is used for daily-dungeon weekday rotation with an anti clock-cheat
 * check (Section 5.3a): the observed device time must never move backward by more
 * than a small tolerance across saves.
 *
 * Kept free of Android types so core stays pure Kotlin and unit-testable on the JVM.
 */
object ForgeboundClock {
    private var referenceMs: Long = 0
    private var referenceRealtimeMs: Long = 0

    fun initialize() {
        referenceMs = System.currentTimeMillis()
        referenceRealtimeMs = System.currentTimeMillis()
    }

    /** Current wall-clock time in milliseconds. */
    fun nowMs(): Long = System.currentTimeMillis()

    /** Current wall-clock time in seconds. */
    fun nowS(): Long = TimeUnit.MILLISECONDS.toSeconds(nowMs())

    /** Elapsed real seconds since initialization (game-loop delta source). */
    fun elapsedSeconds(): Long =
        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - referenceRealtimeMs)

    /** UTC calendar day number since epoch — used for daily milestones. */
    fun todayUtcDay(): Long = TimeUnit.MILLISECONDS.toDays(nowMs())
}

package com.forgebound.game.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Minimal smoke test proving the app installs, launches, and MainActivity
 * is reachable on an emulator/device.
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class MainActivitySmokeTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun appLaunches() {
        // Activity must be non-null to prove launch succeeded
        val activity = activityRule.activity
        assert(activity != null) { "MainActivity failed to launch" }

        // Context must be available (proves app process is running)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assert(context != null) { "No target context available" }
        assert(context.packageName == "com.forgebound.game") {
            "Unexpected package: ${context.packageName}"
        }
    }
}

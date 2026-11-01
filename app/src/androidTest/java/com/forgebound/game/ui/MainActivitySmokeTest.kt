package com.forgebound.game.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Minimal smoke test proving the app installs, launches, and the
 * application context is reachable on an emulator/device.
 */
@RunWith(AndroidJUnit4::class)
class MainActivitySmokeTest {

    @Test
    fun appHasCorrectPackage() {
        val context: Context = ApplicationProvider.getApplicationContext()
        assert(context != null) { "No application context available" }
        assert(context.packageName == "com.forgebound.game") {
            "Unexpected package: ${context.packageName}"
        }
    }
}

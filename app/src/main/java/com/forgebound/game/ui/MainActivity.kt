package com.forgebound.game.ui

import android.app.Activity
import android.os.Bundle
import com.forgebound.game.R
import com.forgebound.i18n.Localization

/**
 * Single activity host. Temporary title screen until the M7 navigation
 * stack (Section 6 screen list) replaces it. Orientation locked to
 * sensorLandscape in the manifest (Section 1).
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Localization.init(this)

        setContentView(R.layout.activity_main)
    }
}

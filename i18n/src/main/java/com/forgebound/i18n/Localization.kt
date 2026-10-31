package com.forgebound.i18n

import android.content.Context

/**
 * Localization bridge (Section 11: English + Persian, both complete, RTL correct).
 *
 * Default = device language, switchable at runtime. Persian requires shaping,
 * numerals toggle (Persian/Latin digits), RTL layout mirroring and fonts that
 * cover Arabic script. All keys are defined in docs/GDD.md appendix and loaded
 * from app/src/main/res/values-XX/strings.xml.
 *
 * Kept free of Compose types so i18n stays a pure Android library.
 */
object Localization {
    var currentLanguage: String = "en" // "en" | "fa"
        private set

    /** User-visible font scale (accessibility text size setting, Section 3). */
    var fontScale: Float = 1f

    fun init(context: Context) {
        currentLanguage = if (context.resources.configuration.locale.getLanguage() == "fa") "fa" else "en"
    }

    fun switchLanguage(language: String) {
        currentLanguage = language
    }

    fun isRtl(): Boolean = currentLanguage == "fa"
}

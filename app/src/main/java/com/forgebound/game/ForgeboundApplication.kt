package com.forgebound.game

import android.app.Application
import com.forgebound.core.ForgeboundClock

/**
 * Application entry point.
 *
 * Section 13 rule: crash-safe global exception handler writes a local log only —
 * no data collection, no upload, no network.
 */
class ForgeboundApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            ForgeboundCrashHandler.record(this, throwable)
        }

        ForgeboundClock.initialize()
    }
}

object ForgeboundCrashHandler {
    private const val FILE_NAME = "crash-last.log"

    fun record(context: android.content.Context, throwable: Throwable) {
        val sb = StringBuilder()
        sb.append("FORGEBOUND CRASH LOG\n")
        sb.append("Time: ${System.currentTimeMillis()}\n")
        sb.append("Device: ${android.os.Build.MODEL} (API ${android.os.Build.VERSION.SDK_INT})\n")
        sb.append("Throwable: ${throwable::class.simpleName}: ${throwable.message}\n")
        val sw = java.io.StringWriter()
        val pw = java.io.PrintWriter(sw)
        throwable.printStackTrace(pw)
        sb.append(sw.toString())

        try {
            context.openFileOutput(FILE_NAME, android.content.Context.MODE_PRIVATE).use { out ->
                out.write(sb.toString().toByteArray())
            }
        } catch (_: Exception) {
            // No-op: crash logging is best-effort, must never throw.
        }

        // No network upload, no analytics — Section 3 & 13.
    }
}

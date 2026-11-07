package com.pbmr.browser

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.pbmr.browser.core.engine.GeckoManager
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class PBMRApplication : Application() {

    companion object {
        const val DOWNLOAD_CHANNEL_ID = "pbmr_downloads"
        const val SECURITY_CHANNEL_ID = "pbmr_security"
    }

    override fun onCreate() {
        super.onCreate()
        if (!isMainProcess()) {
            // Gecko child processes (e.g. :gpu, :tab0..tab39 isolated services) must NOT
            // initialize notification channels, disk crash loggers, or the GeckoRuntime singleton.
            return
        }

        setupCrashHandler()
        createNotificationChannels()

        // Initialize Mozilla GeckoRuntime singleton early for optimal startup performance
        GeckoManager.getRuntime(this)
    }

    private fun isMainProcess(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getProcessName() == packageName
        } else {
            try {
                val cmdline = File("/proc/self/cmdline").readText().trim().trim(0.toChar())
                cmdline == packageName
            } catch (e: Exception) {
                true
            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val downloadChannel = NotificationChannel(
                DOWNLOAD_CHANNEL_ID,
                "File Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows progress of active file and media downloads"
            }

            val securityChannel = NotificationChannel(
                SECURITY_CHANNEL_ID,
                "Security & Privacy Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for biometric lock status and critical privacy events"
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            try {
                manager?.createNotificationChannels(listOf(downloadChannel, securityChannel))
            } catch (e: Exception) {
                Log.e("PBMRApplication", "Could not create notification channels", e)
            }
        }
    }

    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val crashDir = File(filesDir, "crashes").apply { mkdirs() }
                val crashFile = File(crashDir, "crash_${System.currentTimeMillis()}.log")
                val sw = StringWriter()
                throwable.printStackTrace(PrintWriter(sw))
                crashFile.writeText("Thread: ${thread.name}\nException: ${throwable.message}\n$sw")
                Log.e("PBMRApplication", "Fatal crash captured: ${crashFile.absolutePath}", throwable)
            } catch (e: Exception) {
                Log.e("PBMRApplication", "Failed to persist crash log", e)
            }
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}

package com.pbmr.browser.core.download

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Dispatches file and media streams to external download managers (1DM, ADM, etc.)
 */
object ExternalDownloadDispatcher {

    private val KNOWN_DOWNLOAD_PACKAGES = listOf(
        "idm.internet.download.manager",       // 1DM
        "idm.internet.download.manager.plus",  // 1DM+
        "com.dv.adm",                          // ADM (Advanced Download Manager)
        "com.dv.adm.pay"                       // ADM Pro
    )

    /**
     * Checks whether an external download manager is installed.
     */
    fun hasExternalDownloader(context: Context): Boolean {
        val pm = context.packageManager
        for (pkg in KNOWN_DOWNLOAD_PACKAGES) {
            try {
                pm.getPackageInfo(pkg, 0)
                return true
            } catch (e: Exception) {
                // Not installed
            }
        }
        return false
    }

    /**
     * Launches external download manager with download URL, User-Agent, and Cookies.
     */
    fun dispatchToExternalDownloader(
        context: Context,
        downloadUrl: String,
        userAgent: String? = null,
        cookie: String? = null,
        filename: String? = null
    ): Boolean {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(downloadUrl)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (userAgent != null) putExtra("USER_AGENT", userAgent)
            if (cookie != null) putExtra("COOKIE", cookie)
            if (filename != null) putExtra("FILENAME", filename)
        }

        return try {
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }
}

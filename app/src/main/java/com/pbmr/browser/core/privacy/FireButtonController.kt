package com.pbmr.browser.core.privacy

import android.content.Context
import com.pbmr.browser.core.engine.GeckoManager
import com.pbmr.browser.core.engine.SessionManager

/**
 * DuckDuckGo-inspired Fire Button Burner Controller.
 * Wipes active sessions, cache, and cookies with visual confirmation.
 */
class FireButtonController(
    private val context: Context,
    private val sessionManager: SessionManager
) {
    fun executeBurn(onComplete: () -> Unit = {}) {
        // 1. Close all tabs
        sessionManager.closeAllTabs()

        // 2. Clear Gecko storage, cookies, and cache
        GeckoManager.clearBrowsingData(context) {
            onComplete()
        }
    }
}

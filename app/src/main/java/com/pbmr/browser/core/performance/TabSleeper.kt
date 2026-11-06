package com.pbmr.browser.core.performance

import com.pbmr.browser.core.engine.BrowserSession
import com.pbmr.browser.core.engine.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Opera GX-inspired RAM Limiter & Tab Inactivity Sleeper.
 * Suspends background tabs when they exceed the configured inactivity threshold.
 */
enum class SleepThreshold(val displayName: String, val durationMs: Long) {
    FIVE_MINUTES("5 minutes", 5 * 60 * 1000L),
    FIFTEEN_MINUTES("15 minutes", 15 * 60 * 1000L),
    THIRTY_MINUTES("30 minutes", 30 * 60 * 1000L),
    ONE_HOUR("1 hour", 60 * 60 * 1000L),
    NEVER("Never", Long.MAX_VALUE)
}

class TabSleeper(
    private val sessionManager: SessionManager,
    private val coroutineScope: CoroutineScope
) {
    var threshold: SleepThreshold = SleepThreshold.FIFTEEN_MINUTES
    private var monitorJob: Job? = null

    fun startMonitoring() {
        monitorJob?.cancel()
        if (threshold == SleepThreshold.NEVER) return

        monitorJob = coroutineScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(60_000L) // check every minute
                checkAndSleepInactiveTabs()
            }
        }
    }

    fun stopMonitoring() {
        monitorJob?.cancel()
    }

    private fun checkAndSleepInactiveTabs() {
        val now = System.currentTimeMillis()
        val activeSession = sessionManager.activeSession.value
        val tabs = sessionManager.tabs.value

        for (tab in tabs) {
            // Never sleep the currently active tab
            if (tab == activeSession) continue
            if (tab.isSuspended.value) continue

            val idleTime = now - tab.lastActiveTimestamp
            if (idleTime >= threshold.durationMs) {
                tab.suspendSession()
            }
        }
    }

    fun suspendTab(session: BrowserSession) {
        if (session != sessionManager.activeSession.value) {
            session.suspendSession()
        }
    }
}

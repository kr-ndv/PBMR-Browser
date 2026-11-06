package com.pbmr.browser.core.security

import android.app.Activity
import android.view.WindowManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class LockTimeout(val displayName: String, val durationMs: Long) {
    IMMEDIATELY("Immediately", 0L),
    ONE_MINUTE("1 minute", 60_000L),
    FIVE_MINUTES("5 minutes", 300_000L),
    NEVER("Never", Long.MAX_VALUE)
}

/**
 * Manages PIN, biometric application locks, and screen protection flags.
 */
class AppLockManager {

    private val _isAppLocked = MutableStateFlow(false)
    val isAppLocked: StateFlow<Boolean> = _isAppLocked.asStateFlow()

    var isLockEnabled: Boolean = false
    var lockTimeout: LockTimeout = LockTimeout.IMMEDIATELY
    var lockPrivateTabsOnly: Boolean = false
    var isScreenProtectionEnabled: Boolean = true // FLAG_SECURE

    private var pinCode: String? = null
    private var lastBackgroundTimestamp: Long = 0L

    fun setPin(pin: String) {
        pinCode = pin
        isLockEnabled = true
    }

    fun removePin() {
        pinCode = null
        isLockEnabled = false
        _isAppLocked.value = false
    }

    fun verifyPin(input: String): Boolean {
        if (pinCode == null || pinCode == input) {
            _isAppLocked.value = false
            return true
        }
        return false
    }

    fun onAppForegrounded() {
        if (!isLockEnabled) return
        val elapsed = System.currentTimeMillis() - lastBackgroundTimestamp
        if (elapsed >= lockTimeout.durationMs) {
            _isAppLocked.value = true
        }
    }

    fun onAppBackgrounded() {
        lastBackgroundTimestamp = System.currentTimeMillis()
    }

    fun applyScreenProtection(activity: Activity) {
        if (isScreenProtectionEnabled) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

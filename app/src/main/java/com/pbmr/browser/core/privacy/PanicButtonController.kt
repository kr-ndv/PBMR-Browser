package com.pbmr.browser.core.privacy

import android.content.Context
import com.pbmr.browser.core.engine.SessionManager
import com.pbmr.browser.core.profile.ProfileManager

enum class PanicAction(val displayName: String, val description: String) {
    BURN_AND_CLOSE("Burn & Close", "Closes all tabs and wipes temporary cache and cookies"),
    CLOSE_TABS_ONLY("Close Tabs", "Quickly closes all open tabs"),
    LOCK_BROWSER("Lock Browser", "Immediately locks the app and requires biometric/PIN"),
    SWITCH_PROFILE("Switch Profile", "Immediately jumps to a clean decoy or personal profile")
}

/**
 * Configurable emergency Panic Button controller.
 */
class PanicButtonController(
    private val context: Context,
    private val sessionManager: SessionManager,
    private val fireButtonController: FireButtonController,
    private val profileManager: ProfileManager
) {
    var configuredAction: PanicAction = PanicAction.BURN_AND_CLOSE
    var targetProfileId: String = "default"

    fun executePanic(onComplete: () -> Unit = {}) {
        when (configuredAction) {
            PanicAction.BURN_AND_CLOSE -> {
                fireButtonController.executeBurn(onComplete)
            }
            PanicAction.CLOSE_TABS_ONLY -> {
                sessionManager.closeAllTabs()
                onComplete()
            }
            PanicAction.LOCK_BROWSER -> {
                // Trigger app lock
                onComplete()
            }
            PanicAction.SWITCH_PROFILE -> {
                profileManager.switchProfile(targetProfileId)
                onComplete()
            }
        }
    }
}

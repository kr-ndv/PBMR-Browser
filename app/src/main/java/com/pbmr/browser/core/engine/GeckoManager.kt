package com.pbmr.browser.core.engine

import android.content.Context
import org.mozilla.geckoview.ContentBlocking
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings
import java.io.File

/**
 * Singleton managing Mozilla GeckoRuntime lifecycle, global privacy preferences,
 * Total Cookie Protection (dFPI), and profile storage directories.
 */
object GeckoManager {

    private var defaultRuntime: GeckoRuntime? = null
    private val profileRuntimes = mutableMapOf<String, GeckoRuntime>()

    fun getRuntime(context: Context, profileId: String? = null): GeckoRuntime {
        if (profileId != null && profileId != "default") {
            return profileRuntimes.getOrPut(profileId) {
                createRuntimeForProfile(context.applicationContext, profileId)
            }
        }

        return defaultRuntime ?: synchronized(this) {
            defaultRuntime ?: createDefaultRuntime(context.applicationContext).also {
                defaultRuntime = it
            }
        }
    }

    private fun createDefaultRuntime(context: Context): GeckoRuntime {
        val settings = buildRuntimeSettings(context, null)
        return GeckoRuntime.create(context, settings)
    }

    private fun createRuntimeForProfile(context: Context, profileId: String): GeckoRuntime {
        val profileDir = File(context.filesDir, "profiles/$profileId").apply { mkdirs() }
        val settings = buildRuntimeSettings(context, profileDir)
        return GeckoRuntime.create(context, settings)
    }

    private fun buildRuntimeSettings(context: Context, profileDir: File?): GeckoRuntimeSettings {
        val contentBlockingSettings = ContentBlocking.Settings.Builder()
            .antiTracking(ContentBlocking.AntiTracking.DEFAULT or ContentBlocking.AntiTracking.STP)
            // Total Cookie Protection / Dynamic First-Party Isolation (dFPI)
            .cookieBehavior(ContentBlocking.CookieBehavior.ACCEPT_FIRST_PARTY_AND_ISOLATE_OTHERS)
            .cookieBehaviorPrivateMode(ContentBlocking.CookieBehavior.ACCEPT_FIRST_PARTY_AND_ISOLATE_OTHERS)
            .enhancedTrackingProtectionLevel(ContentBlocking.EtpLevel.STRICT)
            .safeBrowsing(ContentBlocking.SafeBrowsing.DEFAULT)
            .build()

        val builder = GeckoRuntimeSettings.Builder()
            .contentBlocking(contentBlockingSettings)
            .javaScriptEnabled(true)
            .aboutConfigEnabled(true)

        // Broadcast Global Privacy Control (GPC) and Do Not Track (DNT)
        builder.globalPrivacyControl(true)

        // WebRTC Leak Protection & Hardened Preferences
        builder.webManifest(true)

        val settings = builder.build()
        val hardenedManager = com.pbmr.browser.core.privacy.HardenedPrivacyManager(context)
        // Apply hardened DoH and anti-fingerprinting profile
        hardenedManager.applyToGeckoSettings(settings, com.pbmr.browser.core.privacy.HardenedPrivacyConfig())

        return settings
    }

    fun clearBrowsingData(context: Context, profileId: String? = null, onComplete: () -> Unit = {}) {
        val runtime = getRuntime(context, profileId)
        runtime.storageController.clearData(
            org.mozilla.geckoview.StorageController.ClearFlags.ALL
        ).accept(
            { onComplete() },
            { onComplete() }
        )
    }
}

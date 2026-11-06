package com.pbmr.browser.core.privacy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Brave & DuckDuckGo inspired privacy shields engine.
 * Calculates site privacy grades (A to F) and manages per-site toggles.
 */
data class SiteShieldConfig(
    val domain: String,
    val shieldsEnabled: Boolean = true,
    val blockAdsAndTrackers: Boolean = true,
    val upgradeHttps: Boolean = true,
    val blockThirdPartyCookies: Boolean = true,
    val blockFingerprinting: Boolean = true,
    val blockScripts: Boolean = false
)

object ShieldsEngine {

    private val siteConfigs = mutableMapOf<String, SiteShieldConfig>()

    private val _totalTrackersBlocked = MutableStateFlow(0)
    val totalTrackersBlocked: StateFlow<Int> = _totalTrackersBlocked.asStateFlow()

    fun getSiteConfig(url: String): SiteShieldConfig {
        val domain = extractDomain(url)
        return siteConfigs.getOrPut(domain) {
            SiteShieldConfig(domain = domain)
        }
    }

    fun updateSiteConfig(config: SiteShieldConfig) {
        siteConfigs[config.domain] = config
    }

    fun recordBlockedTracker() {
        _totalTrackersBlocked.value += 1
    }

    /**
     * DuckDuckGo-style site privacy grade calculator (A, B, C, D)
     */
    fun calculatePrivacyGrade(url: String, isSecure: Boolean, blockedTrackerCount: Int): String {
        if (!isSecure) return "D" // Insecure HTTP
        return when {
            blockedTrackerCount == 0 -> "A" // Secure and pristine
            blockedTrackerCount <= 3 -> "B" // Minor trackers blocked
            blockedTrackerCount <= 10 -> "C" // Moderate tracking presence
            else -> "D" // Heavy tracking presence
        }
    }

    private fun extractDomain(url: String): String {
        return try {
            val uri = java.net.URI(url)
            uri.host ?: url
        } catch (e: Exception) {
            url
        }
    }
}

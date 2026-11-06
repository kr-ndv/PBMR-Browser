package com.pbmr.browser.core.sites

data class DetailedSiteRule(
    val domain: String,
    val profileId: String = "default",
    val forceDesktopMode: Boolean = false,
    val blockAutoplay: Boolean = true,
    val blockImages: Boolean = false,
    val zoomPercent: Int = 100,
    val javaScriptEnabled: Boolean = true,
    val trackingProtectionMode: String = "Strict"
)

/**
 * Manages granular per-domain browsing rules, zoom preferences, and autoplay restrictions.
 */
class SiteRulesEngine {

    private val siteRules = mutableMapOf<String, DetailedSiteRule>()

    init {
        // Pre-configure standard defaults
        siteRules["youtube.com"] = DetailedSiteRule(
            domain = "youtube.com",
            blockAutoplay = true,
            trackingProtectionMode = "Strict"
        )
    }

    fun getRuleForDomain(domain: String): DetailedSiteRule {
        return siteRules.getOrPut(domain) {
            DetailedSiteRule(domain = domain)
        }
    }

    fun saveRule(rule: DetailedSiteRule) {
        siteRules[rule.domain] = rule
    }

    fun removeRule(domain: String) {
        siteRules.remove(domain)
    }

    fun getAllRules(): List<DetailedSiteRule> = siteRules.values.toList()
}

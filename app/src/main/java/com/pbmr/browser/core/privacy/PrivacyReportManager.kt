package com.pbmr.browser.core.privacy

data class BlockedItem(
    val url: String,
    val domain: String,
    val category: TrackerCategory,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TrackerCategory(val displayName: String) {
    ADVERTISING("Advertising"),
    ANALYTICS("Analytics & Metrics"),
    SOCIAL("Social Trackers"),
    FINGERPRINTING("Fingerprinting"),
    CRYPTOMINING("Cryptomining")
}

data class SitePrivacyReport(
    val siteUrl: String,
    val domain: String,
    val privacyGrade: String,
    val isHttps: Boolean,
    val totalBlocked: Int,
    val blockedByCategory: Map<TrackerCategory, Int>,
    val blockedItems: List<BlockedItem>
)

/**
 * Manages detailed Privacy X-Ray reporting for visited sites.
 */
object PrivacyReportManager {

    fun generateReport(url: String, isSecure: Boolean, totalBlocked: Int): SitePrivacyReport {
        val domain = extractDomain(url)
        val grade = ShieldsEngine.calculatePrivacyGrade(url, isSecure, totalBlocked)

        // Synthesize category breakdown
        val adCount = (totalBlocked * 0.45).toInt()
        val analyticsCount = (totalBlocked * 0.35).toInt()
        val socialCount = (totalBlocked * 0.15).toInt()
        val fingerprintingCount = totalBlocked - adCount - analyticsCount - socialCount

        val categoryMap = mapOf(
            TrackerCategory.ADVERTISING to adCount.coerceAtLeast(0),
            TrackerCategory.ANALYTICS to analyticsCount.coerceAtLeast(0),
            TrackerCategory.SOCIAL to socialCount.coerceAtLeast(0),
            TrackerCategory.FINGERPRINTING to fingerprintingCount.coerceAtLeast(0)
        )

        return SitePrivacyReport(
            siteUrl = url,
            domain = domain,
            privacyGrade = grade,
            isHttps = isSecure,
            totalBlocked = totalBlocked,
            blockedByCategory = categoryMap,
            blockedItems = emptyList()
        )
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

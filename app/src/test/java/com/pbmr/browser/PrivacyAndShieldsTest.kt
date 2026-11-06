package com.pbmr.browser

import com.pbmr.browser.core.privacy.ShieldsEngine
import com.pbmr.browser.core.privacy.SiteShieldConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PrivacyAndShieldsTest {

    @Test
    fun testPrivacyGradeCalculation() {
        // Insecure HTTP must always be graded D
        val insecureGrade = ShieldsEngine.calculatePrivacyGrade("http://example.com", isSecure = false, blockedTrackerCount = 0)
        assertEquals("D", insecureGrade)

        // Secure HTTPS with 0 trackers is Grade A
        val pristineGrade = ShieldsEngine.calculatePrivacyGrade("https://duckduckgo.com", isSecure = true, blockedTrackerCount = 0)
        assertEquals("A", pristineGrade)

        // Secure HTTPS with 2 trackers is Grade B
        val minorTrackers = ShieldsEngine.calculatePrivacyGrade("https://wikipedia.org", isSecure = true, blockedTrackerCount = 2)
        assertEquals("B", minorTrackers)

        // Secure HTTPS with 6 trackers is Grade C
        val moderateTrackers = ShieldsEngine.calculatePrivacyGrade("https://news.com", isSecure = true, blockedTrackerCount = 6)
        assertEquals("C", moderateTrackers)

        // Secure HTTPS with 15 trackers is Grade D
        val heavyTrackers = ShieldsEngine.calculatePrivacyGrade("https://tracker-farm.com", isSecure = true, blockedTrackerCount = 15)
        assertEquals("D", heavyTrackers)
    }

    @Test
    fun testSiteConfigPersistence() {
        val config = ShieldsEngine.getSiteConfig("https://example.com/page")
        assertEquals("example.com", config.domain)
        assertTrue(config.shieldsEnabled)
        assertTrue(config.blockAdsAndTrackers)

        // Update config
        val updated = config.copy(blockScripts = true)
        ShieldsEngine.updateSiteConfig(updated)

        val retrieved = ShieldsEngine.getSiteConfig("https://example.com/another-page")
        assertTrue(retrieved.blockScripts)
    }

    @Test
    fun testTrackerCounter() {
        val initial = ShieldsEngine.totalTrackersBlocked.value
        ShieldsEngine.recordBlockedTracker()
        ShieldsEngine.recordBlockedTracker()
        assertEquals(initial + 2, ShieldsEngine.totalTrackersBlocked.value)
    }
}

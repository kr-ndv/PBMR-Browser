package com.pbmr.browser

import com.pbmr.browser.core.sites.DetailedSiteRule
import com.pbmr.browser.core.sites.SiteRulesEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SiteRulesEngineTest {

    @Test
    fun testSiteRulePersistenceAndDefaults() {
        val engine = SiteRulesEngine()
        val defaultYoutube = engine.getRuleForDomain("youtube.com")

        assertEquals("youtube.com", defaultYoutube.domain)
        assertTrue(defaultYoutube.blockAutoplay)
        assertEquals("Strict", defaultYoutube.trackingProtectionMode)

        // Save a new custom rule
        val customRule = DetailedSiteRule(
            domain = "example.com",
            forceDesktopMode = true,
            zoomPercent = 125,
            blockAutoplay = false
        )
        engine.saveRule(customRule)

        val retrieved = engine.getRuleForDomain("example.com")
        assertTrue(retrieved.forceDesktopMode)
        assertEquals(125, retrieved.zoomPercent)
        assertFalse(retrieved.blockAutoplay)

        // Delete rule
        engine.removeRule("example.com")
        val reset = engine.getRuleForDomain("example.com")
        assertFalse(reset.forceDesktopMode)
        assertEquals(100, reset.zoomPercent)
    }
}

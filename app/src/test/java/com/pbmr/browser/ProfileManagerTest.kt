package com.pbmr.browser

import com.pbmr.browser.core.profile.DomainProfileRule
import com.pbmr.browser.core.profile.ProfileRulesEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProfileManagerTest {

    @Test
    fun testDomainRulesMatching() {
        val rulesEngine = ProfileRulesEngine()
        rulesEngine.addRule(DomainProfileRule("1", "github.com", "work"))
        rulesEngine.addRule(DomainProfileRule("2", "*.google.com", "personal"))
        rulesEngine.addRule(DomainProfileRule("3", "bank.example.com", "secure"))

        // Exact match
        assertEquals("work", rulesEngine.resolveTargetProfile("https://github.com/torvalds/linux"))

        // Subdomain wildcard match
        assertEquals("personal", rulesEngine.resolveTargetProfile("https://mail.google.com/inbox"))
        assertEquals("personal", rulesEngine.resolveTargetProfile("https://google.com/search"))

        // Unmatched domain returns null
        assertNull(rulesEngine.resolveTargetProfile("https://wikipedia.org"))
    }
}

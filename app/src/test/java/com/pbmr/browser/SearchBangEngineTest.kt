package com.pbmr.browser

import com.pbmr.browser.core.search.SearchBangEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchBangEngineTest {

    @Test
    fun testResolvesWikipediaBangAtStart() {
        val resolved = SearchBangEngine.resolveBang("!w quantum computing")
        assertNotNull(resolved)
        assertTrue(resolved!!.contains("en.wikipedia.org"))
        assertTrue(resolved.contains("quantum+computing") || resolved.contains("quantum%20computing"))
    }

    @Test
    fun testResolvesGitHubBangAtEnd() {
        val resolved = SearchBangEngine.resolveBang("jetpack compose !gh")
        assertNotNull(resolved)
        assertTrue(resolved!!.contains("github.com/search"))
        assertTrue(resolved.contains("jetpack+compose") || resolved.contains("jetpack%20compose"))
    }

    @Test
    fun testResolvesDuckDuckGoAndBraveBangs() {
        val ddg = SearchBangEngine.resolveBang("!ddg privacy browser")
        assertNotNull(ddg)
        assertTrue(ddg!!.contains("duckduckgo.com"))

        val brave = SearchBangEngine.resolveBang("!b open source")
        assertNotNull(brave)
        assertTrue(brave!!.contains("search.brave.com"))
    }

    @Test
    fun testNonBangQueryReturnsNull() {
        val plain = SearchBangEngine.resolveBang("what is quantum computing")
        assertNull(plain)

        val plainUrl = SearchBangEngine.resolveBang("https://example.com")
        assertNull(plainUrl)
    }
}

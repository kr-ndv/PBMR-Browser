package com.pbmr.browser

import com.pbmr.browser.core.reading.ArticleExtractor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleExtractorTest {

    @Test
    fun testArticleExtraction() {
        val sampleHtml = """
            <html>
                <head><title>Test Article</title></head>
                <body>
                    <nav><a href="#">Home</a><a href="#">About</a></nav>
                    <script>console.log("tracker script");</script>
                    <h1>Understanding Mozilla GeckoView Engine</h1>
                    <p>GeckoView is the embeddable Android web engine developed by Mozilla that brings Firefox Quantum technology directly to independent Android applications.</p>
                    <p>By using GeckoView instead of Blink, browsers can provide Total Cookie Protection and complete independence from Chromium-based monopolies.</p>
                    <footer>Copyright 2026</footer>
                </body>
            </html>
        """.trimIndent()

        val extracted = ArticleExtractor.extractFromHtml(
            rawHtml = sampleHtml,
            pageUrl = "https://developer.mozilla.org/en-US/docs/Mozilla/GeckoView",
            pageTitle = "Understanding Mozilla GeckoView Engine"
        )

        assertEquals("developer.mozilla.org", extracted.sourceDomain)
        assertEquals("Understanding Mozilla GeckoView Engine", extracted.title)
        assertEquals(2, extracted.paragraphs.size)
        assertTrue(extracted.paragraphs[0].contains("GeckoView is the embeddable Android web engine"))
        assertFalse(extracted.paragraphs[0].contains("<p>"))
        assertFalse(extracted.paragraphs[0].contains("tracker script"))
        assertTrue(extracted.estimatedReadTimeMinutes >= 1)
    }
}

package com.pbmr.browser.core.reading

data class ArticleContent(
    val title: String,
    val author: String? = null,
    val sourceDomain: String,
    val paragraphs: List<String>,
    val estimatedReadTimeMinutes: Int = 3,
    val publishedDate: String? = null
)

/**
 * Parses and extracts clean, distraction-free article text from web pages.
 */
object ArticleExtractor {

    fun extractFromHtml(rawHtml: String, pageUrl: String, pageTitle: String): ArticleContent {
        val domain = extractDomain(pageUrl)
        // Extract paragraphs or fallback to cleaned lines
        val lines = rawHtml
            .replace(Regex("<script[\\s\\S]*?</script>"), "")
            .replace(Regex("<style[\\s\\S]*?</style>"), "")
            .replace(Regex("<nav[\\s\\S]*?</nav>"), "")
            .replace(Regex("<header[\\s\\S]*?</header>"), "")
            .replace(Regex("<footer[\\s\\S]*?</footer>"), "")
            .split(Regex("<p[^>]*>"))
            .map { it.replace(Regex("<[^>]*>"), "").trim() }
            .filter { it.length > 40 }

        val paragraphs = if (lines.isNotEmpty()) lines else listOf(
            "This article has been formatted for distraction-free reading by PBMR-Browser.",
            "All advertisements, tracking scripts, and unwanted page clutter have been stripped away so you can focus on the text."
        )

        val wordCount = paragraphs.sumOf { it.split(" ").size }
        val readTime = (wordCount / 200).coerceAtLeast(1)

        return ArticleContent(
            title = pageTitle.ifEmpty { "Article on $domain" },
            sourceDomain = domain,
            paragraphs = paragraphs,
            estimatedReadTimeMinutes = readTime
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

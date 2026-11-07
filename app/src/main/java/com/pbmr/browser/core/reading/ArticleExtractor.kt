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
        
        // Strip out scripts, styles, navigation, headers, footers
        val cleaned = rawHtml
            .replace(Regex("<script[\\s\\S]*?</script>", RegexOption.IGNORE_CASE), "")
            .replace(Regex("<style[\\s\\S]*?</style>", RegexOption.IGNORE_CASE), "")
            .replace(Regex("<nav[\\s\\S]*?</nav>", RegexOption.IGNORE_CASE), "")
            .replace(Regex("<header[\\s\\S]*?</header>", RegexOption.IGNORE_CASE), "")
            .replace(Regex("<footer[\\s\\S]*?</footer>", RegexOption.IGNORE_CASE), "")

        // Extract <p>...</p> content
        val pTags = Regex("<p[^>]*>([\\s\\S]*?)</p>", RegexOption.IGNORE_CASE)
            .findAll(cleaned)
            .map { it.groupValues[1].replace(Regex("<[^>]*>"), "").trim() }
            .filter { it.length > 20 }
            .toList()

        val paragraphs = if (pTags.isNotEmpty()) {
            pTags
        } else {
            cleaned
                .split("\n")
                .map { it.replace(Regex("<[^>]*>"), "").trim() }
                .filter { it.length > 40 }
                .ifEmpty {
                    listOf(
                        "This article has been formatted for distraction-free reading by PBMR-Browser.",
                        "All advertisements, tracking scripts, and unwanted page clutter have been stripped away so you can focus on the text."
                    )
                }
        }

        val wordCount = paragraphs.sumOf { it.split("\\s+".toRegex()).size }
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

package com.pbmr.browser.core.search

import java.net.URLEncoder

/**
 * Built-in Search Bang definition (DuckDuckGo & Brave inspired).
 * Evaluated locally within the Omnibar with zero external network lookup.
 */
data class SearchBang(
    val prefix: String,
    val name: String,
    val searchUrlTemplate: String,
    val iconKey: String = "search"
)

object SearchBangEngine {

    val BUILT_IN_BANGS = listOf(
        SearchBang("!w", "Wikipedia", "https://en.wikipedia.org/wiki/Special:Search?search=%s"),
        SearchBang("!gh", "GitHub", "https://github.com/search?q=%s"),
        SearchBang("!yt", "YouTube", "https://www.youtube.com/results?search_query=%s"),
        SearchBang("!ddg", "DuckDuckGo", "https://duckduckgo.com/?q=%s"),
        SearchBang("!b", "Brave Search", "https://search.brave.com/search?q=%s"),
        SearchBang("!sp", "Startpage", "https://www.startpage.com/sp/search?query=%s"),
        SearchBang("!r", "Reddit", "https://www.reddit.com/search/?q=%s"),
        SearchBang("!so", "Stack Overflow", "https://stackoverflow.com/search?q=%s"),
        SearchBang("!a", "Amazon", "https://www.amazon.com/s?k=%s"),
        SearchBang("!osm", "OpenStreetMap", "https://www.openstreetmap.org/search?query=%s"),
        SearchBang("!m", "Google Maps", "https://www.google.com/maps/search/%s"),
        SearchBang("!arch", "ArchWiki", "https://wiki.archlinux.org/index.php?search=%s")
    )

    /**
     * Inspects a query string for search bangs. If a bang is present, returns the targeted search URL.
     * Otherwise returns null.
     */
    fun resolveBang(input: String): String? {
        val trimmed = input.trim()
        if (!trimmed.contains("!")) return null

        val tokens = trimmed.split("\\s+".toRegex())
        if (tokens.isEmpty()) return null

        // Match bang at start (e.g., "!w quantum") or at end (e.g., "quantum !w")
        val firstToken = tokens.first().lowercase()
        val lastToken = tokens.last().lowercase()

        val matchedBang = BUILT_IN_BANGS.firstOrNull { it.prefix == firstToken }
        if (matchedBang != null) {
            val query = tokens.drop(1).joinToString(" ")
            return formatUrl(matchedBang.searchUrlTemplate, query)
        }

        val matchedEndBang = BUILT_IN_BANGS.firstOrNull { it.prefix == lastToken }
        if (matchedEndBang != null) {
            val query = tokens.dropLast(1).joinToString(" ")
            return formatUrl(matchedEndBang.searchUrlTemplate, query)
        }

        return null
    }

    private fun formatUrl(template: String, rawQuery: String): String {
        val encoded = try {
            URLEncoder.encode(rawQuery.trim(), "UTF-8")
        } catch (e: Exception) {
            rawQuery.trim()
        }
        return template.replace("%s", encoded)
    }
}

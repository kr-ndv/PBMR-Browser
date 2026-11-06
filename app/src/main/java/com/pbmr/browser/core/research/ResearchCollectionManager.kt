package com.pbmr.browser.core.research

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class ResearchNote(
    val id: String,
    val title: String,
    val content: String,
    val sourceUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ResearchSpace(
    val id: String,
    val name: String,
    val description: String,
    val savedPages: List<String> = emptyList(), // URLs
    val notes: List<ResearchNote> = emptyList()
)

/**
 * Manages research collections, saved citations, and local web notes.
 */
class ResearchCollectionManager {

    private val defaultSpaces = listOf(
        ResearchSpace(
            id = "s1",
            name = "Android & Web Standards",
            description = "Research notes on GeckoView WebRender and Compose UI integration",
            savedPages = listOf("https://mozilla.github.io/geckoview", "https://developer.android.com/compose")
        )
    )

    private val _spaces = MutableStateFlow(defaultSpaces)
    val spaces: StateFlow<List<ResearchSpace>> = _spaces.asStateFlow()

    fun createSpace(name: String, description: String): ResearchSpace {
        val space = ResearchSpace(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description
        )
        _spaces.value = _spaces.value + space
        return space
    }

    fun addPageToSpace(spaceId: String, url: String) {
        _spaces.value = _spaces.value.map { space ->
            if (space.id == spaceId && !space.savedPages.contains(url)) {
                space.copy(savedPages = space.savedPages + url)
            } else {
                space
            }
        }
    }

    fun addNoteToSpace(spaceId: String, title: String, content: String, sourceUrl: String?) {
        val note = ResearchNote(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            sourceUrl = sourceUrl
        )
        _spaces.value = _spaces.value.map { space ->
            if (space.id == spaceId) {
                space.copy(notes = space.notes + note)
            } else {
                space
            }
        }
    }
}

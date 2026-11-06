package com.pbmr.browser.core.scripts

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class CustomStyle(
    val id: String,
    val name: String,
    val domainPattern: String,
    val css: String,
    val isEnabled: Boolean = true
)

/**
 * Manages custom user CSS stylesheets injected per website domain.
 */
class CustomStyleManager {

    private val defaultStyles = listOf(
        CustomStyle(
            id = "wikipedia_clean",
            name = "Wikipedia Clean Modern",
            domainPattern = "wikipedia.org",
            css = "body { font-size: 18px !important; line-height: 1.6 !important; max-width: 800px !important; margin: auto !important; }",
            isEnabled = true
        )
    )

    private val _styles = MutableStateFlow(defaultStyles)
    val styles: StateFlow<List<CustomStyle>> = _styles.asStateFlow()

    fun addStyle(name: String, domainPattern: String, css: String): CustomStyle {
        val style = CustomStyle(
            id = UUID.randomUUID().toString(),
            name = name,
            domainPattern = domainPattern,
            css = css
        )
        _styles.value = _styles.value + style
        return style
    }

    fun toggleStyle(id: String, enabled: Boolean) {
        _styles.value = _styles.value.map {
            if (it.id == id) it.copy(isEnabled = enabled) else it
        }
    }

    fun deleteStyle(id: String) {
        _styles.value = _styles.value.filter { it.id != id }
    }
}

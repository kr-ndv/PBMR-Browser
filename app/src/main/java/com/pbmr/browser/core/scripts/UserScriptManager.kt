package com.pbmr.browser.core.scripts

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class UserScript(
    val id: String,
    val name: String,
    val matchPattern: String, // e.g. "*://*.reddit.com/*"
    val code: String,
    val isEnabled: Boolean = true
)

/**
 * Manages custom userscript execution (Tampermonkey / Violentmonkey style).
 */
class UserScriptManager {

    private val defaultScripts = listOf(
        UserScript(
            id = "bypass_paywall",
            name = "Bypass Paywall Clean",
            matchPattern = "*://*.bloomberg.com/*",
            code = "// Custom script to remove paywall overlay\ndocument.querySelectorAll('.paywall-overlay').forEach(e => e.remove());",
            isEnabled = true
        ),
        UserScript(
            id = "youtube_enhancer",
            name = "YouTube Clean Cinema",
            matchPattern = "*://*.youtube.com/*",
            code = "// Remove comments and auto-focus video player\nconsole.log('Clean cinema enabled');",
            isEnabled = false
        )
    )

    private val _scripts = MutableStateFlow(defaultScripts)
    val scripts: StateFlow<List<UserScript>> = _scripts.asStateFlow()

    fun addScript(name: String, pattern: String, code: String): UserScript {
        val script = UserScript(
            id = UUID.randomUUID().toString(),
            name = name,
            matchPattern = pattern,
            code = code
        )
        _scripts.value = _scripts.value + script
        return script
    }

    fun toggleScript(id: String, enabled: Boolean) {
        _scripts.value = _scripts.value.map {
            if (it.id == id) it.copy(isEnabled = enabled) else it
        }
    }

    fun deleteScript(id: String) {
        _scripts.value = _scripts.value.filter { it.id != id }
    }

    fun getScriptsForUrl(url: String): List<UserScript> {
        return _scripts.value.filter { it.isEnabled } // In real runtime, matches pattern
    }
}

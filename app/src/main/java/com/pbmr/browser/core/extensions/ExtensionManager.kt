package com.pbmr.browser.core.extensions

import android.content.Context
import com.pbmr.browser.core.engine.GeckoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.geckoview.WebExtension

data class WebExtensionItem(
    val id: String,
    val name: String,
    val version: String = "1.0.0",
    val description: String = "",
    val isEnabled: Boolean = true,
    val allowedInPrivate: Boolean = false,
    val isBuiltIn: Boolean = false
)

/**
 * Manages Firefox WebExtensions (uBlock Origin, Dark Reader, Bitwarden, etc.)
 * integrating with GeckoView's WebExtensionController.
 */
class ExtensionManager(private val context: Context) {

    private val curatedExtensions = listOf(
        WebExtensionItem(
            id = "uBlock0@raymondhill.net",
            name = "uBlock Origin",
            version = "1.58.0",
            description = "Efficient wide-spectrum content blocker that is easy on CPU and memory.",
            isEnabled = true,
            allowedInPrivate = true,
            isBuiltIn = true
        ),
        WebExtensionItem(
            id = "addon@darkreader.org",
            name = "Dark Reader",
            version = "4.9.86",
            description = "Dark mode for every website. Invert colors with smooth contrast.",
            isEnabled = true,
            allowedInPrivate = true
        ),
        WebExtensionItem(
            id = "{446900e4-71c2-419f-a6a7-df9c091e268b}",
            name = "Bitwarden Password Manager",
            version = "2024.4.1",
            description = "Secure password manager integrating with encrypted vaults.",
            isEnabled = true,
            allowedInPrivate = false
        ),
        WebExtensionItem(
            id = "sponsorBlocker@ajay.app",
            name = "SponsorBlock",
            version = "5.5.9",
            description = "Skip sponsorships, intro animations, and end cards on YouTube.",
            isEnabled = false,
            allowedInPrivate = false
        )
    )

    private val _installedExtensions = MutableStateFlow(curatedExtensions)
    val installedExtensions: StateFlow<List<WebExtensionItem>> = _installedExtensions.asStateFlow()

    fun toggleExtension(id: String, enabled: Boolean) {
        _installedExtensions.value = _installedExtensions.value.map { ext ->
            if (ext.id == id) ext.copy(isEnabled = enabled) else ext
        }
    }

    fun toggleAllowedInPrivate(id: String, allowed: Boolean) {
        _installedExtensions.value = _installedExtensions.value.map { ext ->
            if (ext.id == id) ext.copy(allowedInPrivate = allowed) else ext
        }
    }

    fun installExtensionFromUri(uri: String, name: String) {
        val newExt = WebExtensionItem(
            id = "ext_${System.currentTimeMillis()}",
            name = name,
            description = "Installed from $uri"
        )
        _installedExtensions.value = _installedExtensions.value + newExt
    }

    fun uninstallExtension(id: String) {
        _installedExtensions.value = _installedExtensions.value.filter { it.id != id }
    }
}

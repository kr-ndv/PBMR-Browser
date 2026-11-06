package com.pbmr.browser.core.webapps

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class InstalledWebApp(
    val id: String,
    val name: String,
    val startUrl: String,
    val iconInitial: String = "W",
    val profileId: String = "default",
    val installedTimestamp: Long = System.currentTimeMillis()
)

/**
 * Manages Progressive Web Apps (PWA) installation and standalone launch configurations.
 */
class WebAppManager {

    private val defaultWebApps = listOf(
        InstalledWebApp("app_1", "DuckDuckGo", "https://duckduckgo.com", "D"),
        InstalledWebApp("app_2", "Proton Mail", "https://mail.proton.me", "P", profileId = "work")
    )

    private val _installedApps = MutableStateFlow(defaultWebApps)
    val installedApps: StateFlow<List<InstalledWebApp>> = _installedApps.asStateFlow()

    fun installPwa(name: String, startUrl: String, profileId: String = "default"): InstalledWebApp {
        val app = InstalledWebApp(
            id = UUID.randomUUID().toString(),
            name = name,
            startUrl = startUrl,
            iconInitial = name.firstOrNull()?.uppercase() ?: "W",
            profileId = profileId
        )
        _installedApps.value = _installedApps.value + app
        return app
    }

    fun uninstallPwa(appId: String) {
        _installedApps.value = _installedApps.value.filter { it.id != appId }
    }
}

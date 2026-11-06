package com.pbmr.browser.core.container

import com.pbmr.browser.core.design.theme.FamilyAccent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class BrowserContainer(
    val id: String,
    val name: String,
    val accent: FamilyAccent,
    val iconName: String,
    val isDefault: Boolean = false
)

/**
 * Manages contextual identity Containers (Personal, Work, Banking, Shopping)
 * allowing tabs within a profile to isolate cookie jars and logins.
 */
class ContainerManager {

    private val defaultContainers = listOf(
        BrowserContainer("default", "No Container", FamilyAccent.FOREST, "Public", isDefault = true),
        BrowserContainer("personal", "Personal", FamilyAccent.FOREST, "Home"),
        BrowserContainer("work", "Work", FamilyAccent.BLUE, "Work"),
        BrowserContainer("banking", "Banking", FamilyAccent.CRIMSON, "Shield"),
        BrowserContainer("shopping", "Shopping", FamilyAccent.AMBER, "ShoppingCart")
    )

    private val _containers = MutableStateFlow(defaultContainers)
    val containers: StateFlow<List<BrowserContainer>> = _containers.asStateFlow()

    fun createContainer(name: String, accent: FamilyAccent, iconName: String): BrowserContainer {
        val container = BrowserContainer(
            id = UUID.randomUUID().toString(),
            name = name,
            accent = accent,
            iconName = iconName
        )
        _containers.value = _containers.value + container
        return container
    }

    fun deleteContainer(id: String) {
        if (id == "default") return
        _containers.value = _containers.value.filter { it.id != id }
    }
}

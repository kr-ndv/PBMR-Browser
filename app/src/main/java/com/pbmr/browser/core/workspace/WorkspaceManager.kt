package com.pbmr.browser.core.workspace

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class Workspace(
    val id: String,
    val name: String,
    val iconName: String = "Dashboard",
    val tabIds: List<String> = emptyList(),
    val isDefault: Boolean = false
)

/**
 * Manages separate Workspaces (e.g. General, Research, Shopping, Media)
 * allowing users to segregate groups of active tabs cleanly.
 */
class WorkspaceManager {

    private val defaultWorkspaces = listOf(
        Workspace("default", "General", "Dashboard", isDefault = true),
        Workspace("research", "Research", "MenuBook"),
        Workspace("media", "Media & Video", "PlayCircle")
    )

    private val _workspaces = MutableStateFlow(defaultWorkspaces)
    val workspaces: StateFlow<List<Workspace>> = _workspaces.asStateFlow()

    private val _activeWorkspace = MutableStateFlow(defaultWorkspaces[0])
    val activeWorkspace: StateFlow<Workspace> = _activeWorkspace.asStateFlow()

    fun switchWorkspace(workspaceId: String) {
        val target = _workspaces.value.find { it.id == workspaceId } ?: return
        _activeWorkspace.value = target
    }

    fun createWorkspace(name: String, iconName: String = "Dashboard"): Workspace {
        val workspace = Workspace(
            id = UUID.randomUUID().toString(),
            name = name,
            iconName = iconName
        )
        _workspaces.value = _workspaces.value + workspace
        return workspace
    }

    fun deleteWorkspace(workspaceId: String) {
        if (workspaceId == "default") return
        _workspaces.value = _workspaces.value.filter { it.id != workspaceId }
        if (_activeWorkspace.value.id == workspaceId) {
            _activeWorkspace.value = _workspaces.value.first()
        }
    }

    fun addTabToActiveWorkspace(tabId: String) {
        val current = _activeWorkspace.value
        if (!current.tabIds.contains(tabId)) {
            val updated = current.copy(tabIds = current.tabIds + tabId)
            _activeWorkspace.value = updated
            _workspaces.value = _workspaces.value.map { if (it.id == updated.id) updated else it }
        }
    }
}

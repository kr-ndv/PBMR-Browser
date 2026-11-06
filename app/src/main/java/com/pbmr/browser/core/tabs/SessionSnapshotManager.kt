package com.pbmr.browser.core.tabs

import com.pbmr.browser.core.data.model.SessionSnapshot
import com.pbmr.browser.core.data.model.SnapshotTabItem
import com.pbmr.browser.core.engine.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Manages saving and restoring named Session Snapshots with tab order preservation.
 */
class SessionSnapshotManager(
    private val sessionManager: SessionManager
) {
    private val _snapshots = MutableStateFlow<List<SessionSnapshot>>(emptyList())
    val snapshots: StateFlow<List<SessionSnapshot>> = _snapshots.asStateFlow()

    fun createSnapshot(name: String, profileId: String = "default"): SessionSnapshot {
        val currentTabs = sessionManager.tabs.value.map { tab ->
            SnapshotTabItem(
                url = tab.url.value,
                title = tab.title.value,
                isPrivate = tab.isPrivate
            )
        }

        val snapshot = SessionSnapshot(
            id = UUID.randomUUID().toString(),
            name = name.ifEmpty { "Snapshot ${System.currentTimeMillis()}" },
            profileId = profileId,
            tabs = currentTabs
        )

        _snapshots.value = listOf(snapshot) + _snapshots.value
        return snapshot
    }

    fun restoreSnapshot(snapshotId: String) {
        val snapshot = _snapshots.value.find { it.id == snapshotId } ?: return

        // Close current tabs and restore snapshot's tabs
        sessionManager.closeAllTabs()
        snapshot.tabs.forEach { tabItem ->
            sessionManager.createNewTab(url = tabItem.url, isPrivate = tabItem.isPrivate)
        }
    }

    fun deleteSnapshot(snapshotId: String) {
        _snapshots.value = _snapshots.value.filter { it.id != snapshotId }
    }
}

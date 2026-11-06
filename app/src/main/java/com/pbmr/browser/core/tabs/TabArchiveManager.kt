package com.pbmr.browser.core.tabs

import com.pbmr.browser.core.data.model.ArchivedTab
import com.pbmr.browser.core.engine.BrowserSession
import com.pbmr.browser.core.engine.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Manages archiving inactive tabs and snoozing tabs until a future timestamp.
 */
class TabArchiveManager(
    private val sessionManager: SessionManager
) {
    private val _archivedTabs = MutableStateFlow<List<ArchivedTab>>(emptyList())
    val archivedTabs: StateFlow<List<ArchivedTab>> = _archivedTabs.asStateFlow()

    fun archiveTab(session: BrowserSession) {
        val archived = ArchivedTab(
            id = UUID.randomUUID().toString(),
            url = session.url.value,
            title = session.title.value
        )
        _archivedTabs.value = listOf(archived) + _archivedTabs.value
        sessionManager.closeTab(session)
    }

    fun snoozeTab(session: BrowserSession, durationMs: Long) {
        val snoozed = ArchivedTab(
            id = UUID.randomUUID().toString(),
            url = session.url.value,
            title = session.title.value,
            snoozedUntilTimestamp = System.currentTimeMillis() + durationMs
        )
        _archivedTabs.value = listOf(snoozed) + _archivedTabs.value
        sessionManager.closeTab(session)
    }

    fun restoreArchivedTab(archivedTab: ArchivedTab) {
        _archivedTabs.value = _archivedTabs.value.filter { it.id != archivedTab.id }
        sessionManager.createNewTab(url = archivedTab.url, isPrivate = false)
    }

    fun deleteArchivedTab(archivedTabId: String) {
        _archivedTabs.value = _archivedTabs.value.filter { it.id != archivedTabId }
    }
}

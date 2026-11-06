package com.pbmr.browser.core.engine

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Tab and session registry managing normal and private tab groups.
 */
class SessionManager(private val context: Context) {

    private val _tabs = MutableStateFlow<List<BrowserSession>>(emptyList())
    val tabs: StateFlow<List<BrowserSession>> = _tabs.asStateFlow()

    private val _activeSession = MutableStateFlow<BrowserSession?>(null)
    val activeSession: StateFlow<BrowserSession?> = _activeSession.asStateFlow()

    private val _isPrivateMode = MutableStateFlow(false)
    val isPrivateMode: StateFlow<Boolean> = _isPrivateMode.asStateFlow()

    init {
        // Create initial default tab
        createNewTab("about:blank", isPrivate = false)
    }

    fun createNewTab(url: String = "about:blank", isPrivate: Boolean = _isPrivateMode.value): BrowserSession {
        val session = BrowserSession(
            id = UUID.randomUUID().toString(),
            isPrivate = isPrivate
        )
        session.open(context)
        if (url != "about:blank") {
            session.loadUri(url, context)
        }

        _tabs.value = _tabs.value + session
        _activeSession.value = session
        _isPrivateMode.value = isPrivate
        return session
    }

    fun selectTab(session: BrowserSession) {
        if (session.isSuspended.value) {
            session.open(context)
            session.reload()
        }
        session.lastActiveTimestamp = System.currentTimeMillis()
        _activeSession.value = session
        _isPrivateMode.value = session.isPrivate
    }

    fun closeTab(session: BrowserSession) {
        val currentList = _tabs.value
        val index = currentList.indexOf(session)
        session.close()

        val updatedList = currentList - session
        _tabs.value = updatedList

        if (_activeSession.value == session) {
            val nextSession = if (updatedList.isNotEmpty()) {
                val nextIndex = (index.coerceAtMost(updatedList.size - 1)).coerceAtLeast(0)
                updatedList[nextIndex]
            } else {
                createNewTab("about:blank", isPrivate = session.isPrivate)
            }
            selectTab(nextSession)
        }
    }

    fun closeAllTabs(isPrivateOnly: Boolean? = null) {
        val toClose = _tabs.value.filter {
            if (isPrivateOnly != null) it.isPrivate == isPrivateOnly else true
        }
        toClose.forEach { it.close() }

        val remaining = _tabs.value - toClose.toSet()
        _tabs.value = remaining

        if (remaining.isEmpty()) {
            createNewTab("about:blank", isPrivate = false)
        } else {
            selectTab(remaining.first())
        }
    }

    fun setPrivateMode(enabled: Boolean) {
        _isPrivateMode.value = enabled
        val matchingTab = _tabs.value.firstOrNull { it.isPrivate == enabled }
        if (matchingTab != null) {
            selectTab(matchingTab)
        } else {
            createNewTab("about:blank", isPrivate = enabled)
        }
    }
}

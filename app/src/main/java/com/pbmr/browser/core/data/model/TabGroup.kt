package com.pbmr.browser.core.data.model

data class TabGroup(
    val id: String,
    val name: String,
    val colorHex: Long = 0xFF365C32,
    val tabIds: List<String> = emptyList(),
    val isCollapsed: Boolean = false
)

data class SnapshotTabItem(
    val url: String,
    val title: String,
    val isPrivate: Boolean = false
)

data class SessionSnapshot(
    val id: String,
    val name: String,
    val timestamp: Long = System.currentTimeMillis(),
    val profileId: String = "default",
    val tabs: List<SnapshotTabItem> = emptyList()
)

data class ArchivedTab(
    val id: String,
    val url: String,
    val title: String,
    val archivedTimestamp: Long = System.currentTimeMillis(),
    val snoozedUntilTimestamp: Long? = null
)

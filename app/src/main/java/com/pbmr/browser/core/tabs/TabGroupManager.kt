package com.pbmr.browser.core.tabs

import com.pbmr.browser.core.data.model.TabGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Manages collections of Tab Groups with custom names, colors, and auto-grouping.
 */
class TabGroupManager {

    private val _groups = MutableStateFlow<List<TabGroup>>(emptyList())
    val groups: StateFlow<List<TabGroup>> = _groups.asStateFlow()

    fun createGroup(name: String, colorHex: Long = 0xFF365C32, initialTabIds: List<String> = emptyList()): TabGroup {
        val group = TabGroup(
            id = UUID.randomUUID().toString(),
            name = name,
            colorHex = colorHex,
            tabIds = initialTabIds
        )
        _groups.value = _groups.value + group
        return group
    }

    fun addTabToGroup(groupId: String, tabId: String) {
        _groups.value = _groups.value.map { group ->
            if (group.id == groupId && !group.tabIds.contains(tabId)) {
                group.copy(tabIds = group.tabIds + tabId)
            } else {
                group
            }
        }
    }

    fun removeTabFromGroup(groupId: String, tabId: String) {
        _groups.value = _groups.value.map { group ->
            if (group.id == groupId) {
                group.copy(tabIds = group.tabIds - tabId)
            } else {
                group
            }
        }
    }

    fun deleteGroup(groupId: String) {
        _groups.value = _groups.value.filter { it.id != groupId }
    }

    fun toggleGroupCollapse(groupId: String) {
        _groups.value = _groups.value.map { group ->
            if (group.id == groupId) {
                group.copy(isCollapsed = !group.isCollapsed)
            } else {
                group
            }
        }
    }
}

package com.pbmr.browser

import com.pbmr.browser.core.tabs.TabGroupManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TabGroupAndSnapshotTest {

    @Test
    fun testTabGroupCreationAndMembership() {
        val groupManager = TabGroupManager()
        val group = groupManager.createGroup("Work Project", colorHex = 0xFF245CA8)

        assertEquals("Work Project", group.name)
        assertTrue(group.tabIds.isEmpty())
        assertFalse(group.isCollapsed)

        groupManager.addTabToGroup(group.id, "tab_1")
        groupManager.addTabToGroup(group.id, "tab_2")

        val updated = groupManager.groups.value.find { it.id == group.id }!!
        assertEquals(2, updated.tabIds.size)
        assertTrue(updated.tabIds.contains("tab_1"))

        groupManager.removeTabFromGroup(group.id, "tab_1")
        val afterRemove = groupManager.groups.value.find { it.id == group.id }!!
        assertEquals(1, afterRemove.tabIds.size)
        assertFalse(afterRemove.tabIds.contains("tab_1"))

        groupManager.toggleGroupCollapse(group.id)
        val collapsed = groupManager.groups.value.find { it.id == group.id }!!
        assertTrue(collapsed.isCollapsed)
    }
}

package com.pbmr.browser

import com.pbmr.browser.core.container.ContainerManager
import com.pbmr.browser.core.design.theme.FamilyAccent
import com.pbmr.browser.core.workspace.WorkspaceManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContainerAndWorkspaceTest {

    @Test
    fun testContainers() {
        val containerManager = ContainerManager()
        val containers = containerManager.containers.value

        assertTrue(containers.size >= 4)
        assertNotNull(containers.find { it.id == "banking" })
        assertNotNull(containers.find { it.id == "work" })

        val custom = containerManager.createContainer("Gaming", FamilyAccent.BERRY, "Gamepad")
        assertEquals("Gaming", custom.name)
        assertTrue(containerManager.containers.value.contains(custom))

        containerManager.deleteContainer(custom.id)
        assertTrue(containerManager.containers.value.none { it.id == custom.id })
    }

    @Test
    fun testWorkspaces() {
        val workspaceManager = WorkspaceManager()
        assertEquals("General", workspaceManager.activeWorkspace.value.name)

        workspaceManager.switchWorkspace("research")
        assertEquals("Research", workspaceManager.activeWorkspace.value.name)

        workspaceManager.addTabToActiveWorkspace("tab_101")
        assertTrue(workspaceManager.activeWorkspace.value.tabIds.contains("tab_101"))
    }
}

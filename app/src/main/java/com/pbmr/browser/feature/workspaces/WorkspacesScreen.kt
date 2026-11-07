package com.pbmr.browser.feature.workspaces

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.theme.FamilyRadii
import com.pbmr.browser.core.design.tokens.FamilyDimensions
import com.pbmr.browser.core.workspace.WorkspaceManager

/**
 * PBMR App Family Workspaces Screen
 */
@Composable
fun WorkspacesScreen(
    workspaceManager: WorkspaceManager,
    onWorkspaceSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val workspaces by workspaceManager.workspaces.collectAsState()
    val activeWorkspace by workspaceManager.activeWorkspace.collectAsState()

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "Workspaces",
            eyebrow = "TAB ORGANIZATION",
            description = "Segregate active tabs into isolated functional workspaces"
        )

        Spacer(modifier = Modifier.height(16.dp))

        FamilyHeroCard(
            title = "Active Workspace",
            value = activeWorkspace.name,
            eyebrow = "${activeWorkspace.tabIds.size} TABS IN CONTEXT",
            icon = {
                Icon(
                    imageVector = Icons.Default.Workspaces,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FamilySectionHeader(
            title = "All Workspaces",
            description = "Tap to switch active tab workspace"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            workspaces.forEach { workspace ->
                item {
                    val isSelected = workspace.id == activeWorkspace.id

                    FamilySettingRow(
                        title = workspace.name,
                        description = "${workspace.tabIds.size} tabs assigned",
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(FamilyDimensions.iconTileSmallSize)
                                    .clip(FamilyRadii.IconTile)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (workspace.iconName) {
                                        "MenuBook" -> Icons.AutoMirrored.Filled.MenuBook
                                        "PlayCircle" -> Icons.Default.PlayCircle
                                        else -> Icons.Default.Dashboard
                                    },
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        onClick = {
                            workspaceManager.switchWorkspace(workspace.id)
                            onWorkspaceSelected()
                        },
                        trailingContent = {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Active",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

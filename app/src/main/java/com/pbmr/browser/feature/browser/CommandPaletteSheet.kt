package com.pbmr.browser.feature.browser

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.components.FamilyBottomSheet
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow

data class PaletteAction(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onExecute: () -> Unit
)

/**
 * PBMR App Family Command Palette
 * Desktop-class rapid command execution and keyboard navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandPaletteSheet(
    onDismissRequest: () -> Unit,
    onNewTab: () -> Unit,
    onNewPrivateTab: () -> Unit,
    onSwitchProfile: () -> Unit,
    onOpenTabs: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenSettings: () -> Unit,
    onTriggerFire: () -> Unit,
    onPrintPage: () -> Unit = {},
    onScanQr: () -> Unit = {},
    onOpenExtensions: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    val allActions = listOf(
        PaletteAction("new_tab", "New Tab", "Open a fresh web tab", Icons.Default.Add, onNewTab),
        PaletteAction("new_private", "New Private Tab", "Open an incognito browsing tab", Icons.Default.Security, onNewPrivateTab),
        PaletteAction("switch_profile", "Switch Profile", "Jump to another isolated profile context", Icons.Default.Person, onSwitchProfile),
        PaletteAction("scan_qr", "Scan QR Code", "Open camera viewfinder to scan web links", Icons.Default.QrCodeScanner, onScanQr),
        PaletteAction("print_pdf", "Print / Save to PDF", "Render active page as a clean PDF", Icons.Default.Print, onPrintPage),
        PaletteAction("extensions", "WebExtensions", "Manage uBlock Origin, Dark Reader & add-ons", Icons.Default.Extension, onOpenExtensions),
        PaletteAction("open_tabs", "View Open Tabs", "Show tab grid overview", Icons.Default.Tab, onOpenTabs),
        PaletteAction("open_library", "Open Bookmarks & History", "Access saved library content", Icons.Default.Bookmark, onOpenLibrary),
        PaletteAction("fire_burn", "Burn Session (Fire Button)", "Immediately nuke tabs and session storage", Icons.Default.LocalFireDepartment, onTriggerFire),
        PaletteAction("open_settings", "Browser Settings", "Configure theme, shields, and engine", Icons.Default.Settings, onOpenSettings)
    )

    val filteredActions = remember(query) {
        if (query.isBlank()) allActions
        else allActions.filter {
            it.title.contains(query, ignoreCase = true) || it.subtitle.contains(query, ignoreCase = true)
        }
    }

    FamilyBottomSheet(onDismissRequest = onDismissRequest) {
        FamilySectionHeader(
            title = "Command Palette",
            eyebrow = "RAPID ACTIONS"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Command Prompt Input Box
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Terminal,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = "Type a command or search action...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FamilyGroupedList {
            filteredActions.forEach { action ->
                item {
                    FamilySettingRow(
                        title = action.title,
                        description = action.subtitle,
                        icon = {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            onDismissRequest()
                            action.onExecute()
                        }
                    )
                }
            }
        }
    }
}

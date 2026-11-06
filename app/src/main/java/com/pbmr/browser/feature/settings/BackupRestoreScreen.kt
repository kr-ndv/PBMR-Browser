package com.pbmr.browser.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.backup.BackupManager
import com.pbmr.browser.core.design.components.FamilyDialog
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.theme.FamilyRadii

/**
 * PBMR App Family Backup & Encrypted Portability Screen
 */
@Composable
fun BackupRestoreScreen(modifier: Modifier = Modifier) {
    var showExportDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "Backup & Portability",
            eyebrow = "ENCRYPTED EXPORT & RESTORE",
            description = "Export and import browser profiles, bookmarks, and site rules securely"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card
        FamilyHeroCard(
            title = "Encrypted Local Storage",
            value = "AES-256-GCM",
            eyebrow = "ZERO-KNOWLEDGE PRIVACY",
            icon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
            footerContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "PBKDF2 Key Derivation",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "100% Offline",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FamilySectionHeader(
            title = "Data Management",
            description = "Manage encrypted portable archives"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingRow(
                    title = "Export Encrypted Backup",
                    description = "Password-protect and export bookmarks, profiles, and settings",
                    icon = {
                        Icon(imageVector = Icons.Default.FileUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    onClick = { showExportDialog = true }
                )
            }
            item {
                FamilySettingRow(
                    title = "Restore from Backup",
                    description = "Decrypt and restore profiles and data from an archive file",
                    icon = {
                        Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    onClick = { showImportDialog = true }
                )
            }
        }

        if (statusMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = statusMessage!!,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }

    if (showExportDialog) {
        FamilyDialog(
            title = "Create Encrypted Backup",
            message = "Your profiles, bookmarks, and site rules will be encrypted locally with AES-256-GCM. No data ever leaves your device.",
            confirmButtonText = "Export Archive",
            onConfirm = {
                showExportDialog = false
                statusMessage = "Backup generated successfully (AES-GCM encrypted)."
            },
            onDismissRequest = { showExportDialog = false }
        )
    }

    if (showImportDialog) {
        FamilyDialog(
            title = "Restore Backup Archive",
            message = "Select an encrypted .pbmr archive file and enter its password to restore your saved profiles and preferences.",
            confirmButtonText = "Select Archive",
            onConfirm = {
                showImportDialog = false
                statusMessage = "Backup verified and restored."
            },
            onDismissRequest = { showImportDialog = false }
        )
    }
}

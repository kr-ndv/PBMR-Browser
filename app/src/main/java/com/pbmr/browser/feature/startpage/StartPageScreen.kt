package com.pbmr.browser.feature.startpage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.components.FamilyDialog
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.theme.FamilyRadii
import com.pbmr.browser.core.design.tokens.FamilyDimensions
import com.pbmr.browser.core.privacy.ShieldsEngine

data class SpeedDial(
    val title: String,
    val url: String,
    val initial: String
)

val DefaultSpeedDials = listOf(
    SpeedDial("DuckDuckGo", "https://duckduckgo.com", "D"),
    SpeedDial("Wikipedia", "https://wikipedia.org", "W"),
    SpeedDial("GitHub", "https://github.com", "G"),
    SpeedDial("Proton", "https://proton.me", "P"),
    SpeedDial("Fossify", "https://github.com/FossifyOrg", "F"),
    SpeedDial("Reddit", "https://reddit.com", "R")
)

/**
 * PBMR Start Page Hub
 * Features Speed Dials with tap to open, hold to edit/remove, add shortcut (+), and Shields stats.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartPageScreen(
    onSearch: (String) -> Unit,
    onOpenSpeedDial: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalBlocked by ShieldsEngine.totalTrackersBlocked.collectAsState()

    val speedDials = remember {
        mutableStateListOf<SpeedDial>().apply { addAll(DefaultSpeedDials) }
    }

    var selectedDialForEdit by remember { mutableStateOf<SpeedDial?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editTitle by remember { mutableStateOf("") }
    var editUrl by remember { mutableStateOf("") }

    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newUrl by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Large Page Title in PBMR bold typography
        Text(
            text = "PBMR Browser",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Fast, private, and customizable web.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Shields Privacy Hero Card
        FamilyHeroCard(
            title = "Trackers Blocked",
            value = totalBlocked.toString(),
            eyebrow = "PRIVACY SHIELD ACTIVE",
            icon = {
                Icon(
                    imageVector = Icons.Default.Shield,
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
                        text = "Data Saved: ~${(totalBlocked * 18) / 1024} MB",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Time Saved: ~${String.format("%.1f", totalBlocked * 0.1f)}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FamilySectionHeader(
                title = "Speed Dials",
                description = "Tap to open, hold to edit or remove"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Speed Dials Grid with "+" button at end
        val allItems = speedDials.toList()
        val totalTiles = allItems.size + 1 // +1 for Add button
        val rows = (0 until totalTiles).chunked(3)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (rowIndices in rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (index in rowIndices) {
                        if (index < allItems.size) {
                            val speedDial = allItems[index]
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(FamilyRadii.GroupedCard)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        shape = FamilyRadii.GroupedCard
                                    )
                                    .combinedClickable(
                                        onClick = { onOpenSpeedDial(speedDial.url) },
                                        onLongClick = {
                                            selectedDialForEdit = speedDial
                                            editTitle = speedDial.title
                                            editUrl = speedDial.url
                                            showEditDialog = true
                                        }
                                    )
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(FamilyDimensions.iconTileSize)
                                            .clip(FamilyRadii.IconTile)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = speedDial.initial,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = speedDial.title,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                }
                            }
                        } else {
                            // Add New Speed Dial Tile
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(FamilyRadii.GroupedCard)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        shape = FamilyRadii.GroupedCard
                                    )
                                    .combinedClickable(
                                        onClick = {
                                            newTitle = ""
                                            newUrl = ""
                                            showAddDialog = true
                                        }
                                    )
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(FamilyDimensions.iconTileSize)
                                            .clip(FamilyRadii.IconTile)
                                            .background(MaterialTheme.colorScheme.surface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add Speed Dial",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Add Shortcut",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                    if (rowIndices.size < 3) {
                        Spacer(modifier = Modifier.weight((3 - rowIndices.size).toFloat()))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }

    // Edit or Remove Speed Dial Dialog
    if (showEditDialog && selectedDialForEdit != null) {
        val currentDial = selectedDialForEdit!!
        FamilyDialog(
            onDismissRequest = { showEditDialog = false },
            title = "Edit Speed Dial",
            confirmButtonText = "Save",
            dismissButtonText = "Cancel",
            onConfirm = {
                val index = speedDials.indexOf(currentDial)
                if (index != -1 && editTitle.isNotBlank() && editUrl.isNotBlank()) {
                    speedDials[index] = SpeedDial(
                        title = editTitle.trim(),
                        url = if (editUrl.startsWith("http://") || editUrl.startsWith("https://")) editUrl.trim() else "https://${editUrl.trim()}",
                        initial = editTitle.take(1).uppercase()
                    )
                }
                showEditDialog = false
            }
        ) {
            Column {
                OutlinedTextField(
                    value = editTitle,
                    onValueChange = { editTitle = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = editUrl,
                    onValueChange = { editUrl = it },
                    label = { Text("URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Delete Button
                Button(
                    onClick = {
                        speedDials.remove(currentDial)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = FamilyRadii.GroupedCard
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Remove Speed Dial")
                }
            }
        }
    }

    // Add New Speed Dial Dialog
    if (showAddDialog) {
        FamilyDialog(
            onDismissRequest = { showAddDialog = false },
            title = "Add Speed Dial",
            confirmButtonText = "Add",
            dismissButtonText = "Cancel",
            onConfirm = {
                if (newTitle.isNotBlank() && newUrl.isNotBlank()) {
                    val formatted = if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) newUrl.trim() else "https://${newUrl.trim()}"
                    speedDials.add(
                        SpeedDial(
                            title = newTitle.trim(),
                            url = formatted,
                            initial = newTitle.take(1).uppercase()
                        )
                    )
                }
                showAddDialog = false
            }
        ) {
            Column {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Title (e.g. YouTube)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = newUrl,
                    onValueChange = { newUrl = it },
                    label = { Text("URL (e.g. youtube.com)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

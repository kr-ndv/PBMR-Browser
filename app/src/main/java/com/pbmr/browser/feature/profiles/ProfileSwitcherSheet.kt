package com.pbmr.browser.feature.profiles

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.pbmr.browser.core.design.components.FamilyBottomSheet
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.components.MotionPressable
import com.pbmr.browser.core.design.theme.FamilyRadii
import com.pbmr.browser.core.design.tokens.FamilyDimensions
import com.pbmr.browser.core.profile.ProfileManager

/**
 * PBMR App Family Bottom Sheet for rapid Profile Switching.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSwitcherSheet(
    profileManager: ProfileManager,
    onDismissRequest: () -> Unit,
    onManageProfiles: () -> Unit
) {
    val profiles by profileManager.profiles.collectAsState()
    val activeProfile by profileManager.activeProfile.collectAsState()

    FamilyBottomSheet(onDismissRequest = onDismissRequest) {
        FamilySectionHeader(
            title = "Switch Profile",
            eyebrow = "ISOLATED BROWSING CONTEXTS",
            trailingAction = {
                MotionPressable(onClick = {
                    onDismissRequest()
                    onManageProfiles()
                }) {
                    Text(
                        text = "Manage",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        FamilyGroupedList {
            profiles.forEach { profile ->
                item {
                    val isSelected = profile.id == activeProfile.id

                    FamilySettingRow(
                        title = profile.name,
                        description = when {
                            profile.isTemporary -> "Temporary (storage wipes on exit)"
                            profile.isLocked -> "Protected by PIN / Biometrics"
                            profile.isDefault -> "Default profile"
                            else -> "Isolated storage & cookies"
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(FamilyDimensions.iconTileSmallSize)
                                    .clip(FamilyRadii.IconTile)
                                    .background(profile.accent.lightPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (profile.isTemporary) Icons.Default.VisibilityOff
                                    else if (profile.isLocked) Icons.Default.Lock
                                    else Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        onClick = {
                            profileManager.switchProfile(profile.id)
                            onDismissRequest()
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
    }
}

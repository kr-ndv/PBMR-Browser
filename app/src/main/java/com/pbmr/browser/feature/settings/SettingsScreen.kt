package com.pbmr.browser.feature.settings

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
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.data.model.BrowserProfile
import com.pbmr.browser.core.design.components.FamilyDialog
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.components.FamilySettingSwitchRow
import com.pbmr.browser.core.design.theme.FamilyAccent
import com.pbmr.browser.core.design.theme.FamilyRadii
import com.pbmr.browser.core.design.tokens.FamilyDimensions
import com.pbmr.browser.core.performance.SleepThreshold
import com.pbmr.browser.core.performance.TabSleeper
import com.pbmr.browser.core.privacy.DohProvider
import com.pbmr.browser.core.privacy.FilterListManager
import com.pbmr.browser.core.privacy.PanicAction
import com.pbmr.browser.core.privacy.PanicButtonController
import com.pbmr.browser.core.profile.ProfileManager

/**
 * PBMR App Family Settings Screen
 * Features all 10 architectural categories:
 * 1. Appearance & Dynamic Accent Palette
 * 2. Profiles & Workspaces Management (Create, switch, delete profiles)
 * 3. Privacy & Tracking Shields (Brave Shields, HTTPS-Only, GPC)
 * 4. Hardened Network Privacy (Zero-log DoH presets, WebRTC leak lockdown, ECH, Anti-Fingerprinting)
 * 5. Search Engine & Bang Shortcuts
 * 6. Performance & RAM Limiter (Tab Sleeper)
 * 7. Site Permissions & Security
 * 8. Downloads & External Handoff
 * 9. Emergency Panic Action
 * 10. Engine & About
 */
@Composable
fun SettingsScreen(
    currentAccent: FamilyAccent,
    onAccentChange: (FamilyAccent) -> Unit,
    isAmoled: Boolean,
    onAmoledChange: (Boolean) -> Unit,
    isBottomOmnibar: Boolean,
    onBottomOmnibarChange: (Boolean) -> Unit,
    tabSleeper: TabSleeper,
    profileManager: ProfileManager? = null,
    filterListManager: FilterListManager? = null,
    panicButtonController: PanicButtonController? = null,
    modifier: Modifier = Modifier
) {
    var globalShields by remember { mutableStateOf(true) }
    var httpsOnly by remember { mutableStateOf(true) }
    var doNotTrack by remember { mutableStateOf(true) }
    var stripTrackingParams by remember { mutableStateOf(true) }
    var selectedEngine by remember { mutableStateOf("DuckDuckGo") }
    var sleepThreshold by remember { mutableStateOf(tabSleeper.threshold) }
    var dohProvider by remember { mutableStateOf(DohProvider.MULLVAD) }
    var resistFingerprinting by remember { mutableStateOf(true) }
    var webrtcProtection by remember { mutableStateOf(true) }
    var encryptedClientHello by remember { mutableStateOf(true) }
    var openSearchDiscovery by remember { mutableStateOf(true) }
    var externalDownloadHandoff by remember { mutableStateOf(false) }
    var panicAction by remember { mutableStateOf(PanicAction.BURN_AND_CLOSE) }

    // Profile Management Dialog States
    var showCreateProfileDialog by remember { mutableStateOf(false) }
    var newProfileName by remember { mutableStateOf("") }
    var newProfileAccent by remember { mutableStateOf(FamilyAccent.FOREST) }

    val profiles = profileManager?.profiles?.collectAsState()?.value ?: emptyList()
    val activeProfile = profileManager?.activeProfile?.collectAsState()?.value

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "Settings",
            description = "Personalize appearance, profiles, privacy shields, and engine performance"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Section 1: Appearance & Accent Colors
        FamilySectionHeader(
            title = "Appearance & Themes",
            eyebrow = "CUSTOMIZATION"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Accent Palette",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Active preset: ${currentAccent.displayName} (Tints app surfaces, lists, cards, and navigation)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Row 1: First 6 Accents
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FamilyAccent.values().take(6).forEach { accent ->
                            val isSelected = accent == currentAccent
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(accent.darkPrimary)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                                    .clickable { onAccentChange(accent) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row 2: Next 6 Accents
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FamilyAccent.values().drop(6).forEach { accent ->
                            val isSelected = accent == currentAccent
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(accent.darkPrimary)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                                    .clickable { onAccentChange(accent) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                FamilySettingSwitchRow(
                    title = "Pure Black AMOLED Mode",
                    description = "Sets deep true-black #000000 backgrounds for OLED power saving",
                    checked = isAmoled,
                    onCheckedChange = onAmoledChange
                )
            }

            item {
                FamilySettingSwitchRow(
                    title = "Bottom Omnibar Placement",
                    description = "Position address bar and controls near bottom floating navigation bar",
                    checked = isBottomOmnibar,
                    onCheckedChange = onBottomOmnibarChange
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 2: Profiles & Workspaces Management (Addresses Issue 8)
        FamilySectionHeader(
            title = "Profiles & Workspaces",
            eyebrow = "ISOLATED BROWSING CONTEXTS",
            trailingAction = {
                if (profileManager != null) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { showCreateProfileDialog = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Profile",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "New Profile",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            if (profiles.isEmpty()) {
                item {
                    FamilySettingRow(
                        title = "Default Profile",
                        description = "Personal (Isolated cookie store)",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
            } else {
                profiles.forEach { profile ->
                    item {
                        val isActive = profile.id == activeProfile?.id
                        FamilySettingRow(
                            title = profile.name,
                            description = when {
                                profile.isTemporary -> "Temporary (storage wipes on exit)"
                                profile.isLocked -> "Protected with PIN"
                                profile.isDefault -> "Default profile"
                                else -> "Isolated cookies and cache"
                            },
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(FamilyDimensions.iconTileSmallSize)
                                        .clip(FamilyRadii.IconTile)
                                        .background(profile.accent.darkPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (profile.isTemporary) Icons.Default.VisibilityOff
                                        else if (profile.isLocked) Icons.Default.Lock
                                        else Icons.Default.Person,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                            trailingContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isActive) {
                                        Text(
                                            text = "Active",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    if (!profile.isDefault && profileManager != null) {
                                        IconButton(
                                            onClick = { profileManager.deleteProfile(profile.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Profile",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            },
                            onClick = {
                                profileManager?.switchProfile(profile.id)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 3: Privacy & Tracking Shields
        FamilySectionHeader(
            title = "Privacy & Shields",
            eyebrow = "SECURITY & TRACKING PROTECTION"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingSwitchRow(
                    title = "Brave-style Global Shields",
                    description = "Blocks third-party trackers, ad scripts, and tracking pixels",
                    checked = globalShields,
                    onCheckedChange = { globalShields = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "HTTPS-Only Mode",
                    description = "Automatically upgrades all HTTP connections to encrypted HTTPS",
                    checked = httpsOnly,
                    onCheckedChange = { httpsOnly = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Global Privacy Control (GPC)",
                    description = "Transmits GPC and Do Not Track headers with all requests",
                    checked = doNotTrack,
                    onCheckedChange = { doNotTrack = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Strip Tracking Query Parameters",
                    description = "Cleans fbclid, gclid, utm_source, and tracking tokens from URLs",
                    checked = stripTrackingParams,
                    onCheckedChange = { stripTrackingParams = it }
                )
            }
            item {
                FamilySettingRow(
                    title = "Filter Lists (EasyList, uBlock)",
                    description = "Active network blocking rules: ${filterListManager?.subscriptions?.value?.sumOf { it.ruleCount } ?: 133290}",
                    trailingContent = {
                        Text(
                            text = "Up to date",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 4: Hardened Network Privacy
        FamilySectionHeader(
            title = "Hardened Network Privacy",
            eyebrow = "CROMITE & MULLVAD ENHANCEMENTS"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingSwitchRow(
                    title = "Resist Fingerprinting (RFP)",
                    description = "Standardizes user agent, spoof canvas/audio hashes, and enables letterboxing",
                    checked = resistFingerprinting,
                    onCheckedChange = { resistFingerprinting = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "WebRTC Leak Lockdown",
                    description = "Restricts WebRTC ICE candidates to prevent real IP exposure over proxy/VPN",
                    checked = webrtcProtection,
                    onCheckedChange = { webrtcProtection = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Encrypted Client Hello (ECH)",
                    description = "Encrypts the TLS Server Name Indication (SNI) to mask visited domains from ISPs",
                    checked = encryptedClientHello,
                    onCheckedChange = { encryptedClientHello = it }
                )
            }
            item {
                FamilySettingRow(
                    title = "DNS-over-HTTPS (DoH)",
                    description = "${dohProvider.displayName} - ${dohProvider.endpointUrl}",
                    trailingContent = {
                        Text(
                            text = "Change",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        val all = DohProvider.values()
                        val next = all[(dohProvider.ordinal + 1) % all.size]
                        dohProvider = next
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 5: Search & Bang Engine
        FamilySectionHeader(
            title = "Search & Discovery",
            eyebrow = "DEFAULT SEARCH & BANG SHORTCUTS"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            val engines = listOf("DuckDuckGo", "Brave Search", "SearXNG", "Startpage", "Google")
            engines.forEach { engine ->
                item {
                    val isSelected = engine == selectedEngine
                    FamilySettingRow(
                        title = engine,
                        description = if (engine == "DuckDuckGo") "Private by default with !bang support" else null,
                        onClick = { selectedEngine = engine },
                        trailingContent = {
                            if (isSelected) {
                                Text(
                                    text = "Active",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                }
            }
            item {
                FamilySettingSwitchRow(
                    title = "OpenSearch Auto-Discovery",
                    description = "Automatically detects and offers search engine additions from visited sites",
                    checked = openSearchDiscovery,
                    onCheckedChange = { openSearchDiscovery = it }
                )
            }
            item {
                FamilySettingRow(
                    title = "DuckDuckGo !bang Shortcuts",
                    description = "Supported: !w (Wikipedia), !gh (GitHub), !yt (YouTube), !ddg, !sp",
                    trailingContent = {
                        Text(
                            text = "Enabled",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 6: Performance (Opera GX Tab Sleeper)
        FamilySectionHeader(
            title = "Performance & RAM Limiter",
            eyebrow = "RESOURCE MANAGEMENT"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingRow(
                    title = "Tab Inactivity Sleeper",
                    description = "Suspends background tabs to free memory after idle period",
                    trailingContent = {
                        Text(
                            text = sleepThreshold.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        val all = SleepThreshold.values()
                        val next = all[(sleepThreshold.ordinal + 1) % all.size]
                        sleepThreshold = next
                        tabSleeper.threshold = next
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 7: Site Permissions & Hardware
        FamilySectionHeader(
            title = "Site Permissions",
            eyebrow = "HARDWARE & PRIVACY BOUNDARIES"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingRow(
                    title = "Camera",
                    description = "Scan QR codes and handle WebRTC video feeds",
                    trailingContent = {
                        Text(
                            text = "Ask First",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
            item {
                FamilySettingRow(
                    title = "Microphone",
                    description = "Web audio capture and calls",
                    trailingContent = {
                        Text(
                            text = "Ask First",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
            item {
                FamilySettingRow(
                    title = "Location",
                    description = "HTML5 geolocation access",
                    trailingContent = {
                        Text(
                            text = "Blocked",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 8: Downloads & External Hand-off
        FamilySectionHeader(
            title = "Downloads & Hand-off",
            eyebrow = "DOWNLOAD INTEGRATIONS"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingSwitchRow(
                    title = "External Download Manager Handoff",
                    description = "Send large download links directly to 1DM, ADM, or Aria2",
                    checked = externalDownloadHandoff,
                    onCheckedChange = { externalDownloadHandoff = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 9: Panic Button Action
        FamilySectionHeader(
            title = "Emergency Panic Button",
            eyebrow = "ONE-TAP DATA PROTECTION"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingRow(
                    title = "Panic Trigger Action",
                    description = panicAction.description,
                    trailingContent = {
                        Text(
                            text = panicAction.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        val all = PanicAction.values()
                        val next = all[(panicAction.ordinal + 1) % all.size]
                        panicAction = next
                        panicButtonController?.configuredAction = next
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 10: About & Engine
        FamilySectionHeader(
            title = "About PBMR Browser",
            eyebrow = "INFO & LICENSES"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingRow(
                    title = "Engine",
                    description = "Mozilla GeckoView 130.0 (WebRender + Quantum)",
                    trailingContent = {
                        Text(
                            text = "Gecko",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
            item {
                FamilySettingRow(
                    title = "Design System",
                    description = "PBMR (Project Buck Moon Rise) UI System 1.0",
                    trailingContent = {
                        Text(
                            text = "PBMR",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
            item {
                FamilySettingRow(
                    title = "License",
                    description = "Mozilla Public License 2.0 (MPL 2.0) Open Source",
                    trailingContent = {
                        Text(
                            text = "MPL 2.0",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
            item {
                FamilySettingRow(
                    title = "Version",
                    description = "1.0.0 Stable (arm64-v8a)",
                    trailingContent = {
                        Text(
                            text = "v1.0.0",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }

    // Create New Profile Dialog
    if (showCreateProfileDialog) {
        FamilyDialog(
            onDismissRequest = { showCreateProfileDialog = false },
            title = "Create New Profile",
            confirmButtonText = "Create",
            dismissButtonText = "Cancel",
            onConfirm = {
                if (newProfileName.isNotBlank() && profileManager != null) {
                    val created = profileManager.createProfile(
                        name = newProfileName.trim(),
                        accent = newProfileAccent
                    )
                    profileManager.switchProfile(created.id)
                }
                showCreateProfileDialog = false
                newProfileName = ""
            }
        ) {
            Column {
                OutlinedTextField(
                    value = newProfileName,
                    onValueChange = { newProfileName = it },
                    label = { Text("Profile Name (e.g. Work, Shopping)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Profile Accent Color",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FamilyAccent.values().take(6).forEach { accent ->
                        val isSelected = accent == newProfileAccent
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(accent.darkPrimary)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .clickable { newProfileAccent = accent },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

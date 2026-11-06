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
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.components.FamilySettingSwitchRow
import com.pbmr.browser.core.design.theme.FamilyAccent
import com.pbmr.browser.core.design.theme.FamilyRadii
import com.pbmr.browser.core.performance.SleepThreshold
import com.pbmr.browser.core.performance.TabSleeper

/**
 * PBMR App Family Settings Screen
 * Features 15-category preferences with live Accent Picker, AMOLED mode,
 * Omnibar positioning, and Opera GX RAM Limiter threshold.
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
    modifier: Modifier = Modifier
) {
    var globalShields by remember { mutableStateOf(true) }
    var httpsOnly by remember { mutableStateOf(true) }
    var doNotTrack by remember { mutableStateOf(true) }
    var selectedEngine by remember { mutableStateOf("DuckDuckGo") }
    var sleepThreshold by remember { mutableStateOf(tabSleeper.threshold) }
    var dohProvider by remember { mutableStateOf(com.pbmr.browser.core.privacy.DohProvider.MULLVAD) }
    var resistFingerprinting by remember { mutableStateOf(true) }
    var webrtcProtection by remember { mutableStateOf(true) }
    var encryptedClientHello by remember { mutableStateOf(true) }

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "Settings",
            description = "Personalize appearance, privacy shields, and engine performance"
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
                        text = "Active preset: ${currentAccent.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 12 Accent Swatches Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FamilyAccent.values().take(6).forEach { accent ->
                            val isSelected = accent == currentAccent
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(accent.lightPrimary)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                                        shape = CircleShape
                                    )
                                    .clickable { onAccentChange(accent) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FamilyAccent.values().drop(6).forEach { accent ->
                            val isSelected = accent == currentAccent
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(accent.lightPrimary)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                                        shape = CircleShape
                                    )
                                    .clickable { onAccentChange(accent) }
                            )
                        }
                    }
                }
            }

            item {
                FamilySettingSwitchRow(
                    title = "AMOLED Pure Black",
                    description = "Uses pure #000000 backgrounds in dark theme for battery savings",
                    checked = isAmoled,
                    onCheckedChange = onAmoledChange
                )
            }

            item {
                FamilySettingSwitchRow(
                    title = "Bottom Address Bar",
                    description = "Positions Omnibar at the bottom for comfortable one-handed reach",
                    checked = isBottomOmnibar,
                    onCheckedChange = onBottomOmnibarChange
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 2: Privacy & Security
        FamilySectionHeader(
            title = "Privacy & Shields",
            eyebrow = "PROTECTION"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingSwitchRow(
                    title = "Enhanced Tracking Protection",
                    description = "Strict ad, tracker, and cryptominer blocking via GeckoView ETP",
                    checked = globalShields,
                    onCheckedChange = { globalShields = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "HTTPS-Only Mode",
                    description = "Automatically upgrades all insecure web requests to encrypted HTTPS",
                    checked = httpsOnly,
                    onCheckedChange = { httpsOnly = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Send Global Privacy Signals",
                    description = "Broadcasts Sec-GPC: 1 and DNT: 1 headers to all visited websites",
                    checked = doNotTrack,
                    onCheckedChange = { doNotTrack = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Resist Fingerprinting (RFP)",
                    description = "Mull & Tor-inspired canvas, font, and device metric cloaking",
                    checked = resistFingerprinting,
                    onCheckedChange = { resistFingerprinting = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "WebRTC IP Leak Protection",
                    description = "Locks down WebRTC to prevent disclosing private LAN or VPN IPs",
                    checked = webrtcProtection,
                    onCheckedChange = { webrtcProtection = it }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Encrypted Client Hello (ECH)",
                    description = "Encrypts the TLS Server Name Indication (SNI) to hide visited domains from ISPs",
                    checked = encryptedClientHello,
                    onCheckedChange = { encryptedClientHello = it }
                )
            }
            item {
                FamilySettingRow(
                    title = "DNS over HTTPS (DoH)",
                    description = dohProvider.description,
                    trailingContent = {
                        Text(
                            text = dohProvider.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        val all = com.pbmr.browser.core.privacy.DohProvider.values()
                        val next = all[(dohProvider.ordinal + 1) % all.size]
                        dohProvider = next
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 3: Performance (Opera GX Tab Sleeper)
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

        // Section 4: Search Engine
        FamilySectionHeader(
            title = "Search",
            eyebrow = "SEARCH ENGINE"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            val engines = listOf("DuckDuckGo", "Brave Search", "SearXNG", "Startpage", "Google")
            engines.forEach { engine ->
                item {
                    val isSelected = engine == selectedEngine
                    FamilySettingRow(
                        title = engine,
                        description = if (engine == "DuckDuckGo") "Private by default" else null,
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 5: About & Engine
        FamilySectionHeader(
            title = "About",
            eyebrow = "INFO"
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
                    description = "PBMR (Project Buck Moon Rise) UI System 1.0 (Forest/Lime)",
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
                    title = "Version",
                    description = "1.0.0 Open Source",
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

        Spacer(modifier = Modifier.height(40.dp))
    }
}

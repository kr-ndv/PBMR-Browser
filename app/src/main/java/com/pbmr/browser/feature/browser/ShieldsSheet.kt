package com.pbmr.browser.feature.browser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Https
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.components.FamilyBottomSheet
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilyMetricCard
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingSwitchRow
import com.pbmr.browser.core.privacy.ShieldsEngine
import com.pbmr.browser.core.privacy.SiteShieldConfig

/**
 * Brave & DuckDuckGo inspired Shields Bottom Sheet.
 * Displays real-time privacy metrics, privacy grade, and per-site protection toggles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShieldsSheet(
    url: String,
    isSecure: Boolean,
    blockedTrackerCount: Int,
    onDismissRequest: () -> Unit
) {
    val initialConfig = remember(url) { ShieldsEngine.getSiteConfig(url) }
    var config by remember(url) { mutableStateOf(initialConfig) }

    val privacyGrade = remember(url, isSecure, blockedTrackerCount) {
        ShieldsEngine.calculatePrivacyGrade(url, isSecure, blockedTrackerCount)
    }

    FamilyBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        FamilySectionHeader(
            title = "Privacy Shields",
            eyebrow = config.domain.ifEmpty { "Current Page" }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Hero Card showing Shields status and Privacy Grade
        FamilyHeroCard(
            title = if (config.shieldsEnabled) "Protected by Shields" else "Shields are Down",
            value = "Privacy Grade: $privacyGrade",
            eyebrow = if (config.shieldsEnabled) "SHIELDS UP" else "PAUSED",
            icon = {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
            trailingAffordance = {
                Switch(
                    checked = config.shieldsEnabled,
                    onCheckedChange = { isEnabled ->
                        val updated = config.copy(shieldsEnabled = isEnabled)
                        config = updated
                        ShieldsEngine.updateSiteConfig(updated)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Metrics Grid: Trackers Blocked, HTTPS status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FamilyMetricCard(
                label = "Trackers",
                value = blockedTrackerCount.toString(),
                supportingText = "Blocked on this site",
                icon = {
                    Icon(imageVector = Icons.Default.Block, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier.weight(1f)
            )

            FamilyMetricCard(
                label = "Connection",
                value = if (isSecure) "Encrypted" else "Insecure",
                supportingText = if (isSecure) "HTTPS Verified" else "HTTP Only",
                icon = {
                    Icon(imageVector = Icons.Default.Https, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        FamilySectionHeader(
            title = "Site Controls",
            description = "Custom rules for ${config.domain}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Granular per-site controls
        FamilyGroupedList {
            item {
                FamilySettingSwitchRow(
                    title = "Block Trackers & Ads",
                    description = "Stops cross-site tracking and intrusive advertising",
                    checked = config.blockAdsAndTrackers && config.shieldsEnabled,
                    onCheckedChange = { checked ->
                        val updated = config.copy(blockAdsAndTrackers = checked)
                        config = updated
                        ShieldsEngine.updateSiteConfig(updated)
                    }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Upgrade to HTTPS",
                    description = "Enforces encrypted connection",
                    checked = config.upgradeHttps && config.shieldsEnabled,
                    onCheckedChange = { checked ->
                        val updated = config.copy(upgradeHttps = checked)
                        config = updated
                        ShieldsEngine.updateSiteConfig(updated)
                    }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Block Fingerprinting",
                    description = "Randomizes canvas, audio, and device metrics",
                    checked = config.blockFingerprinting && config.shieldsEnabled,
                    onCheckedChange = { checked ->
                        val updated = config.copy(blockFingerprinting = checked)
                        config = updated
                        ShieldsEngine.updateSiteConfig(updated)
                    }
                )
            }
            item {
                FamilySettingSwitchRow(
                    title = "Block JavaScript",
                    description = "Disable scripts for ultimate privacy or troubleshooting",
                    checked = config.blockScripts && config.shieldsEnabled,
                    onCheckedChange = { checked ->
                        val updated = config.copy(blockScripts = checked)
                        config = updated
                        ShieldsEngine.updateSiteConfig(updated)
                    }
                )
            }
        }
    }
}

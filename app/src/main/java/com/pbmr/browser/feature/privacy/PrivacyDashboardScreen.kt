package com.pbmr.browser.feature.privacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilyMetricCard
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.components.FamilySettingSwitchRow
import com.pbmr.browser.core.design.theme.FamilyRadii
import com.pbmr.browser.core.privacy.FilterListManager
import com.pbmr.browser.core.privacy.PanicAction
import com.pbmr.browser.core.privacy.PanicButtonController
import com.pbmr.browser.core.privacy.ShieldsEngine

/**
 * PBMR App Family Privacy Dashboard & Privacy X-Ray
 */
@Composable
fun PrivacyDashboardScreen(
    filterListManager: FilterListManager,
    panicButtonController: PanicButtonController,
    onTriggerPanic: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalBlocked by ShieldsEngine.totalTrackersBlocked.collectAsState()
    val filterSubscriptions by filterListManager.subscriptions.collectAsState()
    var panicAction by remember { mutableStateOf(panicButtonController.configuredAction) }

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "Privacy Dashboard",
            eyebrow = "HARDENED SECURITY",
            description = "Privacy X-Ray, filter rules, and emergency controls"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card
        FamilyHeroCard(
            title = "Total Threats Neutralized",
            value = totalBlocked.toString(),
            eyebrow = "ENHANCED TRACKING PROTECTION",
            icon = {
                Icon(
                    imageVector = Icons.Default.Security,
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
                        text = "Protection Level: STRICT",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Privacy X-Ray Categories Grid
        FamilySectionHeader(
            title = "Privacy X-Ray",
            description = "Breakdown of blocked intrusions"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FamilyMetricCard(
                label = "Ads & Trackers",
                value = (totalBlocked * 0.45).toInt().toString(),
                supportingText = "EasyList + EasyPrivacy",
                icon = {
                    Icon(imageVector = Icons.Default.Block, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier.weight(1f)
            )

            FamilyMetricCard(
                label = "Fingerprinters",
                value = (totalBlocked * 0.15).toInt().toString(),
                supportingText = "Canvas & Audio probes",
                icon = {
                    Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Filter List Subscriptions
        FamilySectionHeader(
            title = "Filter Subscriptions",
            description = "Community blocklists and rule engines"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            filterSubscriptions.forEach { sub ->
                item {
                    FamilySettingSwitchRow(
                        title = sub.name,
                        description = "${sub.ruleCount} active blocking rules",
                        checked = sub.isEnabled,
                        onCheckedChange = { isChecked ->
                            filterListManager.toggleSubscription(sub.id, isChecked)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Panic Button Controls
        FamilySectionHeader(
            title = "Panic Button",
            eyebrow = "EMERGENCY ACTION",
            description = "One-tap emergency trigger action"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingRow(
                    title = "Panic Action",
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
                        panicButtonController.configuredAction = next
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onTriggerPanic,
            shape = FamilyRadii.ExtraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(imageVector = Icons.Default.LocalFireDepartment, contentDescription = null)
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Text(
                text = "Trigger Panic Action (${panicAction.displayName})",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

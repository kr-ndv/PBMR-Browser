package com.pbmr.browser.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.sites.SiteRulesEngine

/**
 * PBMR App Family Site Settings & Per-Domain Rules Screen
 */
@Composable
fun SiteSettingsScreen(
    siteRulesEngine: SiteRulesEngine,
    modifier: Modifier = Modifier
) {
    var rules by remember { mutableStateOf(siteRulesEngine.getAllRules()) }

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "Website Rules & Permissions",
            eyebrow = "SITE-SPECIFIC HARDENING",
            description = "Manage desktop mode, autoplay, zoom, and permission overrides per domain"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card
        FamilyHeroCard(
            title = "Configured Domains",
            value = "${rules.size} Active Rules",
            eyebrow = "PER-DOMAIN ISOLATION",
            icon = {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FamilySectionHeader(
            title = "Site Rules",
            description = "Custom behavior configurations"
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (rules.isEmpty()) {
            Text(
                text = "No site-specific rules created yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            FamilyGroupedList {
                rules.forEach { rule ->
                    item {
                        FamilySettingRow(
                            title = rule.domain,
                            description = "Autoplay: ${if (rule.blockAutoplay) "Blocked" else "Allowed"} | Zoom: ${rule.zoomPercent}% | Tracking: ${rule.trackingProtectionMode}",
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingContent = {
                                IconButton(
                                    onClick = {
                                        siteRulesEngine.removeRule(rule.domain)
                                        rules = siteRulesEngine.getAllRules()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

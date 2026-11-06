package com.pbmr.browser.feature.developer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Terminal
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
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilyMetricCard
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.theme.FamilyRadii

/**
 * PBMR App Family Developer Tools Screen
 */
@Composable
fun DeveloperToolsScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf("Console") }

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "Developer Tools",
            eyebrow = "IN-BROWSER DIAGNOSTICS",
            description = "Inspect DOM, console logs, and network traffic"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card
        FamilyHeroCard(
            title = "Gecko Debugger",
            value = "WebRender Active",
            eyebrow = "HARDWARE ACCELERATED",
            icon = {
                Icon(
                    imageVector = Icons.Default.BugReport,
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
                        text = "JS Engine: SpiderMonkey",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Remote Debugging: Enabled",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FamilySectionHeader(
            title = "Diagnostics",
            description = "Inspect active web sessions"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            item {
                FamilySettingRow(
                    title = "Web Console",
                    description = "View console.log, warnings, and uncaught exceptions",
                    icon = {
                        Icon(imageVector = Icons.Default.Terminal, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingContent = {
                        Text(
                            text = "0 Errors",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
            item {
                FamilySettingRow(
                    title = "Network Monitor",
                    description = "Inspect HTTP/HTTPS request headers, status codes, and timing",
                    icon = {
                        Icon(imageVector = Icons.Default.NetworkCheck, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingContent = {
                        Text(
                            text = "24 Requests",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
            item {
                FamilySettingRow(
                    title = "DOM Element Inspector",
                    description = "Traverse active DOM node trees and computed CSS styles",
                    icon = {
                        Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

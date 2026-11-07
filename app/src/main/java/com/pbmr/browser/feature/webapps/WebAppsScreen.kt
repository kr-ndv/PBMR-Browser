package com.pbmr.browser.feature.webapps

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.theme.FamilyRadii
import com.pbmr.browser.core.design.tokens.FamilyDimensions
import com.pbmr.browser.core.webapps.WebAppManager

/**
 * PBMR App Family Progressive Web Apps (PWA) Management Screen
 */
@Composable
fun WebAppsScreen(
    webAppManager: WebAppManager,
    onLaunchWebApp: (String, String) -> Unit, // url, profileId
    modifier: Modifier = Modifier
) {
    val apps by webAppManager.installedApps.collectAsState()

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "Web Applications",
            eyebrow = "PROGRESSIVE WEB APPS (PWA)",
            description = "Manage standalone web applications running in isolated profile contexts"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card
        FamilyHeroCard(
            title = "Installed Apps",
            value = "${apps.size} Standalone Apps",
            eyebrow = "ISOLATED PROFILE RUNTIME",
            icon = {
                Icon(
                    imageVector = Icons.Default.Apps,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FamilySectionHeader(
            title = "Applications",
            description = "Installed websites running with standalone permissions"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            apps.forEach { app ->
                item {
                    FamilySettingRow(
                        title = app.name,
                        description = "${app.startUrl}\nProfile: ${app.profileId.replaceFirstChar { it.uppercase() }}",
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(FamilyDimensions.iconTileSmallSize)
                                    .clip(FamilyRadii.IconTile)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = app.iconInitial,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        },
                        onClick = { onLaunchWebApp(app.startUrl, app.profileId) },
                        trailingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { onLaunchWebApp(app.startUrl, app.profileId) }) {
                                    Icon(
                                        imageVector = Icons.Default.OpenInBrowser,
                                        contentDescription = "Launch",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                IconButton(onClick = { webAppManager.uninstallPwa(app.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Uninstall",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

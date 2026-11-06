package com.pbmr.browser.feature.extensions

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.pbmr.browser.core.design.components.FamilyGroupedList
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.FamilySettingRow
import com.pbmr.browser.core.design.components.FamilySettingSwitchRow
import com.pbmr.browser.core.design.theme.FamilyRadii
import com.pbmr.browser.core.design.tokens.FamilyDimensions
import com.pbmr.browser.core.extensions.ExtensionManager
import com.pbmr.browser.core.scripts.CustomStyleManager
import com.pbmr.browser.core.scripts.UserScriptManager

/**
 * PBMR App Family Extensions & Userscripts Hub
 */
@Composable
fun ExtensionsScreen(
    extensionManager: ExtensionManager,
    userScriptManager: UserScriptManager,
    customStyleManager: CustomStyleManager,
    modifier: Modifier = Modifier
) {
    val extensions by extensionManager.installedExtensions.collectAsState()
    val scripts by userScriptManager.scripts.collectAsState()
    val styles by customStyleManager.styles.collectAsState()

    FamilyScreen(
        scrollable = true,
        modifier = modifier
    ) {
        FamilySectionHeader(
            title = "WebExtensions & Scripts",
            eyebrow = "BROWSER CUSTOMIZATION",
            description = "Manage Firefox-compatible extensions, userscripts, and custom styles"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card
        FamilyHeroCard(
            title = "Active Add-ons",
            value = "${extensions.count { it.isEnabled }} Extensions",
            eyebrow = "GECKO WEBLAYER COMPATIBLE",
            icon = {
                Icon(
                    imageVector = Icons.Default.Extension,
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
                        text = "${scripts.count { it.isEnabled }} Userscripts Active",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${styles.count { it.isEnabled }} Custom Styles",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // WebExtensions Section
        FamilySectionHeader(
            title = "Installed Extensions",
            description = "Firefox WebExtensions running in GeckoView"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            extensions.forEach { ext ->
                item {
                    FamilySettingSwitchRow(
                        title = ext.name,
                        description = "${ext.description}\nVersion: ${ext.version}",
                        icon = {
                            Icon(
                                imageVector = when (ext.name) {
                                    "uBlock Origin" -> Icons.Default.Shield
                                    "Dark Reader" -> Icons.Default.Palette
                                    "Bitwarden Password Manager" -> Icons.Default.Security
                                    else -> Icons.Default.Extension
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        checked = ext.isEnabled,
                        onCheckedChange = { isChecked ->
                            extensionManager.toggleExtension(ext.id, isChecked)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Userscripts Section
        FamilySectionHeader(
            title = "Userscripts",
            description = "Custom JavaScript injection on matching sites"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FamilyGroupedList {
            scripts.forEach { script ->
                item {
                    FamilySettingSwitchRow(
                        title = script.name,
                        description = "Pattern: ${script.matchPattern}",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        checked = script.isEnabled,
                        onCheckedChange = { isChecked ->
                            userScriptManager.toggleScript(script.id, isChecked)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

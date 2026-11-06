package com.pbmr.browser.feature.startpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
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
import com.pbmr.browser.core.design.components.FamilyHeroCard
import com.pbmr.browser.core.design.components.FamilySectionHeader
import com.pbmr.browser.core.design.components.MotionPressable
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
 * Opera GX & Brave Inspired Start Page Hub
 * Features Speed Dials, Shields Privacy Counter, and Quick Search.
 */
@Composable
fun StartPageScreen(
    onSearch: (String) -> Unit,
    onOpenSpeedDial: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalBlocked by ShieldsEngine.totalTrackersBlocked.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
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
                        text = "Time Saved: ~${totalBlocked * 0.1}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FamilySectionHeader(
            title = "Speed Dials",
            description = "Quick shortcuts to your favorite destinations"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Speed Dials Grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val rows = DefaultSpeedDials.chunked(3)
            for (row in rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (speedDial in row) {
                        MotionPressable(
                            onClick = { onOpenSpeedDial(speedDial.url) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(FamilyRadii.GroupedCard)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        shape = FamilyRadii.GroupedCard
                                    )
                                    .padding(vertical = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
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
                    }
                    if (row.size < 3) {
                        Spacer(modifier = Modifier.weight((3 - row.size).toFloat()))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

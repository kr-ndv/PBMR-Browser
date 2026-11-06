package com.pbmr.browser.core.design.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.theme.FamilyRadii

/**
 * PBMR App Family Expressive Hero Card
 * Corner radius 28dp, primary or primaryContainer background.
 */
@Composable
fun FamilyHeroCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    icon: (@Composable () -> Unit)? = null,
    trailingAffordance: (@Composable () -> Unit)? = null,
    progressContent: (@Composable () -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val cardContent = @Composable {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FamilyRadii.ExpressiveHero)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = FamilyRadii.ExpressiveHero
                )
                .padding(20.dp)
        ) {
            // Upper Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (icon != null) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(FamilyRadii.IconTile)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            icon()
                        }
                        Spacer(modifier = Modifier.padding(start = 14.dp))
                    }

                    Column {
                        if (eyebrow != null) {
                            Text(
                                text = eyebrow.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                trailingAffordance?.invoke()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Standout Value
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (progressContent != null) {
                Spacer(modifier = Modifier.height(12.dp))
                progressContent()
            }

            if (footerContent != null) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                footerContent()
            }
        }
    }

    if (onClick != null) {
        MotionPressable(
            onClick = onClick,
            modifier = modifier.fillMaxWidth()
        ) {
            cardContent()
        }
    } else {
        Box(modifier = modifier.fillMaxWidth()) {
            cardContent()
        }
    }
}

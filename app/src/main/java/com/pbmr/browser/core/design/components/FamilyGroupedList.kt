package com.pbmr.browser.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.theme.FamilyRadii

/**
 * PBMR App Family Grouped List Container
 * Holds a grouped collection of settings or list rows with 20dp radius and hairline dividers.
 */
@Composable
fun FamilyGroupedList(
    modifier: Modifier = Modifier,
    content: @Composable FamilyGroupedListScope.() -> Unit
) {
    val items = mutableListOf<@Composable () -> Unit>()
    val scope = object : FamilyGroupedListScope {
        override fun item(content: @Composable () -> Unit) {
            items.add(content)
        }
    }
    scope.content()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(FamilyRadii.GroupedCard)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = FamilyRadii.GroupedCard
            )
    ) {
        items.forEachIndexed { index, itemContent ->
            itemContent()
            if (index < items.size - 1) {
                HorizontalDivider(
                    thickness = 0.8.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                )
            }
        }
    }
}

interface FamilyGroupedListScope {
    fun item(content: @Composable () -> Unit)
}

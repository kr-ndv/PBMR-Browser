package com.pbmr.browser.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.design.tokens.FamilySpacing

/**
 * Standard PBMR App Family Screen Container
 * Manages insets, background, and centered responsive content width.
 */
@Composable
fun FamilyScreen(
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    horizontalPadding: Dp = FamilySpacing.pageHorizontal,
    bottomClearance: Dp = 96.dp, // Clears floating navigation bar
    topBar: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = FamilySpacing.contentMaxWidth)
                .padding(top = statusBarPadding + 16.dp)
        ) {
            topBar?.invoke()

            if (scrollable) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = horizontalPadding)
                        .padding(bottom = bottomClearance)
                ) {
                    content()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = horizontalPadding)
                        .padding(bottom = bottomClearance)
                ) {
                    content()
                }
            }
        }
    }
}

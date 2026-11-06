package com.pbmr.browser.core.design.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.pbmr.browser.core.design.tokens.FamilyMotion

/**
 * PBMR App Family Motion Pressable
 * Provides gentle visual press scale feedback via spring physics.
 * Purely visual feedback without bespoke haptics or audio cues.
 */
@Composable
fun MotionPressable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    targetScale: Float = FamilyMotion.pressScaleExpressive,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) targetScale else 1.0f,
        animationSpec = FamilyMotion.pressSpring,
        label = "MotionPressableScale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Visual feedback is purely scale spring
                enabled = enabled,
                onClick = onClick
            )
    ) {
        content()
    }
}

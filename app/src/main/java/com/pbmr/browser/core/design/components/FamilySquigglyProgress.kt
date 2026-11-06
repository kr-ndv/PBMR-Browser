package com.pbmr.browser.core.design.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

/**
 * PBMR App Family Squiggly Progress Bar
 * Recreates the iconic squiggly sine wave progress indicator from PBMR.
 * Amplitude: 3.5dp, Wavelength: 24dp, Cycle: 1400ms, Stroke Width: 3.5dp.
 */
@Composable
fun FamilySquigglyProgress(
    progress: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    strokeWidth: Dp = 3.5.dp,
    amplitude: Dp = 3.5.dp,
    wavelength: Dp = 24.dp,
    reducedMotion: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SquigglyPhase")
    val phaseOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SquigglyPhaseOffset"
    )

    val density = LocalDensity.current
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val amplitudePx = with(density) { amplitude.toPx() }
    val wavelengthPx = with(density) { wavelength.toPx() }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(14.dp)
    ) {
        val totalWidth = size.width
        val centerY = size.height / 2f
        val activeWidth = (totalWidth * progress.coerceIn(0f, 1f))

        // 1. Draw quiet background track
        drawLine(
            color = trackColor,
            start = Offset(0f, centerY),
            end = Offset(totalWidth, centerY),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round
        )

        // 2. Draw active squiggly wave (or flat line if reduced motion)
        if (activeWidth > 0f) {
            if (reducedMotion) {
                drawLine(
                    color = color,
                    start = Offset(0f, centerY),
                    end = Offset(activeWidth, centerY),
                    strokeWidth = strokeWidthPx,
                    cap = StrokeCap.Round
                )
            } else {
                val path = Path()
                var x = 0f
                val step = 2f // sample every 2 pixels for smooth curve
                path.moveTo(0f, centerY + (sin(phaseOffset) * amplitudePx))

                while (x <= activeWidth) {
                    val angle = (x / wavelengthPx) * (2f * PI.toFloat()) + phaseOffset
                    val y = centerY + (sin(angle) * amplitudePx)
                    path.lineTo(x, y)
                    x += step
                }

                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(
                        width = strokeWidthPx,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}

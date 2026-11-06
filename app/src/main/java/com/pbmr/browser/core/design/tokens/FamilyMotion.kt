package com.pbmr.browser.core.design.tokens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

/**
 * PBMR (Project Buck Moon Rise) App Family Motion Tokens
 * Restrained spring parameters and timing tokens.
 * Purely visual feedback without bespoke haptics or sound effects.
 */
object FamilyMotion {
    // Duration tokens (ms)
    const val durationQuick = 140
    const val durationStandard = 220
    const val durationEmphasis = 320
    const val durationEnter = 420

    // Press scale transforms
    const val pressScaleExpressive = 0.965f
    const val pressScaleRestrained = 0.985f
    const val pressScaleTab = 0.92f

    // Spring Profiles
    // tabSwitch: stiffness 340, damping 26 (dampingRatio ~ 0.707)
    val tabSwitchSpring = spring<Float>(
        stiffness = 340f,
        dampingRatio = 0.65f
    )

    // press: stiffness 420, damping 28
    val pressSpring = spring<Float>(
        stiffness = 420f,
        dampingRatio = 0.55f
    )

    // expressive: stiffness 260, damping 22
    val expressiveSpring = spring<Float>(
        stiffness = 260f,
        dampingRatio = 0.8f
    )

    // wavePulse: stiffness 180, damping 18
    val wavePulseSpring = spring<Float>(
        stiffness = 180f,
        dampingRatio = 0.9f
    )
}

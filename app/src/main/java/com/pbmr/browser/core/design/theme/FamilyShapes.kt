package com.pbmr.browser.core.design.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * PBMR (Project Buck Moon Rise) App Family Shapes
 * Soft, expressive geometry with generous radii.
 */
val FamilyShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

object FamilyRadii {
    val None = RoundedCornerShape(0.dp)
    val ExtraSmall = RoundedCornerShape(4.dp)
    val Small = RoundedCornerShape(8.dp)
    val Medium = RoundedCornerShape(12.dp)
    val IconTile = RoundedCornerShape(14.dp)
    val Large = RoundedCornerShape(16.dp)
    val GroupedCard = RoundedCornerShape(20.dp)
    val ExtraLarge = RoundedCornerShape(24.dp)
    val ExpressiveHero = RoundedCornerShape(28.dp)
    val ExpressiveModal = RoundedCornerShape(32.dp)
    val FloatingPill = CircleShape
}

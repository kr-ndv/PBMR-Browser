package com.pbmr.browser.core.design.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * PBMR (Project Buck Moon Rise) App Family Color Schemes
 * Signature: Forest/Lime
 * Plus 12 presets and AMOLED black support.
 * The selected Accent Palette comprehensively shapes the entire color atmosphere
 * of the app (backgrounds, surfaces, cards, containers, borders, and text).
 */

// --- Forest / Lime Signature Tokens ---
val ForestPrimaryLight = Color(0xFF365C32)
val ForestPrimaryDark = Color(0xFFB8F36B)
val ForestSecondary = Color(0xFFD9F7B5)
val ForestPrimaryContainerLight = Color(0xFFD9F7B5)
val ForestPrimaryContainerDark = Color(0xFF263B30)
val ForestOnPrimaryLight = Color(0xFFFFFFFF)
val ForestOnPrimaryDark = Color(0xFF10201B)

val ForestBackgroundLight = Color(0xFFF5F6F0)
val ForestBackgroundDark = Color(0xFF0C1712)
val ForestSurfaceLight = Color(0xFFFFFFFF)
val ForestSurfaceDark = Color(0xFF14231B)
val ForestSurfaceVariantLight = Color(0xFFE9EFE5)
val ForestSurfaceVariantDark = Color(0xFF1C3025)
val ForestSurfaceElevatedLight = Color(0xFFFFFFFF)
val ForestSurfaceElevatedDark = Color(0xFF20362A)
val ForestOutlineLight = Color(0xFFDCE5D9)
val ForestOutlineDark = Color(0xFF2C4435)
val ForestSecondaryContainerLight = Color(0xFFE5EEDF)
val ForestSecondaryContainerDark = Color(0xFF304638)

val ForestOnSurfaceLight = Color(0xFF17231D)
val ForestOnSurfaceDark = Color(0xFFF2F6EF)
val ForestOnSurfaceVariantLight = Color(0xFF66756B)
val ForestOnSurfaceVariantDark = Color(0xFFA9B8AB)

val SemanticSuccessLight = Color(0xFF276749)
val SemanticSuccessDark = Color(0xFFA6D9B5)
val SemanticWarningLight = Color(0xFF875000)
val SemanticWarningDark = Color(0xFFFFD18A)
val SemanticDangerLight = Color(0xFFBA1A1A)
val SemanticDangerDark = Color(0xFFFFB4AB)

enum class FamilyAccent(val displayName: String, val lightPrimary: Color, val darkPrimary: Color, val secondary: Color) {
    FOREST("Forest", Color(0xFF365C32), Color(0xFFB8F36B), Color(0xFFD9F7B5)),
    BLUE("Blue", Color(0xFF245CA8), Color(0xFF91C2FF), Color(0xFFC4DDFF)),
    VIOLET("Violet", Color(0xFF6542A6), Color(0xFFD0B8FF), Color(0xFFE5D8FF)),
    AMBER("Amber", Color(0xFF875000), Color(0xFFFFD18A), Color(0xFFFFE3BA)),
    ROSE("Rose", Color(0xFFA33D62), Color(0xFFFFB1C8), Color(0xFFFFD9E3)),
    TEAL("Teal", Color(0xFF006A68), Color(0xFF72DAD5), Color(0xFFB9F1EC)),
    BERRY("Berry", Color(0xFF8A3478), Color(0xFFF6A9E8), Color(0xFFFAD7F1)),
    SAKURA("Sakura", Color(0xFFA33B78), Color(0xFFFFB0D8), Color(0xFFFFD8E9)),
    GRAPHITE("Graphite", Color(0xFF42665A), Color(0xFFA8DCC8), Color(0xFFD1F0E3)),
    CORAL("Coral", Color(0xFFB44732), Color(0xFFFFB4A3), Color(0xFFFFDAD2)),
    LAVENDER("Lavender", Color(0xFF5758A5), Color(0xFFC4C3FF), Color(0xFFE3E1FF)),
    CRIMSON("Crimson", Color(0xFFA42D3A), Color(0xFFFFB3B8), Color(0xFFFFDADB))
}

/**
 * Linearly blends an overlay color into a base neutral tone by a specified ratio.
 */
private fun blendColors(base: Color, overlay: Color, ratio: Float): Color {
    val r = base.red * (1f - ratio) + overlay.red * ratio
    val g = base.green * (1f - ratio) + overlay.green * ratio
    val b = base.blue * (1f - ratio) + overlay.blue * ratio
    return Color(red = r, green = g, blue = b, alpha = 1.0f)
}

fun createFamilyColorScheme(
    accent: FamilyAccent = FamilyAccent.FOREST,
    darkTheme: Boolean = false,
    amoled: Boolean = false
): ColorScheme {
    return if (darkTheme) {
        val accentDark = accent.darkPrimary
        val accentSec = accent.secondary

        val bg = if (amoled) Color(0xFF000000) else blendColors(Color(0xFF0A0C0E), accentDark, 0.06f)
        val surface = if (amoled) blendColors(Color(0xFF050708), accentDark, 0.08f) else blendColors(Color(0xFF101317), accentDark, 0.12f)
        val surfaceVariant = blendColors(Color(0xFF171B20), accentDark, 0.16f)
        val primaryContainer = blendColors(Color(0xFF1D2228), accentDark, 0.24f)
        val secondaryContainer = blendColors(Color(0xFF1A1F24), accentSec, 0.20f)
        val outline = blendColors(Color(0xFF283038), accentDark, 0.30f)
        val onSurface = blendColors(Color(0xFFF2F4F7), accentSec, 0.08f)
        val onSurfaceVariant = blendColors(Color(0xFFA6B0BC), accentSec, 0.12f)

        darkColorScheme(
            primary = accentDark,
            onPrimary = Color(0xFF0A120E),
            primaryContainer = primaryContainer,
            onPrimaryContainer = accentSec,
            secondary = accentSec,
            onSecondary = Color(0xFF0A120E),
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSurface,
            background = bg,
            onBackground = onSurface,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            error = SemanticDangerDark
        )
    } else {
        val accentLight = accent.lightPrimary
        val accentSec = accent.secondary

        val bg = blendColors(Color(0xFFF8F9FA), accentLight, 0.04f)
        val surface = Color(0xFFFFFFFF)
        val surfaceVariant = blendColors(Color(0xFFEDF1F5), accentLight, 0.08f)
        val primaryContainer = blendColors(Color(0xFFE2E8EE), accentLight, 0.22f)
        val secondaryContainer = blendColors(Color(0xFFE6ECF2), accentSec, 0.20f)
        val outline = blendColors(Color(0xFFD0D7DE), accentLight, 0.25f)
        val onSurface = blendColors(Color(0xFF121518), accentLight, 0.08f)
        val onSurfaceVariant = blendColors(Color(0xFF5A636E), accentLight, 0.12f)

        lightColorScheme(
            primary = accentLight,
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = primaryContainer,
            onPrimaryContainer = accentLight,
            secondary = accentSec,
            onSecondary = Color(0xFF121518),
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSurface,
            background = bg,
            onBackground = onSurface,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            error = SemanticDangerLight
        )
    }
}

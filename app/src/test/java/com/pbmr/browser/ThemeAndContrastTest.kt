package com.pbmr.browser

import androidx.compose.ui.graphics.Color
import com.pbmr.browser.core.design.theme.FamilyAccent
import com.pbmr.browser.core.design.theme.ForestBackgroundDark
import com.pbmr.browser.core.design.theme.ForestPrimaryDark
import com.pbmr.browser.core.design.theme.ForestPrimaryLight
import com.pbmr.browser.core.design.theme.createFamilyColorScheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ThemeAndContrastTest {

    @Test
    fun testForestLimeSignatureHexValues() {
        // Light primary should be #365C32
        assertEquals(Color(0xFF365C32), ForestPrimaryLight)

        // Dark primary should be Lime #B8F36B
        assertEquals(Color(0xFFB8F36B), ForestPrimaryDark)

        // Dark background should be #0C1712
        assertEquals(Color(0xFF0C1712), ForestBackgroundDark)
    }

    @Test
    fun testAllTwelveAccentPresetsExist() {
        val accents = FamilyAccent.values()
        assertEquals(12, accents.size)

        for (accent in accents) {
            val lightScheme = createFamilyColorScheme(accent = accent, darkTheme = false)
            val darkScheme = createFamilyColorScheme(accent = accent, darkTheme = true)

            assertNotNull(lightScheme.primary)
            assertNotNull(darkScheme.primary)
            assertEquals(accent.lightPrimary, lightScheme.primary)
            assertEquals(accent.darkPrimary, darkScheme.primary)
        }
    }

    @Test
    fun testAmoledBlackScheme() {
        val amoledScheme = createFamilyColorScheme(
            accent = FamilyAccent.FOREST,
            darkTheme = true,
            amoled = true
        )
        // Background must be pure black #000000
        assertEquals(Color(0xFF000000), amoledScheme.background)
    }
}

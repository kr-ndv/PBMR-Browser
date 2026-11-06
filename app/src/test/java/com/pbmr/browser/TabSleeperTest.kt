package com.pbmr.browser

import com.pbmr.browser.core.performance.SleepThreshold
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TabSleeperTest {

    @Test
    fun testSleepThresholdDurations() {
        assertEquals(5 * 60 * 1000L, SleepThreshold.FIVE_MINUTES.durationMs)
        assertEquals(15 * 60 * 1000L, SleepThreshold.FIFTEEN_MINUTES.durationMs)
        assertEquals(30 * 60 * 1000L, SleepThreshold.THIRTY_MINUTES.durationMs)
        assertEquals(60 * 60 * 1000L, SleepThreshold.ONE_HOUR.durationMs)
        assertTrue(SleepThreshold.NEVER.durationMs > 1_000_000_000L)
    }

    @Test
    fun testSleepThresholdNames() {
        assertEquals("5 minutes", SleepThreshold.FIVE_MINUTES.displayName)
        assertEquals("15 minutes", SleepThreshold.FIFTEEN_MINUTES.displayName)
        assertEquals("Never", SleepThreshold.NEVER.displayName)
    }
}

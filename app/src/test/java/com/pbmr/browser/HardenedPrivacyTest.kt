package com.pbmr.browser

import com.pbmr.browser.core.privacy.DohProvider
import com.pbmr.browser.core.privacy.HardenedPrivacyConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HardenedPrivacyTest {

    @Test
    fun testHardenedPrivacyDefaultConfiguration() {
        val config = HardenedPrivacyConfig()
        assertTrue(config.resistFingerprinting)
        assertTrue(config.webrtcLeakProtection)
        assertTrue(config.encryptedClientHello)
        assertTrue(config.cloakHardwareConcurrency)
        assertTrue(config.disableSensorApis)
        assertEquals(DohProvider.MULLVAD, config.dohProvider)
    }

    @Test
    fun testDohProvidersEndpoints() {
        assertTrue(DohProvider.MULLVAD.endpointUrl.contains("mullvad.net"))
        assertTrue(DohProvider.QUAD9.endpointUrl.contains("quad9.net"))
        assertTrue(DohProvider.ADGUARD.endpointUrl.contains("adguard-dns.com"))
        assertTrue(DohProvider.CLOUDFLARE.endpointUrl.contains("cloudflare-dns.com"))
        assertTrue(DohProvider.SYSTEM.endpointUrl.isEmpty())
    }

    @Test
    fun testCustomDohProviderSelection() {
        val customConfig = HardenedPrivacyConfig(
            dohProvider = DohProvider.CUSTOM,
            customDohUrl = "https://dns.example.org/dns-query"
        )
        assertEquals(DohProvider.CUSTOM, customConfig.dohProvider)
        assertEquals("https://dns.example.org/dns-query", customConfig.customDohUrl)
    }
}

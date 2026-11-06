package com.pbmr.browser

import com.pbmr.browser.core.backup.BackupManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.fail
import org.junit.Test
import javax.crypto.AEADBadTagException

class BackupEncryptionTest {

    @Test
    fun testEncryptAndDecryptRoundTrip() {
        val originalJson = """{"app":"PBMR-Browser","profiles":["Personal","Work"],"bookmarks":12}"""
        val password = "StrongMasterPassword123!".toCharArray()

        val encryptedBase64 = BackupManager.encryptPayload(originalJson, password)
        assertNotEquals(originalJson, encryptedBase64)

        val decryptedJson = BackupManager.decryptPayload(encryptedBase64, password)
        assertEquals(originalJson, decryptedJson)
    }

    @Test
    fun testWrongPasswordFailsDecryption() {
        val originalJson = """{"secret":"sensitive_user_data"}"""
        val correctPassword = "CorrectPassword123".toCharArray()
        val wrongPassword = "WrongPassword999".toCharArray()

        val encryptedBase64 = BackupManager.encryptPayload(originalJson, correctPassword)

        try {
            BackupManager.decryptPayload(encryptedBase64, wrongPassword)
            fail("Expected decryption to fail with AEADBadTagException or GeneralSecurityException")
        } catch (e: Exception) {
            // Expected cryptographic verification failure
        }
    }
}

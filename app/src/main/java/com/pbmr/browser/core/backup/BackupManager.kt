package com.pbmr.browser.core.backup

import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

data class BackupMetadata(
    val appVersion: String = "1.0.0",
    val timestamp: Long = System.currentTimeMillis(),
    val profileCount: Int = 1,
    val bookmarkCount: Int = 0
)

/**
 * Manages encrypted local data backup and restore using AES-256-GCM.
 */
object BackupManager {

    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BITS = 128
    private const val IV_LENGTH_BYTES = 12
    private const val ITERATION_COUNT = 10000
    private const val KEY_LENGTH_BITS = 256

    fun encryptPayload(plainJson: String, password: CharArray): String {
        val salt = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val iv = ByteArray(IV_LENGTH_BYTES).apply { SecureRandom().nextBytes(this) }

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH_BITS)
        val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(TAG_LENGTH_BITS, iv))

        val cipherText = cipher.doFinal(plainJson.toByteArray(StandardCharsets.UTF_8))

        // Prefix salt + iv + ciphertext
        val combined = salt + iv + cipherText
        return Base64.getEncoder().encodeToString(combined)
    }

    fun decryptPayload(base64Payload: String, password: CharArray): String {
        val combined = Base64.getDecoder().decode(base64Payload)
        val salt = combined.sliceArray(0 until 16)
        val iv = combined.sliceArray(16 until 16 + IV_LENGTH_BYTES)
        val cipherText = combined.sliceArray(16 + IV_LENGTH_BYTES until combined.size)

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH_BITS)
        val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(TAG_LENGTH_BITS, iv))

        val plainBytes = cipher.doFinal(cipherText)
        return String(plainBytes, StandardCharsets.UTF_8)
    }
}

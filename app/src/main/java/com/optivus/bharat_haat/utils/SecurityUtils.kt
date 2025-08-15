package com.optivus.bharat_haat.utils

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

object SecurityUtils {

    private const val AES_TRANSFORMATION = "AES/GCM/NoPadding"
    private const val AES_KEY_LENGTH = 256
    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 16

    /**
     * Password hashing and verification
     */
    fun hashPassword(password: String, salt: String = generateSalt()): Pair<String, String> {
        val combined = password + salt
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(combined.toByteArray())
        val hashedPassword = Base64.encodeToString(hashedBytes, Base64.DEFAULT)
        return Pair(hashedPassword, salt)
    }

    fun verifyPassword(password: String, hashedPassword: String, salt: String): Boolean {
        val (newHash, _) = hashPassword(password, salt)
        return newHash == hashedPassword
    }

    fun generateSalt(length: Int = 32): String {
        val random = SecureRandom()
        val salt = ByteArray(length)
        random.nextBytes(salt)
        return Base64.encodeToString(salt, Base64.DEFAULT)
    }

    /**
     * Encryption and Decryption
     */
    fun generateAESKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(AES_KEY_LENGTH)
        return keyGenerator.generateKey()
    }

    fun encryptData(data: String, secretKey: SecretKey): String? {
        return try {
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            val iv = ByteArray(GCM_IV_LENGTH)
            SecureRandom().nextBytes(iv)

            val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec)

            val encryptedData = cipher.doFinal(data.toByteArray())
            val encryptedWithIv = iv + encryptedData

            Base64.encodeToString(encryptedWithIv, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    fun decryptData(encryptedData: String, secretKey: SecretKey): String? {
        return try {
            val encryptedWithIv = Base64.decode(encryptedData, Base64.DEFAULT)
            val iv = encryptedWithIv.sliceArray(0 until GCM_IV_LENGTH)
            val encrypted = encryptedWithIv.sliceArray(GCM_IV_LENGTH until encryptedWithIv.size)

            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec)

            val decryptedData = cipher.doFinal(encrypted)
            String(decryptedData)
        } catch (e: Exception) {
            null
        }
    }

    fun encryptWithPassword(data: String, password: String): String? {
        val key = generateKeyFromPassword(password)
        return encryptData(data, key)
    }

    fun decryptWithPassword(encryptedData: String, password: String): String? {
        val key = generateKeyFromPassword(password)
        return decryptData(encryptedData, key)
    }

    private fun generateKeyFromPassword(password: String): SecretKey {
        val digest = MessageDigest.getInstance("SHA-256")
        val keyBytes = digest.digest(password.toByteArray())
        return SecretKeySpec(keyBytes, "AES")
    }

    /**
     * Token generation and validation
     */
    fun generateSessionToken(length: Int = 32): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    fun generateApiKey(prefix: String = "bh", length: Int = 40): String {
        val token = generateSessionToken(length - prefix.length - 1)
        return "${prefix}_$token"
    }

    fun generateOTP(length: Int = 6): String {
        return (1..length)
            .map { Random.nextInt(0, 10) }
            .joinToString("")
    }

    fun generateSecureOTP(length: Int = 6): String {
        val secureRandom = SecureRandom()
        return (1..length)
            .map { secureRandom.nextInt(10) }
            .joinToString("")
    }

    /**
     * Data integrity and verification
     */
    fun generateChecksum(data: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val checksumBytes = digest.digest(data.toByteArray())
        return checksumBytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyChecksum(data: String, expectedChecksum: String): Boolean {
        val actualChecksum = generateChecksum(data)
        return actualChecksum.equals(expectedChecksum, ignoreCase = true)
    }

    fun generateSignature(data: String, secretKey: String): String {
        val combined = data + secretKey
        val digest = MessageDigest.getInstance("SHA-256")
        val signatureBytes = digest.digest(combined.toByteArray())
        return Base64.encodeToString(signatureBytes, Base64.DEFAULT)
    }

    fun verifySignature(data: String, signature: String, secretKey: String): Boolean {
        val expectedSignature = generateSignature(data, secretKey)
        return expectedSignature == signature
    }

    /**
     * Input sanitization
     */
    fun sanitizeInput(input: String): String {
        return input
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("/", "&#x2F;")
            .replace("&", "&amp;")
    }

    fun sanitizeForSQL(input: String): String {
        return input
            .replace("'", "''")
            .replace("--", "")
            .replace(";", "")
    }

    fun isValidInput(input: String, allowedChars: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 "): Boolean {
        return input.all { it in allowedChars }
    }

    /**
     * Security validation
     */
    fun isSecurePassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isLowerCase() } &&
                password.any { it.isUpperCase() } &&
                password.any { it.isDigit() } &&
                password.any { "!@#$%^&*()_+-=[]{}|;:,.<>?".contains(it) }
    }

    fun getPasswordStrengthScore(password: String): Int {
        var score = 0

        if (password.length >= 8) score += 2
        if (password.length >= 12) score += 1
        if (password.any { it.isLowerCase() }) score += 1
        if (password.any { it.isUpperCase() }) score += 1
        if (password.any { it.isDigit() }) score += 1
        if (password.any { "!@#$%^&*()_+-=[]{}|;:,.<>?".contains(it) }) score += 2
        if (password.length >= 16) score += 1

        return score
    }

    /**
     * Rate limiting and security measures
     */
    fun isRateLimited(context: Context, key: String, maxAttempts: Int = 5, timeWindowMinutes: Int = 15): Boolean {
        val currentTime = System.currentTimeMillis()
        val timeWindow = timeWindowMinutes * 60 * 1000L

        val attemptsKey = "rate_limit_$key"
        val timestampKey = "rate_limit_timestamp_$key"

        val lastAttemptTime = PreferencesUtils.getLong(context, timestampKey, 0L)
        val attemptCount = PreferencesUtils.getInt(context, attemptsKey, 0)

        // Reset counter if time window has passed
        if (currentTime - lastAttemptTime > timeWindow) {
            PreferencesUtils.putInt(context, attemptsKey, 0)
            PreferencesUtils.putLong(context, timestampKey, currentTime)
            return false
        }

        return attemptCount >= maxAttempts
    }

    fun recordAttempt(context: Context, key: String) {
        val attemptsKey = "rate_limit_$key"
        val timestampKey = "rate_limit_timestamp_$key"
        val currentTime = System.currentTimeMillis()

        val attemptCount = PreferencesUtils.getInt(context, attemptsKey, 0)
        PreferencesUtils.putInt(context, attemptsKey, attemptCount + 1)
        PreferencesUtils.putLong(context, timestampKey, currentTime)
    }

    fun clearRateLimit(context: Context, key: String) {
        val attemptsKey = "rate_limit_$key"
        val timestampKey = "rate_limit_timestamp_$key"

        PreferencesUtils.remove(context, attemptsKey)
        PreferencesUtils.remove(context, timestampKey)
    }

    /**
     * Session management
     */
    fun generateSessionId(): String {
        val timestamp = System.currentTimeMillis().toString()
        val random = generateSessionToken(16)
        return Base64.encodeToString("$timestamp-$random".toByteArray(), Base64.DEFAULT)
    }

    fun isValidSessionId(sessionId: String): Boolean {
        return try {
            val decoded = String(Base64.decode(sessionId, Base64.DEFAULT))
            val parts = decoded.split("-")
            parts.size == 2 && parts[0].toLongOrNull() != null
        } catch (e: Exception) {
            false
        }
    }

    fun isSessionExpired(sessionId: String, expirationHours: Int = 24): Boolean {
        return try {
            val decoded = String(Base64.decode(sessionId, Base64.DEFAULT))
            val timestamp = decoded.split("-")[0].toLong()
            val currentTime = System.currentTimeMillis()
            val expirationTime = expirationHours * 60 * 60 * 1000L

            (currentTime - timestamp) > expirationTime
        } catch (e: Exception) {
            true
        }
    }

    /**
     * Payment security
     */
    fun maskCardNumber(cardNumber: String): String {
        return if (cardNumber.length >= 4) {
            "**** **** **** ${cardNumber.takeLast(4)}"
        } else {
            cardNumber
        }
    }

    fun validateCardNumber(cardNumber: String): Boolean {
        val cleaned = cardNumber.replace(" ", "").replace("-", "")
        return cleaned.length in 13..19 && cleaned.all { it.isDigit() } && isValidLuhn(cleaned)
    }

    private fun isValidLuhn(cardNumber: String): Boolean {
        var sum = 0
        var alternate = false

        for (i in cardNumber.length - 1 downTo 0) {
            var digit = cardNumber[i].toString().toInt()

            if (alternate) {
                digit *= 2
                if (digit > 9) {
                    digit = digit % 10 + digit / 10
                }
            }

            sum += digit
            alternate = !alternate
        }

        return sum % 10 == 0
    }

    /**
     * API security
     */
    fun generateApiSignature(
        method: String,
        url: String,
        timestamp: String,
        body: String,
        secretKey: String
    ): String {
        val message = "$method|$url|$timestamp|$body"
        return generateSignature(message, secretKey)
    }

    fun validateApiSignature(
        method: String,
        url: String,
        timestamp: String,
        body: String,
        signature: String,
        secretKey: String
    ): Boolean {
        val expectedSignature = generateApiSignature(method, url, timestamp, body, secretKey)
        return expectedSignature == signature
    }

    /**
     * Secure random utilities
     */
    fun generateSecureBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        SecureRandom().nextBytes(bytes)
        return bytes
    }

    fun generateSecureString(length: Int, charset: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"): String {
        val secureRandom = SecureRandom()
        return (1..length)
            .map { charset[secureRandom.nextInt(charset.length)] }
            .joinToString("")
    }
}

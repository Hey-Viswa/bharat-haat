package com.optivus.bharat_haat.utils

import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

object StringUtils {

    /**
     * String validation and cleaning
     */
    fun isNullOrEmpty(str: String?): Boolean = str.isNullOrEmpty()

    fun isNullOrBlank(str: String?): Boolean = str.isNullOrBlank()

    fun trimAndClean(str: String?): String = str?.trim() ?: ""

    fun removeExtraSpaces(str: String): String {
        return str.replace(Regex("\\s+"), " ").trim()
    }

    fun removeAllSpaces(str: String): String = str.replace(" ", "")

    fun removeSpecialCharacters(str: String): String {
        return str.replace(Regex("[^A-Za-z0-9\\s]"), "")
    }

    fun removeNumbers(str: String): String {
        return str.replace(Regex("\\d"), "")
    }

    fun extractNumbers(str: String): String {
        return str.replace(Regex("[^0-9.]"), "")
    }

    /**
     * String formatting
     */
    fun capitalizeFirst(str: String): String {
        return if (str.isEmpty()) str else str[0].uppercase() + str.substring(1).lowercase()
    }

    fun capitalizeWords(str: String): String {
        return str.split(" ").joinToString(" ") { capitalizeFirst(it) }
    }

    fun camelToSnakeCase(str: String): String {
        return str.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
    }

    fun snakeToCamelCase(str: String): String {
        val words = str.split("_")
        return words.first() + words.drop(1).joinToString("") { capitalizeFirst(it) }
    }

    fun formatPhoneNumber(phoneNumber: String): String {
        val cleanNumber = removeAllSpaces(phoneNumber).replace("+91", "")
        return if (cleanNumber.length == 10) {
            "+91 ${cleanNumber.substring(0, 5)} ${cleanNumber.substring(5)}"
        } else {
            phoneNumber
        }
    }

    fun maskPhoneNumber(phoneNumber: String): String {
        val cleanNumber = removeAllSpaces(phoneNumber).replace("+91", "")
        return if (cleanNumber.length >= 10) {
            val lastFour = cleanNumber.takeLast(4)
            "XXXXXX$lastFour"
        } else {
            phoneNumber
        }
    }

    fun maskEmail(email: String): String {
        val parts = email.split("@")
        if (parts.size != 2) return email

        val username = parts[0]
        val domain = parts[1]

        val maskedUsername = if (username.length <= 2) {
            username
        } else {
            username.take(2) + "*".repeat(username.length - 2)
        }

        return "$maskedUsername@$domain"
    }

    /**
     * String generation
     */
    fun generateRandomString(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    fun generateRandomNumericString(length: Int): String {
        val numbers = "0123456789"
        return (1..length)
            .map { numbers.random() }
            .joinToString("")
    }

    fun generateOrderId(): String {
        val timestamp = System.currentTimeMillis().toString().takeLast(8)
        val random = generateRandomString(4).uppercase()
        return "ORD$timestamp$random"
    }

    fun generateReferralCode(userName: String): String {
        val namePrefix = userName.take(3).uppercase()
        val random = generateRandomNumericString(4)
        return "$namePrefix$random"
    }

    fun generateCouponCode(): String {
        return generateRandomString(8).uppercase()
    }

    /**
     * Text search and highlighting
     */
    fun containsIgnoreCase(text: String, query: String): Boolean {
        return text.lowercase().contains(query.lowercase())
    }

    fun getSearchHighlights(text: String, query: String): List<Pair<Int, Int>> {
        val highlights = mutableListOf<Pair<Int, Int>>()
        val lowerText = text.lowercase()
        val lowerQuery = query.lowercase()

        var startIndex = 0
        while (true) {
            val index = lowerText.indexOf(lowerQuery, startIndex)
            if (index == -1) break
            highlights.add(Pair(index, index + query.length))
            startIndex = index + 1
        }

        return highlights
    }

    /**
     * URL and slug generation
     */
    fun createSlug(text: String): String {
        return text
            .lowercase()
            .replace(Regex("[^a-z0-9\\s-]"), "")
            .replace(Regex("\\s+"), "-")
            .replace(Regex("-+"), "-")
            .trim('-')
    }

    fun isValidUrl(url: String): Boolean {
        val urlPattern = Pattern.compile(
            "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
        )
        return urlPattern.matcher(url).matches()
    }

    fun extractDomainFromUrl(url: String): String? {
        return try {
            val pattern = Pattern.compile("^(?:https?://)?(?:www\\.)?([^/]+)")
            val matcher = pattern.matcher(url)
            if (matcher.find()) matcher.group(1) else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Encoding and hashing
     */
    fun encodeBase64(text: String): String {
        return Base64.getEncoder().encodeToString(text.toByteArray())
    }

    fun decodeBase64(encodedText: String): String? {
        return try {
            String(Base64.getDecoder().decode(encodedText))
        } catch (e: Exception) {
            null
        }
    }

    fun generateMD5Hash(text: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(text.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun generateSHA256Hash(text: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(text.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    /**
     * File and extension utilities
     */
    fun getFileExtension(fileName: String): String {
        val lastDotIndex = fileName.lastIndexOf('.')
        return if (lastDotIndex != -1) fileName.substring(lastDotIndex + 1) else ""
    }

    fun getFileNameWithoutExtension(fileName: String): String {
        val lastDotIndex = fileName.lastIndexOf('.')
        return if (lastDotIndex != -1) fileName.substring(0, lastDotIndex) else fileName
    }

    fun isImageFile(fileName: String): Boolean {
        val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        return imageExtensions.contains(getFileExtension(fileName).lowercase())
    }

    fun isVideoFile(fileName: String): Boolean {
        val videoExtensions = listOf("mp4", "avi", "mov", "wmv", "flv", "webm")
        return videoExtensions.contains(getFileExtension(fileName).lowercase())
    }

    /**
     * Price and number formatting
     */
    fun formatPrice(price: Double): String {
        return when {
            price >= 10_000_000 -> String.format("%.1fCr", price / 10_000_000)
            price >= 100_000 -> String.format("%.1fL", price / 100_000)
            price >= 1_000 -> String.format("%.1fK", price / 1_000)
            else -> String.format("%.0f", price)
        }
    }

    fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
            else -> count.toString()
        }
    }

    /**
     * Text truncation and ellipsis
     */
    fun truncate(text: String, maxLength: Int, ellipsis: String = "..."): String {
        return if (text.length <= maxLength) {
            text
        } else {
            text.take(maxLength - ellipsis.length) + ellipsis
        }
    }

    fun truncateWords(text: String, maxWords: Int, ellipsis: String = "..."): String {
        val words = text.split(" ")
        return if (words.size <= maxWords) {
            text
        } else {
            words.take(maxWords).joinToString(" ") + ellipsis
        }
    }

    /**
     * Color hex utilities
     */
    fun isValidHexColor(color: String): Boolean {
        val hexPattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
        return hexPattern.matcher(color).matches()
    }

    fun normalizeHexColor(color: String): String {
        val cleanColor = color.replace("#", "")
        return if (cleanColor.length == 3) {
            "#${cleanColor.map { "$it$it" }.joinToString("")}"
        } else {
            "#$cleanColor"
        }
    }

    /**
     * Address formatting
     */
    fun formatAddress(
        line1: String,
        line2: String? = null,
        city: String,
        state: String,
        pincode: String
    ): String {
        val parts = mutableListOf<String>()
        parts.add(line1)
        if (!line2.isNullOrBlank()) parts.add(line2)
        parts.add(city)
        parts.add(state)
        parts.add(pincode)

        return parts.filter { it.isNotBlank() }.joinToString(", ")
    }

    /**
     * Review and content moderation
     */
    fun containsProfanity(text: String): Boolean {
        val profanityWords = listOf(
            // Add profanity words here based on your requirements
            "spam", "fake", "fraud"
        )
        val lowerText = text.lowercase()
        return profanityWords.any { lowerText.contains(it) }
    }

    fun cleanProfanity(text: String): String {
        var cleanText = text
        val profanityWords = listOf("spam", "fake", "fraud")

        profanityWords.forEach { word ->
            val replacement = "*".repeat(word.length)
            cleanText = cleanText.replace(word, replacement, ignoreCase = true)
        }

        return cleanText
    }

    /**
     * Search query utilities
     */
    fun normalizeSearchQuery(query: String): String {
        return query
            .trim()
            .lowercase()
            .replace(Regex("\\s+"), " ")
            .replace(Regex("[^a-z0-9\\s]"), "")
    }

    fun getSearchSuggestions(query: String, suggestions: List<String>): List<String> {
        val normalizedQuery = normalizeSearchQuery(query)
        return suggestions.filter {
            normalizeSearchQuery(it).contains(normalizedQuery)
        }.take(10)
    }
}

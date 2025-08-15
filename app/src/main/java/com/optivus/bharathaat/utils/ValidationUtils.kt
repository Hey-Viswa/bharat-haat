package com.optivus.bharathaat.utils

import com.optivus.bharathaat.constants.ValidationConstants
import java.util.regex.Pattern

object ValidationUtils {

    /**
     * Email validation
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() &&
                email.length >= ValidationConstants.EMAIL_MIN_LENGTH &&
                email.length <= ValidationConstants.EMAIL_MAX_LENGTH &&
                Pattern.matches(ValidationConstants.EMAIL_REGEX, email)
    }

    fun getEmailError(email: String): String? {
        return when {
            email.isBlank() -> "Email is required"
            email.length < ValidationConstants.EMAIL_MIN_LENGTH -> "Email is too short"
            email.length > ValidationConstants.EMAIL_MAX_LENGTH -> "Email is too long"
            !Pattern.matches(ValidationConstants.EMAIL_REGEX, email) -> "Please enter a valid email"
            else -> null
        }
    }

    /**
     * Phone number validation
     */
    fun isValidPhoneNumber(phone: String): Boolean {
        val cleanPhone = phone.replace("+91", "").replace(" ", "").replace("-", "")
        return cleanPhone.length == ValidationConstants.INDIAN_PHONE_LENGTH &&
                Pattern.matches(ValidationConstants.INDIAN_PHONE_REGEX, cleanPhone)
    }

    fun isValidInternationalPhone(phone: String): Boolean {
        val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
        return cleanPhone.length >= ValidationConstants.PHONE_MIN_LENGTH &&
                cleanPhone.length <= ValidationConstants.PHONE_MAX_LENGTH &&
                Pattern.matches(ValidationConstants.PHONE_REGEX, cleanPhone)
    }

    fun getPhoneError(phone: String): String? {
        val cleanPhone = phone.replace("+91", "").replace(" ", "").replace("-", "")
        return when {
            phone.isBlank() -> "Phone number is required"
            cleanPhone.length != ValidationConstants.INDIAN_PHONE_LENGTH -> "Phone number must be 10 digits"
            !Pattern.matches(ValidationConstants.INDIAN_PHONE_REGEX, cleanPhone) -> "Please enter a valid phone number"
            else -> null
        }
    }

    /**
     * Password validation
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= ValidationConstants.PASSWORD_MIN_LENGTH &&
                password.length <= ValidationConstants.PASSWORD_MAX_LENGTH &&
                Pattern.matches(ValidationConstants.PASSWORD_REGEX, password)
    }

    fun getPasswordError(password: String): String? {
        return when {
            password.isBlank() -> "Password is required"
            password.length < ValidationConstants.PASSWORD_MIN_LENGTH -> "Password must be at least 8 characters"
            password.length > ValidationConstants.PASSWORD_MAX_LENGTH -> "Password must not exceed 32 characters"
            !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !password.any { it.isDigit() } -> "Password must contain at least one digit"
            !password.any { "[@$!%*?&]".contains(it) } -> "Password must contain at least one special character"
            else -> null
        }
    }

    fun getPasswordStrength(password: String): PasswordStrength {
        var score = 0

        if (password.length >= 8) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { "[@$!%*?&]".contains(it) }) score++
        if (password.length >= 12) score++

        return when (score) {
            0, 1, 2 -> PasswordStrength.WEAK
            3, 4 -> PasswordStrength.MEDIUM
            5, 6 -> PasswordStrength.STRONG
            else -> PasswordStrength.VERY_STRONG
        }
    }

    enum class PasswordStrength(val label: String, val color: Long) {
        WEAK("Weak", 0xFFFF5722),
        MEDIUM("Medium", 0xFFFF9800),
        STRONG("Strong", 0xFF4CAF50),
        VERY_STRONG("Very Strong", 0xFF2E7D32)
    }

    /**
     * Name validation
     */
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() &&
                name.length >= ValidationConstants.NAME_MIN_LENGTH &&
                name.length <= ValidationConstants.NAME_MAX_LENGTH &&
                Pattern.matches(ValidationConstants.NAME_REGEX, name)
    }

    fun getNameError(name: String): String? {
        return when {
            name.isBlank() -> "Name is required"
            name.length < ValidationConstants.NAME_MIN_LENGTH -> "Name is too short"
            name.length > ValidationConstants.NAME_MAX_LENGTH -> "Name is too long"
            !Pattern.matches(ValidationConstants.NAME_REGEX, name) -> "Name can only contain letters and spaces"
            else -> null
        }
    }

    /**
     * PIN code validation
     */
    fun isValidPincode(pincode: String): Boolean {
        return pincode.length == ValidationConstants.PINCODE_LENGTH &&
                Pattern.matches(ValidationConstants.PINCODE_REGEX, pincode)
    }

    fun getPincodeError(pincode: String): String? {
        return when {
            pincode.isBlank() -> "PIN code is required"
            pincode.length != ValidationConstants.PINCODE_LENGTH -> "PIN code must be 6 digits"
            !Pattern.matches(ValidationConstants.PINCODE_REGEX, pincode) -> "Please enter a valid PIN code"
            else -> null
        }
    }

    /**
     * OTP validation
     */
    fun isValidOTP(otp: String): Boolean {
        return otp.length == ValidationConstants.OTP_LENGTH &&
                Pattern.matches(ValidationConstants.OTP_REGEX, otp)
    }

    fun getOTPError(otp: String): String? {
        return when {
            otp.isBlank() -> "OTP is required"
            otp.length != ValidationConstants.OTP_LENGTH -> "OTP must be 6 digits"
            !Pattern.matches(ValidationConstants.OTP_REGEX, otp) -> "Please enter a valid OTP"
            else -> null
        }
    }

    /**
     * IFSC code validation
     */
    fun isValidIFSCCode(ifsc: String): Boolean {
        return ifsc.length == ValidationConstants.IFSC_CODE_LENGTH &&
                Pattern.matches(ValidationConstants.IFSC_CODE_REGEX, ifsc.uppercase())
    }

    fun getIFSCError(ifsc: String): String? {
        return when {
            ifsc.isBlank() -> "IFSC code is required"
            ifsc.length != ValidationConstants.IFSC_CODE_LENGTH -> "IFSC code must be 11 characters"
            !Pattern.matches(ValidationConstants.IFSC_CODE_REGEX, ifsc.uppercase()) -> "Please enter a valid IFSC code"
            else -> null
        }
    }

    /**
     * PAN number validation
     */
    fun isValidPAN(pan: String): Boolean {
        return pan.length == ValidationConstants.PAN_LENGTH &&
                Pattern.matches(ValidationConstants.PAN_REGEX, pan.uppercase())
    }

    fun getPANError(pan: String): String? {
        return when {
            pan.isBlank() -> "PAN number is required"
            pan.length != ValidationConstants.PAN_LENGTH -> "PAN number must be 10 characters"
            !Pattern.matches(ValidationConstants.PAN_REGEX, pan.uppercase()) -> "Please enter a valid PAN number"
            else -> null
        }
    }

    /**
     * GST number validation
     */
    fun isValidGST(gst: String): Boolean {
        return gst.length == ValidationConstants.GST_NUMBER_LENGTH &&
                Pattern.matches(ValidationConstants.GST_REGEX, gst.uppercase())
    }

    fun getGSTError(gst: String): String? {
        return when {
            gst.isBlank() -> "GST number is required"
            gst.length != ValidationConstants.GST_NUMBER_LENGTH -> "GST number must be 15 characters"
            !Pattern.matches(ValidationConstants.GST_REGEX, gst.uppercase()) -> "Please enter a valid GST number"
            else -> null
        }
    }

    /**
     * Price validation
     */
    fun isValidPrice(price: String): Boolean {
        return try {
            val priceValue = price.toDouble()
            priceValue >= ValidationConstants.PRODUCT_PRICE_MIN &&
                    priceValue <= ValidationConstants.PRODUCT_PRICE_MAX
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun getPriceError(price: String): String? {
        return try {
            if (price.isBlank()) return "Price is required"
            val priceValue = price.toDouble()
            when {
                priceValue < ValidationConstants.PRODUCT_PRICE_MIN -> "Price must be at least ₹${ValidationConstants.PRODUCT_PRICE_MIN}"
                priceValue > ValidationConstants.PRODUCT_PRICE_MAX -> "Price must not exceed ₹${ValidationConstants.PRODUCT_PRICE_MAX}"
                else -> null
            }
        } catch (e: NumberFormatException) {
            "Please enter a valid price"
        }
    }

    /**
     * Quantity validation
     */
    fun isValidQuantity(quantity: Int): Boolean {
        return quantity >= ValidationConstants.MIN_QUANTITY &&
                quantity <= ValidationConstants.MAX_QUANTITY_PER_ITEM
    }

    fun getQuantityError(quantity: Int): String? {
        return when {
            quantity < ValidationConstants.MIN_QUANTITY -> "Minimum quantity is ${ValidationConstants.MIN_QUANTITY}"
            quantity > ValidationConstants.MAX_QUANTITY_PER_ITEM -> "Maximum quantity per item is ${ValidationConstants.MAX_QUANTITY_PER_ITEM}"
            else -> null
        }
    }

    /**
     * Rating validation
     */
    fun isValidRating(rating: Int): Boolean {
        return rating >= ValidationConstants.RATING_MIN && rating <= ValidationConstants.RATING_MAX
    }

    fun getRatingError(rating: Int): String? {
        return when {
            rating < ValidationConstants.RATING_MIN -> "Rating must be at least ${ValidationConstants.RATING_MIN}"
            rating > ValidationConstants.RATING_MAX -> "Rating must not exceed ${ValidationConstants.RATING_MAX}"
            else -> null
        }
    }

    /**
     * Review validation
     */
    fun isValidReview(review: String): Boolean {
        return review.isNotBlank() &&
                review.length >= ValidationConstants.REVIEW_MIN_LENGTH &&
                review.length <= ValidationConstants.REVIEW_MAX_LENGTH
    }

    fun getReviewError(review: String): String? {
        return when {
            review.isBlank() -> "Review is required"
            review.length < ValidationConstants.REVIEW_MIN_LENGTH -> "Review must be at least ${ValidationConstants.REVIEW_MIN_LENGTH} characters"
            review.length > ValidationConstants.REVIEW_MAX_LENGTH -> "Review must not exceed ${ValidationConstants.REVIEW_MAX_LENGTH} characters"
            else -> null
        }
    }

    /**
     * Coupon code validation
     */
    fun isValidCouponCode(coupon: String): Boolean {
        return coupon.isNotBlank() &&
                coupon.length >= ValidationConstants.COUPON_CODE_MIN_LENGTH &&
                coupon.length <= ValidationConstants.COUPON_CODE_MAX_LENGTH &&
                Pattern.matches(ValidationConstants.COUPON_CODE_REGEX, coupon.uppercase())
    }

    fun getCouponError(coupon: String): String? {
        return when {
            coupon.isBlank() -> "Coupon code is required"
            coupon.length < ValidationConstants.COUPON_CODE_MIN_LENGTH -> "Coupon code is too short"
            coupon.length > ValidationConstants.COUPON_CODE_MAX_LENGTH -> "Coupon code is too long"
            !Pattern.matches(ValidationConstants.COUPON_CODE_REGEX, coupon.uppercase()) -> "Invalid coupon code format"
            else -> null
        }
    }
}

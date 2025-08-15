 package com.optivus.bharathaat.constants

object ValidationConstants {

    // Phone Number Validation
    const val PHONE_MIN_LENGTH = 10
    const val PHONE_MAX_LENGTH = 15
    const val INDIAN_PHONE_LENGTH = 10
    const val PHONE_REGEX = "^[+]?[0-9]{10,15}$"
    const val INDIAN_PHONE_REGEX = "^[6-9][0-9]{9}$"

    // Email Validation
    const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    const val EMAIL_MIN_LENGTH = 5
    const val EMAIL_MAX_LENGTH = 100

    // Password Validation
    const val PASSWORD_MIN_LENGTH = 8
    const val PASSWORD_MAX_LENGTH = 32
    const val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"

    // Name Validation
    const val NAME_MIN_LENGTH = 2
    const val NAME_MAX_LENGTH = 50
    const val NAME_REGEX = "^[a-zA-Z\\s]{2,50}$"

    // PIN Code Validation
    const val PINCODE_LENGTH = 6
    const val PINCODE_REGEX = "^[1-9][0-9]{5}$"

    // OTP Validation
    const val OTP_LENGTH = 6
    const val OTP_REGEX = "^[0-9]{6}$"
    const val OTP_EXPIRY_MINUTES = 5

    // Product Validation
    const val PRODUCT_NAME_MIN_LENGTH = 3
    const val PRODUCT_NAME_MAX_LENGTH = 100
    const val PRODUCT_DESCRIPTION_MAX_LENGTH = 1000
    const val PRODUCT_PRICE_MIN = 1.0
    const val PRODUCT_PRICE_MAX = 1000000.0

    // Address Validation
    const val ADDRESS_LINE_MAX_LENGTH = 100
    const val CITY_MAX_LENGTH = 50
    const val STATE_MAX_LENGTH = 50
    const val LANDMARK_MAX_LENGTH = 100

    // Search Validation
    const val SEARCH_MIN_LENGTH = 2
    const val SEARCH_MAX_LENGTH = 100

    // Review Validation
    const val REVIEW_MIN_LENGTH = 10
    const val REVIEW_MAX_LENGTH = 500
    const val RATING_MIN = 1
    const val RATING_MAX = 5

    // Bank Account Validation
    const val ACCOUNT_NUMBER_MIN_LENGTH = 9
    const val ACCOUNT_NUMBER_MAX_LENGTH = 18
    const val IFSC_CODE_LENGTH = 11
    const val IFSC_CODE_REGEX = "^[A-Z]{4}0[A-Z0-9]{6}$"

    // GST Validation
    const val GST_NUMBER_LENGTH = 15
    const val GST_REGEX = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"

    // PAN Validation
    const val PAN_LENGTH = 10
    const val PAN_REGEX = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$"

    // Quantity Validation
    const val MIN_QUANTITY = 1
    const val MAX_QUANTITY_PER_ITEM = 10
    const val MAX_CART_ITEMS = 50

    // Coupon Code Validation
    const val COUPON_CODE_MIN_LENGTH = 3
    const val COUPON_CODE_MAX_LENGTH = 20
    const val COUPON_CODE_REGEX = "^[A-Z0-9]{3,20}$"
}

package com.optivus.bharat_haat.constants

object AppConstants {

    // App Information
    const val APP_NAME = "Bharat Haat"
    const val APP_VERSION = "1.0.0"
    const val SUPPORT_EMAIL = "support@bharathaat.com"
    const val PRIVACY_POLICY_URL = "https://bharathaat.com/privacy"
    const val TERMS_CONDITIONS_URL = "https://bharathaat.com/terms"

    // API Configuration
    const val BASE_URL = "https://api.bharathaat.com/v1/"
    const val API_TIMEOUT = 30L // seconds
    const val MAX_RETRY_ATTEMPTS = 3
    const val CACHE_SIZE = 10 * 1024 * 1024L // 10MB

    // Database
    const val DATABASE_NAME = "bharat_haat_db"
    const val DATABASE_VERSION = 1

    // SharedPreferences Keys
    const val PREFS_NAME = "bharat_haat_prefs"
    const val KEY_USER_TOKEN = "user_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USER_PHONE = "user_phone"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_FIRST_TIME_LAUNCH = "first_time_launch"
    const val KEY_SELECTED_LANGUAGE = "selected_language"
    const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
    const val KEY_DARK_MODE = "dark_mode"
    const val KEY_CART_COUNT = "cart_count"
    const val KEY_WISHLIST_COUNT = "wishlist_count"

    // Network Status
    const val NETWORK_TIMEOUT = 15000L
    const val SOCKET_TIMEOUT = 10000L
    const val CONNECTION_TIMEOUT = 10000L

    // Image Configuration
    const val MAX_IMAGE_SIZE = 5 * 1024 * 1024 // 5MB
    const val COMPRESSED_IMAGE_QUALITY = 80
    const val MAX_IMAGE_DIMENSION = 1920

    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_PAGE_SIZE = 100
    const val INITIAL_PAGE = 1

    // Currency
    const val CURRENCY_SYMBOL = "₹"
    const val CURRENCY_CODE = "INR"
    const val DEFAULT_DECIMAL_PLACES = 2

    // Payment
    const val MIN_ORDER_AMOUNT = 1.0
    const val MAX_ORDER_AMOUNT = 100000.0
    const val FREE_DELIVERY_THRESHOLD = 500.0
    const val DEFAULT_DELIVERY_CHARGE = 50.0
    const val COD_CHARGE = 20.0
    const val MAX_COD_AMOUNT = 5000.0

    // Time Format
    const val DATE_FORMAT_DISPLAY = "dd MMM yyyy"
    const val DATE_FORMAT_API = "yyyy-MM-dd"
    const val TIME_FORMAT_12 = "hh:mm a"
    const val TIME_FORMAT_24 = "HH:mm"
    const val DATETIME_FORMAT = "dd MMM yyyy, hh:mm a"
    const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    // Animation Duration
    const val ANIMATION_DURATION_SHORT = 150L
    const val ANIMATION_DURATION_MEDIUM = 300L
    const val ANIMATION_DURATION_LONG = 500L
    const val SPLASH_DELAY = 2000L

    // Request Codes
    const val REQUEST_CODE_PERMISSION = 1001
    const val REQUEST_CODE_CAMERA = 1002
    const val REQUEST_CODE_GALLERY = 1003
    const val REQUEST_CODE_LOCATION = 1004
    const val REQUEST_CODE_STORAGE = 1005

    // Bundle Keys
    const val BUNDLE_PRODUCT_ID = "product_id"
    const val BUNDLE_CATEGORY_ID = "category_id"
    const val BUNDLE_ORDER_ID = "order_id"
    const val BUNDLE_USER_DATA = "user_data"
    const val BUNDLE_CART_DATA = "cart_data"

    // Firebase
    const val FCM_TOPIC_ALL_USERS = "all_users"
    const val FCM_TOPIC_OFFERS = "offers"
    const val FCM_TOPIC_ORDER_UPDATES = "order_updates"

    // File Paths
    const val CACHE_DIR = "cache"
    const val IMAGES_DIR = "images"
    const val DOCUMENTS_DIR = "documents"

    // URL Schemes
    const val DEEP_LINK_SCHEME = "bharathaat"
    const val SHARE_URL_PREFIX = "https://bharathaat.com/share/"

    // Error Messages
    const val ERROR_NETWORK = "Network connection error"
    const val ERROR_SERVER = "Server error occurred"
    const val ERROR_UNKNOWN = "Unknown error occurred"
    const val ERROR_TIMEOUT = "Request timeout"
    const val ERROR_NO_DATA = "No data found"
}

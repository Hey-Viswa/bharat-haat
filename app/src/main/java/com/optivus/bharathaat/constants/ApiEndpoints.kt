package com.optivus.bharathaat.constants

object ApiEndpoints {

    // Authentication Endpoints
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"
    const val LOGOUT = "auth/logout"
    const val REFRESH_TOKEN = "auth/refresh"
    const val SEND_OTP = "auth/send-otp"
    const val VERIFY_OTP = "auth/verify-otp"
    const val FORGOT_PASSWORD = "auth/forgot-password"
    const val RESET_PASSWORD = "auth/reset-password"
    const val CHANGE_PASSWORD = "auth/change-password"

    // User Management
    const val USER_PROFILE = "user/profile"
    const val UPDATE_PROFILE = "user/update"
    const val DELETE_ACCOUNT = "user/delete"
    const val USER_ADDRESSES = "user/addresses"
    const val ADD_ADDRESS = "user/addresses/add"
    const val UPDATE_ADDRESS = "user/addresses/{id}/update"
    const val DELETE_ADDRESS = "user/addresses/{id}/delete"
    const val SET_DEFAULT_ADDRESS = "user/addresses/{id}/default"

    // Products
    const val PRODUCTS = "products"
    const val PRODUCT_DETAILS = "products/{id}"
    const val PRODUCT_SEARCH = "products/search"
    const val PRODUCT_FILTER = "products/filter"
    const val FEATURED_PRODUCTS = "products/featured"
    const val TRENDING_PRODUCTS = "products/trending"
    const val RELATED_PRODUCTS = "products/{id}/related"
    const val PRODUCT_REVIEWS = "products/{id}/reviews"
    const val ADD_REVIEW = "products/{id}/reviews/add"

    // Categories
    const val CATEGORIES = "categories"
    const val CATEGORY_PRODUCTS = "categories/{id}/products"
    const val SUBCATEGORIES = "categories/{id}/subcategories"

    // Cart Management
    const val CART = "cart"
    const val ADD_TO_CART = "cart/add"
    const val UPDATE_CART_ITEM = "cart/items/{id}/update"
    const val REMOVE_FROM_CART = "cart/items/{id}/remove"
    const val CLEAR_CART = "cart/clear"
    const val CART_COUNT = "cart/count"

    // Wishlist
    const val WISHLIST = "wishlist"
    const val ADD_TO_WISHLIST = "wishlist/add"
    const val REMOVE_FROM_WISHLIST = "wishlist/remove/{id}"
    const val WISHLIST_COUNT = "wishlist/count"

    // Orders
    const val ORDERS = "orders"
    const val ORDER_DETAILS = "orders/{id}"
    const val PLACE_ORDER = "orders/place"
    const val CANCEL_ORDER = "orders/{id}/cancel"
    const val TRACK_ORDER = "orders/{id}/track"
    const val ORDER_HISTORY = "orders/history"
    const val REORDER = "orders/{id}/reorder"

    // Payments
    const val PAYMENT_METHODS = "payments/methods"
    const val INITIATE_PAYMENT = "payments/initiate"
    const val VERIFY_PAYMENT = "payments/verify"
    const val PAYMENT_STATUS = "payments/{id}/status"
    const val SAVED_CARDS = "payments/cards"
    const val ADD_CARD = "payments/cards/add"
    const val DELETE_CARD = "payments/cards/{id}/delete"

    // Coupons & Offers
    const val COUPONS = "coupons"
    const val VALIDATE_COUPON = "coupons/validate"
    const val APPLY_COUPON = "coupons/apply"
    const val OFFERS = "offers"
    const val BANNER_OFFERS = "offers/banners"

    // Notifications
    const val NOTIFICATIONS = "notifications"
    const val MARK_READ = "notifications/{id}/read"
    const val MARK_ALL_READ = "notifications/read-all"
    const val NOTIFICATION_SETTINGS = "notifications/settings"

    // Support
    const val SUPPORT_TICKETS = "support/tickets"
    const val CREATE_TICKET = "support/tickets/create"
    const val TICKET_DETAILS = "support/tickets/{id}"
    const val FAQ = "support/faq"
    const val CONTACT_US = "support/contact"

    // Delivery
    const val DELIVERY_SLOTS = "delivery/slots"
    const val DELIVERY_CHARGES = "delivery/charges"
    const val PIN_CODE_CHECK = "delivery/pincode/{code}"

    // Seller/Vendor (if applicable)
    const val SELLER_PROFILE = "seller/profile"
    const val SELLER_PRODUCTS = "seller/products"
    const val SELLER_ORDERS = "seller/orders"
    const val SELLER_DASHBOARD = "seller/dashboard"

    // App Configuration
    const val APP_CONFIG = "config/app"
    const val TERMS_CONDITIONS = "config/terms"
    const val PRIVACY_POLICY = "config/privacy"
    const val RETURN_POLICY = "config/return-policy"
    const val SHIPPING_POLICY = "config/shipping-policy"

    // Location Services
    const val NEARBY_STORES = "location/stores"
    const val DELIVERY_AREAS = "location/delivery-areas"
}

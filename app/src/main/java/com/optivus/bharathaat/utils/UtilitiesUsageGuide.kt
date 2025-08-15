package com.optivus.bharathaat.utils

/**
 * COMPREHENSIVE UTILITIES USAGE GUIDE FOR BHARAT HAAT ECOMMERCE APP
 * =================================================================
 *
 * This guide explains when, where, and how to use each utility in your ecommerce app.
 * Each utility is designed for specific scenarios to ensure consistent, secure, and
 * efficient functionality across the entire application.
 */

object UtilitiesUsageGuide {

    /**
     * 1. VALIDATION UTILS - ValidationUtils.kt
     * =========================================
     *
     * USE FOR: Input validation across all forms and user inputs
     * WHEN TO USE:
     * - User registration/login forms
     * - Product review submissions
     * - Address form validation
     * - Payment information validation
     * - Search input validation
     * - Profile update forms
     *
     * EXAMPLES:
     *
     * // Email validation in login screen
     * val emailError = ValidationUtils.getEmailError(email)
     * if (emailError != null) {
     *     showError(emailError)
     *     return
     * }
     *
     * // Password strength in registration
     * val passwordStrength = ValidationUtils.getPasswordStrength(password)
     * showPasswordStrengthIndicator(passwordStrength)
     *
     * // Phone number validation in profile
     * if (!ValidationUtils.isValidPhoneNumber(phone)) {
     *     showError("Please enter a valid phone number")
     * }
     *
     * // Product review validation
     * val reviewError = ValidationUtils.getReviewError(reviewText)
     * val ratingError = ValidationUtils.getRatingError(rating)
     *
     * // Address validation
     * val pincodeError = ValidationUtils.getPincodeError(pincode)
     *
     * // Payment validation
     * if (!ValidationUtils.isValidIFSCCode(ifscCode)) {
     *     showError("Invalid IFSC code")
     * }
     */

    /**
     * 2. CALCULATION UTILS - CalculationUtils.kt
     * ==========================================
     *
     * USE FOR: All price calculations, discounts, taxes, and cart operations
     * WHEN TO USE:
     * - Cart total calculations
     * - Product discount displays
     * - Tax calculations (GST)
     * - Delivery charge calculations
     * - Coupon application
     * - EMI calculations
     * - Order summary calculations
     *
     * EXAMPLES:
     *
     * // Cart calculations in CartViewModel
     * val subtotal = CalculationUtils.calculateSubtotal(cartItems)
     * val deliveryCharge = CalculationUtils.calculateDeliveryCharge(
     *     subtotal = subtotal,
     *     distance = userDistance,
     *     isCOD = isCashOnDelivery
     * )
     * val finalTotal = CalculationUtils.calculateCartTotal(
     *     subtotal = subtotal,
     *     deliveryCharge = deliveryCharge,
     *     couponDiscount = appliedCouponDiscount,
     *     gstPercentage = 18.0
     * )
     *
     * // Product discount display
     * val discountedPrice = CalculationUtils.calculateDiscountedPrice(originalPrice, discountPercent)
     * val savings = CalculationUtils.calculateSavings(originalPrice, discountedPrice)
     *
     * // Tax calculations for business users
     * val gstAmount = CalculationUtils.calculateGST(amount, 18.0)
     * val cgst = CalculationUtils.calculateCGST(amount, 18.0)
     * val sgst = CalculationUtils.calculateSGST(amount, 18.0)
     *
     * // Currency formatting for display
     * val formattedPrice = CalculationUtils.formatCurrency(price)
     * val compactPrice = CalculationUtils.formatCurrencyCompact(largeAmount) // Shows as 1.5L, 2.3Cr
     *
     * // Coupon application
     * val couponResult = CalculationUtils.applyCoupon(
     *     subtotal = cartSubtotal,
     *     couponType = CouponType.PERCENTAGE,
     *     couponValue = 10.0,
     *     maxDiscount = 500.0,
     *     minOrderAmount = 1000.0
     * )
     */

    /**
     * 3. STRING UTILS - StringUtils.kt
     * ================================
     *
     * USE FOR: Text processing, formatting, and manipulation
     * WHEN TO USE:
     * - Phone number formatting
     * - Email masking for privacy
     * - Search query processing
     * - Address formatting
     * - Product name formatting
     * - URL generation for sharing
     * - Text truncation for UI
     *
     * EXAMPLES:
     *
     * // Phone number formatting in profile display
     * val formattedPhone = StringUtils.formatPhoneNumber(rawPhoneNumber)
     * val maskedPhone = StringUtils.maskPhoneNumber(phoneNumber) // Shows XXXXXX1234
     *
     * // Email masking for privacy
     * val maskedEmail = StringUtils.maskEmail(userEmail) // Shows jo***@email.com
     *
     * // Search functionality
     * val normalizedQuery = StringUtils.normalizeSearchQuery(userInput)
     * val suggestions = StringUtils.getSearchSuggestions(query, availableProducts)
     *
     * // Product URL generation for sharing
     * val productSlug = StringUtils.createSlug(productName)
     * val shareUrl = "https://bharathaat.com/product/$productSlug"
     *
     * // Text truncation for product cards
     * val truncatedTitle = StringUtils.truncate(productTitle, maxLength = 50)
     * val truncatedDescription = StringUtils.truncateWords(description, maxWords = 20)
     *
     * // Address formatting
     * val fullAddress = StringUtils.formatAddress(line1, line2, city, state, pincode)
     *
     * // Order ID generation
     * val orderId = StringUtils.generateOrderId() // Generates: ORD12345678ABCD
     *
     * // Referral code generation
     * val referralCode = StringUtils.generateReferralCode(userName) // Generates: ABC1234
     */

    /**
     * 4. DATETIME UTILS - DateTimeUtils.kt
     * ====================================
     *
     * USE FOR: Date/time operations, delivery scheduling, order tracking
     * WHEN TO USE:
     * - Order date formatting
     * - Delivery date calculations
     * - Relative time display (2 hours ago)
     * - Business day calculations
     * - Time slot management
     * - Age verification
     *
     * EXAMPLES:
     *
     * // Order date display
     * val orderDateFormatted = DateTimeUtils.formatDate(orderDate)
     * val relativeTime = DateTimeUtils.getRelativeTimeString(orderDate) // "2 hours ago"
     *
     * // Delivery date calculations
     * val estimatedDelivery = DateTimeUtils.getEstimatedDeliveryDate(orderDate, deliveryDays = 3)
     * val deliveryRange = DateTimeUtils.getDeliveryDateRange(orderDate, minDays = 2, maxDays = 5)
     *
     * // Time slots for delivery scheduling
     * val availableSlots = DateTimeUtils.getDeliveryTimeSlots()
     * val isValidDeliveryTime = DateTimeUtils.isValidDeliveryTime(selectedDateTime)
     *
     * // Business logic
     * val businessDaysToDelivery = DateTimeUtils.getBusinessDaysBetween(orderDate, deliveryDate)
     * val nextBusinessDay = DateTimeUtils.addBusinessDays(today, 1)
     *
     * // Age verification for restricted products
     * val userAge = DateTimeUtils.calculateAge(birthDate)
     * if (userAge < 18) {
     *     // Restrict alcohol/tobacco products
     * }
     *
     * // Order tracking timeline
     * if (DateTimeUtils.isToday(orderDate)) {
     *     showMessage("Order placed today")
     * } else if (DateTimeUtils.isYesterday(orderDate)) {
     *     showMessage("Order placed yesterday")
     * }
     */

    /**
     * 5. PREFERENCES UTILS - PreferencesUtils.kt
     * ==========================================
     *
     * USE FOR: Storing user data, app settings, and session management
     * WHEN TO USE:
     * - User login state persistence
     * - Cart count storage
     * - App settings (language, notifications)
     * - Search history
     * - Recently viewed products
     * - User preferences
     *
     * EXAMPLES:
     *
     * // User session management
     * PreferencesUtils.setLoggedIn(context, true)
     * val isUserLoggedIn = PreferencesUtils.isLoggedIn(context)
     * PreferencesUtils.saveUserToken(context, authToken)
     * PreferencesUtils.logout(context) // Clears all user data
     *
     * // Cart management
     * PreferencesUtils.setCartCount(context, cartItems.size)
     * val cartBadgeCount = PreferencesUtils.getCartCount(context)
     *
     * // App settings
     * PreferencesUtils.setNotificationEnabled(context, true)
     * PreferencesUtils.setSelectedLanguage(context, "hi") // Hindi
     * val isDarkMode = PreferencesUtils.isDarkModeEnabled(context)
     *
     * // Search functionality
     * PreferencesUtils.addToSearchHistory(context, searchQuery)
     * val recentSearches = PreferencesUtils.getSearchHistory(context)
     * PreferencesUtils.clearSearchHistory(context)
     *
     * // Product browsing
     * PreferencesUtils.addToRecentlyViewed(context, productId)
     * val recentProducts = PreferencesUtils.getRecentlyViewed(context)
     *
     * // Cache management
     * PreferencesUtils.setCacheTimestamp(context, "product_list")
     * val isCacheValid = PreferencesUtils.isCacheValid(context, "product_list", validityDuration = 300000L)
     *
     * // First time user experience
     * if (PreferencesUtils.isFirstTimeLaunch(context)) {
     *     showOnboarding()
     *     PreferencesUtils.setFirstTimeLaunch(context, false)
     * }
     */

    /**
     * 6. NETWORK UTILS - NetworkUtils.kt
     * ==================================
     *
     * USE FOR: Network connectivity checks and API call management
     * WHEN TO USE:
     * - Before making API calls
     * - Showing offline/online states
     * - Network error handling
     * - Retry logic for failed requests
     * - Bandwidth-aware loading
     *
     * EXAMPLES:
     *
     * // Before API calls
     * if (!NetworkUtils.isNetworkAvailable(context)) {
     *     showOfflineMessage()
     *     return
     * }
     *
     * // Network type based loading
     * val networkType = NetworkUtils.getNetworkType(context)
     * when (networkType) {
     *     NetworkType.WIFI -> loadHighQualityImages()
     *     NetworkType.MOBILE -> loadCompressedImages()
     *     NetworkType.NONE -> showCachedData()
     * }
     *
     * // Network speed optimization
     * val networkSpeed = NetworkUtils.getNetworkSpeed(context)
     * when (networkSpeed) {
     *     NetworkSpeed.SLOW -> {
     *         // Load minimal data
     *         loadBasicProductInfo()
     *     }
     *     NetworkSpeed.FAST -> {
     *         // Load full data with images
     *         loadFullProductDetails()
     *     }
     * }
     *
     * // Error handling
     * if (NetworkUtils.isNetworkError(exception)) {
     *     val errorMessage = NetworkUtils.getNetworkErrorMessage(exception)
     *     showRetryOption(errorMessage)
     * }
     *
     * // WiFi vs Mobile data awareness
     * if (NetworkUtils.isWifiConnected(context)) {
     *     // Auto-sync large data
     *     syncProductCatalog()
     * } else if (NetworkUtils.isMobileDataConnected(context)) {
     *     // Ask user before syncing
     *     askUserForDataSync()
     * }
     */

    /**
     * 7. FILE UTILS - FileUtils.kt
     * ============================
     *
     * USE FOR: File operations, image caching, and storage management
     * WHEN TO USE:
     * - Image caching for products
     * - Document storage (invoices, receipts)
     * - App data backup
     * - Cache management
     * - File sharing functionality
     *
     * EXAMPLES:
     *
     * // Image caching for products
     * val imageFile = FileUtils.createFile(FileUtils.getImagesDirectory(context), "product_$productId.jpg")
     * FileUtils.writeBytesToFile(imageFile, imageBytes)
     *
     * // Cache management
     * val cacheSize = FileUtils.getCacheSize(context)
     * val formattedSize = FileUtils.formatFileSize(cacheSize) // "15.2 MB"
     * if (cacheSize > 100 * 1024 * 1024) { // 100MB
     *     FileUtils.clearCache(context)
     * }
     *
     * // Document storage (invoices, order receipts)
     * val documentsDir = FileUtils.getDocumentsDirectory(context)
     * val invoiceFile = FileUtils.createFile(documentsDir, "invoice_$orderId.pdf")
     *
     * // File validation for user uploads
     * if (FileUtils.isValidImageFile(selectedFile)) {
     *     if (FileUtils.isFileSizeValid(selectedFile, maxSize = 5 * 1024 * 1024)) { // 5MB
     *         uploadProductImage(selectedFile)
     *     } else {
     *         showError("Image size must be less than 5MB")
     *     }
     * }
     *
     * // App data backup
     * val backupData = getUserDataAsJson()
     * FileUtils.createBackup(context, backupData, "user_backup_${System.currentTimeMillis()}.json")
     *
     * // File sharing for order receipts
     * val shareUri = FileUtils.shareFile(context, receiptFile)
     * shareOrderReceipt(shareUri)
     *
     * // Cleanup temporary files
     * FileUtils.cleanupTempFiles(context) // Call in onDestroy or periodically
     */

    /**
     * 8. IMAGE UTILS - ImageUtils.kt
     * ==============================
     *
     * USE FOR: Image processing, compression, and manipulation
     * WHEN TO USE:
     * - Product image uploads
     * - Profile picture processing
     * - Image compression for storage
     * - Thumbnail generation
     * - Image effects and filters
     *
     * EXAMPLES:
     *
     * // Product image upload with compression
     * val compressedImage = ImageUtils.compressImage(
     *     context = context,
     *     uri = selectedImageUri,
     *     maxWidth = 1024,
     *     maxHeight = 1024,
     *     quality = 80
     * )
     *
     * // Profile picture processing
     * val circularAvatar = ImageUtils.createCircularBitmap(profileBitmap)
     * ImageUtils.saveBitmapToFile(circularAvatar, profileImageFile)
     *
     * // Thumbnail generation for product grid
     * val thumbnail = ImageUtils.createThumbnail(productImage, size = 200)
     *
     * // Image validation
     * if (ImageUtils.isValidImageFile(context, imageUri)) {
     *     val dimensions = ImageUtils.getImageDimensions(context, imageUri)
     *     if (dimensions != null && dimensions.first > 500 && dimensions.second > 500) {
     *         processProductImage(imageUri)
     *     } else {
     *         showError("Image must be at least 500x500 pixels")
     *     }
     * }
     *
     * // Watermark for product images (for sellers)
     * val watermarkedImage = ImageUtils.addTextWatermark(
     *     bitmap = productImage,
     *     watermarkText = "Bharat Haat",
     *     textSize = 24f,
     *     alpha = 128
     * )
     *
     * // Image effects for product display
     * val roundedProductImage = ImageUtils.createRoundedBitmap(productBitmap, cornerRadius = 12f)
     *
     * // Dominant color extraction for UI theming
     * val dominantColor = ImageUtils.getDominantColor(productBitmap)
     * setProductCardBackgroundColor(dominantColor)
     */

    /**
     * 9. DEVICE UTILS - DeviceUtils.kt
     * ================================
     *
     * USE FOR: Device information, responsive UI, and feature detection
     * WHEN TO USE:
     * - Responsive UI design
     * - Feature availability checks
     * - Analytics and crash reporting
     * - Security checks
     * - Performance optimization
     *
     * EXAMPLES:
     *
     * // Responsive UI design
     * if (DeviceUtils.isTablet(context)) {
     *     showTabletLayout()
     *     setGridColumns(3)
     * } else {
     *     showPhoneLayout()
     *     setGridColumns(2)
     * }
     *
     * // Feature availability
     * if (DeviceUtils.hasCamera(context)) {
     *     showCameraOption()
     * }
     * if (DeviceUtils.hasNFC(context)) {
     *     enableNFCPayments()
     * }
     * if (DeviceUtils.hasFingerprint(context)) {
     *     enableBiometricLogin()
     * }
     *
     * // Analytics data
     * val deviceInfo = DeviceUtils.getDeviceInfo()
     * analyticsTracker.setDeviceInfo(deviceInfo)
     *
     * // Performance optimization
     * val availableRAM = DeviceUtils.getAvailableRAM(context)
     * if (DeviceUtils.isLowMemory(context)) {
     *     // Reduce image quality, limit cache size
     *     reduceCacheSize()
     * }
     *
     * // Screen size calculations
     * val screenWidth = DeviceUtils.getScreenWidth(context)
     * val productImageSize = screenWidth / 2 - DeviceUtils.dpToPx(context, 16f)
     *
     * // Security checks
     * if (DeviceUtils.isDeviceRooted()) {
     *     // Show warning for payment security
     *     showRootedDeviceWarning()
     * }
     *
     * // Localization
     * if (DeviceUtils.isRTL(context)) {
     *     setupRTLLayout()
     * }
     */

    /**
     * 10. SECURITY UTILS - SecurityUtils.kt
     * =====================================
     *
     * USE FOR: Data security, encryption, and authentication
     * WHEN TO USE:
     * - Password handling
     * - Sensitive data storage
     * - API security
     * - Rate limiting
     * - Session management
     * - Payment security
     *
     * EXAMPLES:
     *
     * // Password security
     * val (hashedPassword, salt) = SecurityUtils.hashPassword(userPassword)
     * // Store hashedPassword and salt in secure storage
     *
     * val isPasswordValid = SecurityUtils.verifyPassword(inputPassword, storedHash, storedSalt)
     *
     * // Sensitive data encryption
     * val encryptedCardInfo = SecurityUtils.encryptWithPassword(cardDetails, userPassword)
     * val decryptedCardInfo = SecurityUtils.decryptWithPassword(encryptedData, userPassword)
     *
     * // Rate limiting for security
     * if (SecurityUtils.isRateLimited(context, "payment_attempt", maxAttempts = 3, timeWindowMinutes = 30)) {
     *     showError("Too many payment attempts. Please try again later.")
     *     return
     * }
     * SecurityUtils.recordAttempt(context, "payment_attempt")
     *
     * // Session management
     * val sessionToken = SecurityUtils.generateSessionToken()
     * if (SecurityUtils.isSessionExpired(sessionToken, expirationHours = 24)) {
     *     redirectToLogin()
     * }
     *
     * // Payment card security
     * val maskedCardNumber = SecurityUtils.maskCardNumber(cardNumber) // Shows **** **** **** 1234
     * if (SecurityUtils.validateCardNumber(cardNumber)) {
     *     processPayment()
     * }
     *
     * // Input sanitization
     * val sanitizedReview = SecurityUtils.sanitizeInput(userReview)
     * val sanitizedSearchQuery = SecurityUtils.sanitizeForSQL(searchQuery)
     *
     * // API security
     * val apiSignature = SecurityUtils.generateApiSignature(
     *     method = "POST",
     *     url = "/api/orders",
     *     timestamp = currentTimestamp,
     *     body = requestBody,
     *     secretKey = apiSecretKey
     * )
     *
     * // OTP generation
     * val secureOTP = SecurityUtils.generateSecureOTP(length = 6)
     * sendOTPToUser(phoneNumber, secureOTP)
     */

    /**
     * 11. NOTIFICATION UTILS - NotificationUtils.kt
     * =============================================
     *
     * USE FOR: Push notifications, order updates, and user engagement
     * WHEN TO USE:
     * - Order status updates
     * - Promotional offers
     * - Cart abandonment
     * - Price drop alerts
     * - Delivery notifications
     * - Chat messages
     *
     * EXAMPLES:
     *
     * // Initialize notification channels (in Application class or MainActivity)
     * NotificationUtils.createNotificationChannels(context)
     *
     * // Order notifications
     * NotificationUtils.showOrderNotification(
     *     context = context,
     *     orderId = order.id,
     *     title = "Order Confirmed",
     *     message = "Your order #${order.id} has been confirmed and will be delivered by ${order.deliveryDate}",
     *     intent = createOrderTrackingIntent(order.id)
     * )
     *
     * // Delivery notifications
     * NotificationUtils.showDeliveryNotification(
     *     context = context,
     *     title = "Out for Delivery",
     *     message = "Your order is out for delivery and will arrive soon!",
     *     orderId = order.id,
     *     isOutForDelivery = true
     * )
     *
     * // Promotional notifications
     * NotificationUtils.showOfferNotification(
     *     context = context,
     *     title = "Flash Sale! 50% Off",
     *     message = "Limited time offer on electronics. Shop now!",
     *     imageUrl = offerBannerUrl,
     *     intent = createOfferIntent()
     * )
     *
     * // Cart abandonment
     * NotificationUtils.showCartAbandonmentNotification(
     *     context = context,
     *     itemCount = abandonedCartItems.size,
     *     intent = createCartIntent()
     * )
     *
     * // Price drop alerts
     * NotificationUtils.showPriceDropNotification(
     *     context = context,
     *     productName = product.name,
     *     oldPrice = "₹${product.oldPrice}",
     *     newPrice = "₹${product.newPrice}",
     *     intent = createProductIntent(product.id)
     * )
     *
     * // Chat notifications
     * NotificationUtils.showChatNotification(
     *     context = context,
     *     senderName = "Customer Support",
     *     message = "Your query has been resolved. Please check the app.",
     *     chatId = chat.id,
     *     intent = createChatIntent(chat.id)
     * )
     *
     * // Notification management
     * if (!NotificationUtils.areNotificationsEnabled(context)) {
     *     showNotificationPermissionDialog()
     * }
     *
     * // Clear notifications when appropriate
     * NotificationUtils.cancelNotification(context, NotificationUtils.NOTIFICATION_ORDER_UPDATE)
     */

    /**
     * INTEGRATION BEST PRACTICES
     * ==========================
     *
     * 1. Always check network connectivity before API calls using NetworkUtils
     * 2. Validate all user inputs using ValidationUtils before processing
     * 3. Use SecurityUtils for any sensitive data operations
     * 4. Store user preferences and session data using PreferencesUtils
     * 5. Use CalculationUtils for all monetary calculations to ensure accuracy
     * 6. Implement proper error handling with user-friendly messages
     * 7. Use DateTimeUtils for consistent date/time formatting across the app
     * 8. Optimize images using ImageUtils before storage or upload
     * 9. Use DeviceUtils for responsive design and feature detection
     * 10. Implement proper notification strategies using NotificationUtils
     *
     * PERFORMANCE TIPS
     * ===============
     *
     * 1. Cache network responses using PreferencesUtils with timestamps
     * 2. Compress images using ImageUtils before storage
     * 3. Use FileUtils to manage cache size and cleanup temporary files
     * 4. Check device capabilities using DeviceUtils to optimize performance
     * 5. Implement rate limiting using SecurityUtils to prevent abuse
     * 6. Use StringUtils for efficient text processing operations
     * 7. Validate inputs client-side using ValidationUtils before API calls
     * 8. Use CalculationUtils for client-side calculations to reduce server load
     */
}

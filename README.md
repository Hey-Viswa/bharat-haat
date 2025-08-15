# Bharat Haat - Indian Ecommerce App

A modern Android ecommerce application built with Jetpack Compose, focusing on Indian handicrafts, traditional products, and local artisan goods.

## 🚀 Features

### ✅ Implemented
- **Authentication System**
  - Email/Password authentication with Firebase
  - Phone number authentication with OTP
  - Real-time input validation
  - Password strength indicators
  - Rate limiting for security

- **User Interface**
  - Modern Jetpack Compose UI
  - Light theme enforcement
  - Responsive design for tablets and phones
  - Custom text fields with validation
  - Material Design 3 components

- **Comprehensive Utilities**
  - Validation utilities for all input types
  - Calculation utilities for pricing, taxes, discounts
  - String formatting and manipulation
  - Date/time operations
  - File and image processing
  - Security utilities with encryption
  - Network connectivity management
  - SharedPreferences with Gson support

- **Product Management**
  - Product grid display
  - Search functionality with history
  - Category filtering
  - Recently viewed products
  - Product ratings and reviews

### 🔄 In Development
- Cart and checkout system
- Payment integration
- Order tracking
- Push notifications
- Seller dashboard

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModels
- **Dependency Injection**: Hilt
- **Authentication**: Firebase Auth
- **Image Loading**: Coil
- **JSON Parsing**: Gson
- **Networking**: (To be implemented)
- **Database**: (To be implemented)

## 📱 App Structure

```
app/src/main/java/com/optivus/bharat_haat/
├── constants/          # App-wide constants
│   ├── AppConstants.kt
│   ├── ValidationConstants.kt
│   ├── ApiEndpoints.kt
│   └── UIConstants.kt
├── utils/              # Utility classes
│   ├── ValidationUtils.kt
│   ├── CalculationUtils.kt
│   ├── StringUtils.kt
│   ├── DateTimeUtils.kt
│   ├── PreferencesUtils.kt
│   ├── NetworkUtils.kt
│   ├── FileUtils.kt
│   ├── ImageUtils.kt
│   ├── DeviceUtils.kt
│   ├── SecurityUtils.kt
│   └── NotificationUtils.kt
├── ui/
│   ├── components/     # Reusable UI components
│   ├── screens/        # App screens
│   ├── theme/          # App theming
│   └── viewmodels/     # ViewModels
└── MainActivity.kt
```

## 🎨 Design Philosophy

- **User-Centric**: Focus on Indian users and shopping patterns
- **Performance**: Optimized for various device capabilities
- **Security**: Comprehensive input validation and data protection
- **Accessibility**: Support for different screen sizes and orientations
- **Offline-First**: Caching and offline functionality where possible

## 🔧 Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd bharat-haat
   ```

2. **Open in Android Studio**
   - Use Android Studio Hedgehog or later
   - Ensure Kotlin plugin is up to date

3. **Configure Firebase**
   - Add your `google-services.json` file to `app/` directory
   - Enable Authentication in Firebase Console
   - Configure email/password and phone authentication

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

## 📋 Key Features Detail

### Authentication
- Firebase Authentication integration
- Email/password and phone number login
- Real-time validation with custom error messages
- Password strength indicators
- Rate limiting to prevent brute force attacks

### Validation System
- Comprehensive input validation for all form fields
- Indian-specific validations (phone numbers, PIN codes)
- Email, password, name, and address validation
- Financial validations (IFSC, PAN, GST numbers)

### Calculation Engine
- Product pricing with discounts and taxes
- Cart total calculations with delivery charges
- GST calculations (CGST, SGST, IGST)
- Coupon and offer applications
- EMI calculations for financing

### Security
- Input sanitization and validation
- Data encryption for sensitive information
- Session management with secure tokens
- Rate limiting for API endpoints
- Payment card validation

## 🎯 Future Roadmap

- [ ] Complete cart and checkout flow
- [ ] Payment gateway integration
- [ ] Order management system
- [ ] Push notifications
- [ ] Seller onboarding and dashboard
- [ ] Advanced search and filters
- [ ] Wishlist functionality
- [ ] Social features (reviews, ratings)
- [ ] Multilingual support

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Team

- **Development**: Bharat Haat Development Team
- **Design**: UI/UX Team
- **Product**: Product Management Team

## 📞 Support

For support and queries:
- Email: support@bharathaat.com
- Documentation: [Link to documentation]
- Issues: [GitHub Issues page]

---

**Bharat Haat** - Connecting India's artisans with the world 🇮🇳

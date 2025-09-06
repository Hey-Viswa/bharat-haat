# Bharat Haat - Indian Ecommerce App 🇮🇳

A modern Android ecommerce application built with Jetpack Compose, focusing on Indian handicrafts, traditional products, and local artisan goods. The app connects customers with authentic Indian products while providing artisans and sellers with a digital marketplace platform.

## 🚀 Features

### ✅ Implemented

#### 🔐 Robust Authentication System
- **Multi-method Authentication**
  - Email/Password authentication with Firebase
  - Phone number authentication with OTP verification
  - Google Sign-In integration
  - Session management with secure tokens
- **Security Features**
  - Real-time input validation and sanitization
  - Password strength indicators with visual feedback
  - Rate limiting to prevent brute force attacks
  - Network connectivity checks before auth operations
  - Comprehensive error handling with user-friendly messages

#### 🎨 Modern User Interface
- **Jetpack Compose UI**
  - Material Design 3 components and theming
  - Custom color scheme optimized for Indian market
  - Responsive design supporting phones and tablets
  - Dark/Light theme support
- **Custom Components**
  - Reusable UI components (buttons, cards, text fields)
  - Product cards with image loading and rating display
  - Custom navigation with bottom navigation bar
  - Loading states and error handling UI

#### 🛠️ Comprehensive Utility System
- **Validation Utilities**
  - Indian-specific validations (phone numbers, PIN codes)
  - Financial data validation (IFSC codes, PAN, GST numbers)
  - Email, password, and address validation
  - Real-time validation feedback
- **Calculation Engine**
  - Product pricing with discounts and offers
  - Tax calculations (CGST, SGST, IGST)
  - Cart totals with delivery charges
  - EMI calculation for financing options
- **Security & Data Management**
  - Data encryption for sensitive information
  - Secure SharedPreferences with Gson serialization
  - Network connectivity management
  - File and image processing utilities

#### 🛍️ Product & Shopping Features
- **Product Discovery**
  - Grid-based product display with lazy loading
  - Advanced search with query history
  - Category-based filtering
  - Recently viewed products tracking
- **User Experience**
  - Product ratings and reviews system
  - Wishlist functionality (in development)
  - Cart management with persistent storage
  - User profile management

#### 🏗️ Technical Infrastructure
- **Clean Architecture Implementation**
  - MVVM pattern with ViewModels
  - Repository pattern for data management
  - Use cases for business logic separation
- **Dependency Injection**
  - Hilt for comprehensive DI
  - Scoped ViewModels and repositories
- **Data Management**
  - Firebase Firestore for cloud data
  - Room database for offline support
  - Image loading with Coil library
  - Background sync with WorkManager

### 🔄 In Development
- Cart and checkout system
- Payment integration
- Order tracking
- Push notifications
- Seller dashboard

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose with Material Design 3
- **Architecture**: Clean Architecture + MVVM
- **Build System**: Gradle with Kotlin DSL
- **Target SDK**: Android 35 (API 35)
- **Minimum SDK**: Android 24 (API 24)

### Architecture & Patterns
- **Dependency Injection**: Hilt with `@HiltAndroidApp` and `@HiltViewModel`
- **Navigation**: Jetpack Navigation Compose
- **State Management**: StateFlow and Compose State
- **Reactive Programming**: Kotlin Coroutines + Flow

### Backend & Data
- **Authentication**: Firebase Auth (Email, Phone OTP, Google Sign-In)
- **Cloud Database**: Firebase Firestore
- **Storage**: Firebase Storage for images and files
- **Local Database**: Room for offline support
- **Preferences**: DataStore Preferences
- **Background Tasks**: WorkManager

### UI & Media
- **Image Loading**: Coil for Compose
- **Fonts**: Google Fonts (Poppins)
- **Icons**: Material Icons Extended
- **Theming**: Material 3 with custom color schemes
- **Refresh**: Accompanist Swipe Refresh

### Development Tools
- **JSON Parsing**: Gson
- **Code Generation**: KSP (Kotlin Symbol Processing)
- **Testing**: JUnit, Espresso, Compose Testing
- **Security**: Google Play Integrity API

## 📱 App Architecture

### Package Structure (Clean Architecture)
```
app/src/main/java/com/optivus/bharathaat/
├── BharatHaatApplication.kt    # Hilt Application class
├── MainActivity.kt             # Single Activity entry point
├──
├── constants/                  # App-wide constants
│   ├── AppConstants.kt
│   ├── ValidationConstants.kt
│   ├── ApiEndpoints.kt
│   └── UIConstants.kt
├──
├── data/                       # Data Layer
│   ├── auth/                   # Firebase Auth implementation
│   ├── database/               # Room database setup
│   │   ├── dao/                # Data Access Objects
│   │   └── entities/           # Room entities
│   ├── dto/                    # Data Transfer Objects
│   ├── local/                  # Local data sources
│   │   ├── dao/
│   │   ├── database/
│   │   └── entities/
│   ├── mappers/                # Data mappers
│   ├── models/                 # Data models
│   ├── remote/                 # Remote data sources
│   └── repository/             # Repository implementations
├──
├── di/                         # Dependency Injection modules
├──
├── domain/                     # Domain Layer
│   ├── models/                 # Domain models
│   ├── repository/             # Repository interfaces
│   ├── usecases/               # Business logic use cases
│   └── utils/                  # Domain utilities
├──
├── presentation/               # Presentation Layer
│   ├── screens/                # Screen-specific ViewModels
│   │   ├── auth/
│   │   ├── demo/
│   │   ├── profile/
│   │   └── settings/
│   └── viewmodel/              # Shared ViewModels
├──
├── services/                   # Background services
├──
├── ui/                         # UI Layer
│   ├── auth/                   # Auth-specific UI
│   ├── components/             # Reusable UI components
│   │   ├── buttons/            # Custom button components
│   │   ├── cards/              # Card components
│   │   ├── controls/           # Form controls
│   │   ├── navigation/         # Navigation components
│   │   ├── product/            # Product-related components
│   │   └── textfields/         # Custom text fields
│   ├── navigation/             # Navigation setup
│   ├── screens/                # Screen composables
│   │   ├── auth/               # Authentication screens
│   │   ├── home/               # Home screen
│   │   ├── products/           # Product screens
│   │   ├── profile/            # Profile screens
│   │   └── splash/             # Splash screen
│   ├── theme/                  # App theming
│   └── viewmodels/             # UI ViewModels
└──
└── utils/                      # Utility classes
    ├── ValidationUtils.kt      # Input validation
    ├── CalculationUtils.kt     # Price/tax calculations
    ├── StringUtils.kt          # String operations
    ├── SecurityUtils.kt        # Security operations
    ├── NetworkUtils.kt         # Network utilities
    ├── PreferencesUtils.kt     # Data storage
    └── ... (10+ utility classes)
```

## 🎨 Design Philosophy

- **User-Centric**: Focus on Indian users and shopping patterns
- **Performance**: Optimized for various device capabilities
- **Security**: Comprehensive input validation and data protection
- **Accessibility**: Support for different screen sizes and orientations
- **Offline-First**: Caching and offline functionality where possible

## 🔧 Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2024.1.1) or later
- JDK 11 or higher
- Android SDK with API 35
- Git

### Installation Steps

1. **Clone the repository**
   ```powershell
   git clone <repository-url>
   cd bharat-haat
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned repository folder
   - Wait for Gradle sync to complete

3. **Configure Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app with package name: `com.optivus.bharathaat`
   - Download `google-services.json` and place it in `app/` directory
   - Enable the following in Firebase Console:
     - Authentication (Email/Password, Phone, Google Sign-In)
     - Firestore Database
     - Storage

4. **Build the Project**
   ```powershell
   # Clean build
   .\gradlew clean
   
   # Build debug APK
   .\gradlew assembleDebug
   
   # Install on connected device
   .\gradlew installDebug
   ```

## 🚀 Build Commands

### Development
```powershell
# Build debug APK
.\gradlew assembleDebug

# Build release APK
.\gradlew assembleRelease

# Clean project
.\gradlew clean

# Install debug APK to device
.\gradlew installDebug
```

### Testing
```powershell
# Run unit tests
.\gradlew test

# Run instrumented tests
.\gradlew connectedAndroidTest

# Run specific test class
.\gradlew test --tests="com.optivus.bharathaat.utils.ValidationUtilsTest"

# Generate test coverage report
.\gradlew testDebugUnitTestCoverage
```

### Code Quality
```powershell
# Run lint checks
.\gradlew lint

# Generate lint report
.\gradlew lintDebug

# Format code (if configured)
.\gradlew ktlintFormat
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

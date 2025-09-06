# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

Bharat Haat is an Android ecommerce application built with Jetpack Compose, focusing on Indian handicrafts, traditional products, and local artisan goods. The app follows Clean Architecture principles with MVVM pattern and uses modern Android development practices.

## Build Commands

### Development Environment
```powershell
# Build debug APK
.\gradlew assembleDebug

# Build release APK
.\gradlew assembleRelease

# Clean build
.\gradlew clean

# Install debug APK to device
.\gradlew installDebug
```

### Testing
```powershell
# Run unit tests
.\gradlew test

# Run instrumented tests on connected device
.\gradlew connectedAndroidTest

# Run specific test class
.\gradlew test --tests="com.optivus.bharathaat.utils.ValidationUtilsTest"

# Run tests with coverage
.\gradlew testDebugUnitTestCoverage
```

### Code Quality
```powershell
# Lint check
.\gradlew lint

# Format Kotlin code (if ktlint is configured)
.\gradlew ktlintFormat

# Generate lint report
.\gradlew lintDebug
```

## Architecture Overview

### Clean Architecture Layers
The app follows Clean Architecture with clear separation of concerns:

1. **Presentation Layer** (`presentation/` and `ui/`)
   - ViewModels with Hilt injection
   - Compose UI screens and components
   - State management with StateFlow/LiveData

2. **Domain Layer** (`domain/`)
   - Use cases for business logic
   - Repository interfaces
   - Domain models

3. **Data Layer** (`data/`)
   - Repository implementations
   - Firebase integration (Auth, Firestore, Storage)
   - Room database for offline support
   - Network data sources

### Key Architectural Patterns

#### Dependency Injection with Hilt
- Application class: `BharatHaatApplication` with `@HiltAndroidApp`
- ViewModels use `@HiltViewModel` with constructor injection
- MainActivity uses `@AndroidEntryPoint`
- DI modules located in `di/` package

#### Navigation
- Single Activity architecture with Jetpack Navigation Compose
- Navigation graph defined in `ui/navigation/NavigationGraph.kt`
- Screen routes defined in sealed class structure

#### State Management
```kotlin
// ViewModels use StateFlow for reactive UI updates
private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
val authState: StateFlow<AuthState> = _authState.asStateFlow()
```

## Package Structure

```
app/src/main/java/com/optivus/bharathaat/
├── constants/          # App-wide constants
├── data/              # Data layer (repositories, APIs, database)
│   ├── auth/          # Firebase Auth implementation
│   ├── database/      # Room database entities and DAOs
│   ├── local/         # Local data sources
│   ├── models/        # Data models and DTOs
│   ├── remote/        # Network data sources
│   └── repository/    # Repository implementations
├── di/                # Dependency injection modules
├── domain/            # Domain layer (use cases, interfaces)
│   ├── models/        # Domain models
│   ├── repository/    # Repository interfaces
│   ├── usecases/      # Business logic use cases
│   └── utils/         # Domain utilities
├── presentation/      # Presentation layer
│   ├── screens/       # Screen-level ViewModels and state
│   └── viewmodel/     # Shared ViewModels
├── services/          # Background services
├── ui/                # UI layer
│   ├── auth/          # Authentication UI components
│   ├── components/    # Reusable UI components
│   │   ├── buttons/   # Custom button components
│   │   ├── cards/     # Card components
│   │   ├── textfields/# Custom text field components
│   │   └── ...        # Other component categories
│   ├── navigation/    # Navigation setup
│   ├── screens/       # Screen composables
│   ├── theme/         # App theming
│   └── viewmodels/    # UI-layer ViewModels
└── utils/             # Utility classes
```

## Key Technical Decisions

### Firebase Integration
- Authentication: Email/password, phone number OTP, Google Sign-In
- Firestore: NoSQL database for products, orders, user data
- Storage: Image and file storage
- All Firebase operations include proper error handling and network checks

### Validation & Security
The app has extensive utility classes for validation and security:
- `ValidationUtils`: Comprehensive input validation including Indian-specific formats
- `SecurityUtils`: Rate limiting, session tokens, data encryption
- `NetworkUtils`: Network connectivity management
- All user inputs are sanitized and validated before processing

### State Management Philosophy
- Loading, Success, Error states for all async operations
- Centralized error handling with user-friendly messages
- Rate limiting for authentication to prevent abuse
- Session management with secure token storage

### UI Patterns
- Material Design 3 theming with custom color scheme
- Responsive design supporting phones and tablets
- Custom components for consistent UI/UX
- Error states with retry mechanisms
- Loading states with progress indicators

## Development Guidelines

### Adding New Features
1. Create domain models first in `domain/models/`
2. Define use cases in `domain/usecases/`
3. Implement repository interface in `domain/repository/`
4. Create data implementation in `data/repository/`
5. Build ViewModel in `ui/viewmodels/` or `presentation/viewmodel/`
6. Create UI composables in `ui/screens/`

### Testing Strategy
- Unit tests for ViewModels, Use Cases, and Utilities
- Integration tests for Repository implementations
- UI tests for critical user flows
- Focus on validation logic, business rules, and error handling

### Firebase Configuration
Before running the app:
1. Add `google-services.json` to `app/` directory
2. Enable Authentication in Firebase Console
3. Configure email/password and phone authentication
4. Set up Firestore database with proper security rules

### Utility Usage
The app includes comprehensive utility classes - always use these instead of implementing similar logic:
- Input validation: Always use `ValidationUtils` methods
- String operations: Use `StringUtils` for consistent formatting
- Security operations: Use `SecurityUtils` for tokens, encryption
- Preferences: Use `PreferencesUtils` for consistent data storage
- Network checks: Use `NetworkUtils` before Firebase operations

### Error Handling Pattern
```kotlin
// Always check network before Firebase operations
if (!NetworkUtils.isNetworkAvailable(context)) {
    _state.value = State.Error("No internet connection")
    return
}

// Sanitize inputs
val sanitizedInput = StringUtils.trimAndClean(userInput)

// Validate using utility
val error = ValidationUtils.getEmailError(sanitizedInput)
if (error != null) {
    _state.value = State.Error(error)
    return
}

// Implement rate limiting for auth operations
if (SecurityUtils.isRateLimited(context, key, maxAttempts, timeWindow)) {
    _state.value = State.Error("Too many attempts. Try later.")
    return
}
```

## Firebase Security Rules
The project includes `firestore.rules` for database security. Always test security rules in Firebase Console before deploying.

## Performance Considerations
- Image loading with Coil for optimal performance
- Room database for offline functionality
- WorkManager for background sync
- Lazy loading for large product lists
- Proper state hoisting to avoid unnecessary recompositions

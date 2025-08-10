# Bharat Haat - E-commerce Android App

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-purple.svg" alt="Language">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg" alt="UI Framework">
  <img src="https://img.shields.io/badge/Version-1.0-orange.svg" alt="Version">
  <img src="https://img.shields.io/badge/License-MIT-red.svg" alt="License">
  <img src="https://img.shields.io/badge/Repository-GitHub-black.svg" alt="Repository">
</div>

## 📱 Overview

**Bharat Haat** is a modern Android e-commerce application built with Jetpack Compose, designed to showcase authentic Indian products from local artisans and businesses. The app provides a seamless shopping experience with a focus on Indian craftsmanship and culture.

**Repository**: [https://github.com/Hey-Viswa/bharat-haat.git](https://github.com/Hey-Viswa/bharat-haat.git)

### 🎯 Key Features

- **Modern UI/UX**: Built entirely with Jetpack Compose for a smooth, native Android experience
- **Comprehensive Component Library**: 17+ specialized UI components across 8 categories
- **Clean Architecture**: Proper separation of concerns with data and domain layers
- **User Authentication**: Secure login/signup flow with Firebase integration
- **Product Display**: Interactive galleries, size charts, color variants, and ratings
- **Navigation System**: Advanced breadcrumbs, search bars, and filtering
- **Responsive Design**: Optimized for various screen sizes and orientations
- **Material Design 3**: Following Google's latest design guidelines

## 🛠️ Technical Stack

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern Android UI toolkit
- **Clean Architecture** - Data, Domain, and UI layer separation
- **Navigation Compose** - Type-safe navigation
- **Material Design 3** - Google's design system

### Libraries & Dependencies
- **Firebase** - Authentication and analytics (google-services.json configured)
- **Coil** - Image loading and caching
- **Material Icons Extended** - Comprehensive icon set
- **Compose BOM** - Bill of Materials for consistent Compose versions

### Build Configuration
- **Compile SDK**: 36
- **Target SDK**: 36
- **Min SDK**: 24 (Android 7.0+)
- **Java Version**: 11
- **Kotlin Version**: 2.0.21
- **AGP Version**: 8.12.0

## 📁 Project Architecture

### Clean Architecture Structure
```
app/src/main/java/com/optivus/bharat_haat/
├── MainActivity.kt                        # Application entry point
├── data/                                  # Data Layer
│   ├── local/                            # Local data sources
│   ├── remote/                           # Remote data sources (APIs, Firebase)
│   ├── repository/                       # Repository implementations  
│   ├── dto/                              # Data Transfer Objects
│   ├── database/                         # Room database configuration
│   │   ├── entities/                     # Database entities
│   │   └── dao/                          # Data Access Objects
│   └── mappers/                          # Data-Domain mappers
├── domain/                               # Domain Layer (Business Logic)
│   ├── models/                           # Business entities
│   ├── repository/                       # Repository interfaces
│   ├── usecases/                         # Use cases (business rules)
│   └── utils/                            # Domain utilities
└── ui/                                   # Presentation Layer
    ├── components/                       # Reusable UI Components (17 files)
    │   ├── buttons/                      # 4 specialized button components
    │   ├── cards/                        # Product and e-commerce cards
    │   ├── checkout/                     # Checkout flow components
    │   ├── controls/                     # Input controls
    │   ├── filters/                      # Filtering components
    │   ├── informational/                # Info display components
    │   ├── lists/                        # Product list components
    │   ├── navigation/                   # Navigation components
    │   ├── product/                      # Product display components
    │   ├── promotional/                  # Promotional components
    │   └── textfields/                   # Custom text input fields
    ├── navigation/                       # App navigation logic
    ├── screens/                          # App screens
    │   ├── splash/                       # Animated splash screen
    │   ├── onboarding/                   # Welcome flow
    │   └── auth/                         # Login/signup screens
    └── theme/                            # Material 3 theming
```

## 🏗️ Component Library

The app includes a comprehensive UI component library with 17+ components:

### 🔘 Buttons (4 components)
- **CustomButton**: Base customizable button
- **EcommerceButtons**: Add to cart, wishlist, buy now
- **SignInButton**: Social authentication buttons
- **SpecializedButtons**: Filter, sort, and action buttons

### 📱 Cards & Display
- **ProductCards**: Product showcase with ratings and pricing
- **EcommerceCards**: Category cards, promotional banners
- **ProductDisplayComponents**: Image galleries, size charts, color variants
- **InformationalComponents**: Help sections, FAQ displays

### 🛒 E-commerce Features
- **CheckoutComponents**: Payment flow and order summary
- **FilterComponents**: Advanced product filtering
- **ProductLists**: Grid and list view layouts
- **NavigationComponents**: Breadcrumbs, search, and tab navigation

### 📝 Input Components
- **CustomTextField**: Styled form inputs with validation
- **OtpTextField**: OTP verification interface
- **SpecializedTextFields**: Search bars, tag inputs
- **InputControls**: Sliders, toggles, and selections

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or higher
- Android SDK with API level 36
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Hey-Viswa/bharat-haat.git
   cd bharat-haat
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Firebase Setup** (Already configured)
   - The project includes `google-services.json`
   - Firebase Authentication and Analytics are pre-configured
   - Build and run to test Firebase connectivity

4. **Build and Run**
   ```bash
   ./gradlew build
   ```
   - Connect an Android device or start an emulator
   - Click the Run button in Android Studio

## 🌿 Branch Structure

The repository maintains two main branches:

- **`master`** - Production-ready stable code
- **`stable-ui-phase1`** - UI development and component library

## 🎨 UI Highlights

### Authentication Flow
- **Splash Screen**: Animated introduction with brand identity
- **Onboarding**: Multi-screen welcome experience
- **Login/Signup**: Firebase-powered secure authentication

### Product Experience
- **Image Galleries**: Zoomable product images with thumbnails
- **Size & Color Selection**: Interactive variant selection
- **Rating System**: Star ratings with review counts
- **Advanced Search**: Text search with category filters

### Navigation
- **Breadcrumb Navigation**: Hierarchical path tracking
- **Tab System**: Category-based content organization
- **Search Integration**: Real-time search with suggestions

## 📊 Performance Features

- **Lazy Loading**: Efficient memory usage with lazy lists
- **Image Caching**: Coil-powered image optimization
- **Smooth Animations**: 60fps Material Motion animations
- **Responsive Layout**: Adaptive UI for all screen sizes

## 🧪 Development Workflow

### Current Development Phase
✅ **Phase 1 Complete**: Core UI component library  
✅ **Navigation System**: Complete app navigation flow  
✅ **Authentication UI**: Login/signup interface  
🔄 **Phase 2 In Progress**: Business logic implementation  

### Next Steps
- [ ] Repository pattern implementation
- [ ] Use case development for business logic
- [ ] API integration for product data
- [ ] Local database setup with Room
- [ ] Payment gateway integration

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch** from `stable-ui-phase1`
   ```bash
   git checkout -b feature/your-feature-name stable-ui-phase1
   ```
3. **Make your changes** and add tests
4. **Commit with clear messages**
5. **Push to your fork** and create a Pull Request to `stable-ui-phase1`

### Development Guidelines
- Follow Kotlin coding conventions
- Use meaningful component and function names
- Add KDoc comments for public APIs
- Test components in isolation
- Follow Material Design 3 guidelines

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Lead Developer**: [Hey-Viswa](https://github.com/Hey-Viswa)
- **Repository**: [bharat-haat](https://github.com/Hey-Viswa/bharat-haat.git)

## 🙏 Acknowledgments

- **Material Design 3**: Google's design system
- **Jetpack Compose**: Modern Android UI toolkit  
- **Firebase**: Authentication and backend services
- **Coil**: Efficient image loading
- **Indian Artisan Community**: Inspiration and purpose

## 📞 Support

For questions, issues, or contributions:
- **GitHub Issues**: [Report bugs or request features](https://github.com/Hey-Viswa/bharat-haat/issues)
- **Discussions**: [Join project discussions](https://github.com/Hey-Viswa/bharat-haat/discussions)

---

<div align="center">
  <p>🇮🇳 Made with ❤️ for Indian artisans and craftspeople 🇮🇳</p>
  <p>© 2025 Bharat Haat. Supporting local businesses and authentic Indian culture.</p>
</div>

# ☕ Coffee For Me - Premium Coffee Ordering App

<p align="center">
  <img src="screenshots/app_logo.png" alt="Coffee For Me Logo" width="200"/>
</p>

<p align="center">
  <strong>A modern, elegant coffee ordering application built with Jetpack Compose and Firebase</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-blue.svg" alt="Language"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg" alt="UI Framework"/>
  <img src="https://img.shields.io/badge/Backend-Firebase-yellow.svg" alt="Backend"/>
  <img src="https://img.shields.io/badge/Architecture-MVVM-red.svg" alt="Architecture"/>
</p>

## 📱 Screenshots

| Splash Screen | Home Screen | Coffee Details | Cart Screen |
|:---:|:---:|:---:|:---:|
| ![Splash](screenshots/splash_screen.png) | ![Home](screenshots/home_screen.png) | ![Details](screenshots/coffee_details.png) | ![Cart](screenshots/cart_screen.png) |

| Login Screen | Sign Up | Profile | Coffee Categories |
|:---:|:---:|:---:|:---:|
| ![Login](screenshots/login_screen.png) | ![SignUp](screenshots/signup_screen.png) | ![Profile](screenshots/profile_screen.png) | ![Categories](screenshots/categories.png) |

## ✨ Features

### 🔐 Authentication & User Management
- **Firebase Authentication** with email/password
- **User Registration** with profile creation
- **Login/Logout** functionality
- **Forgot Password** with email reset
- **Profile Management** with user data persistence

### ☕ Coffee Shopping Experience
- **Coffee Catalog** with beautiful product displays
- **Category Filtering** (Espresso, Cappuccino, Latte, Americano, Hot Chocolate)
- **Detailed Coffee Information** with descriptions, ratings, and pricing
- **High-Quality Images** with smooth loading via Coil
- **Popular Coffee Section** highlighting trending items

### 🛒 Shopping Cart & Orders
- **Add to Cart** with quantity selection
- **Size Selection** (Small, Medium, Large)
- **Cart Management** (add, remove, update quantities)
- **Real-time Price Calculation**
- **Cart Persistence** across app sessions
- **Firebase Cart Sync** for logged-in users

### 🎨 Modern UI/UX Design
- **Material 3 Design System** with coffee-themed colors
- **Warm Color Palette** (Amber yellows, coffee browns, cream backgrounds)
- **Responsive Layouts** optimized for different screen sizes
- **Smooth Animations** and transitions
- **Intuitive Navigation** with bottom navigation and screen transitions

### 🔥 Firebase Integration
- **Realtime Database** for coffee data and user carts
- **Authentication Service** for secure user management
- **Cloud Data Sync** ensuring data consistency across devices
- **Offline Support** with local data caching

## 🏗️ Architecture

### Project Structure
```
app/
├── src/main/java/com/coffeeforme/giggo/
│   ├── data/
│   │   ├── model/           # Data models (Coffee, CartItem, User)
│   │   └── repository/      # Data access layer
│   ├── presentation/
│   │   ├── screen/          # UI Screens (Compose)
│   │   └── viewmodel/       # ViewModels (MVVM)
│   ├── service/             # Firebase services
│   ├── ui/theme/           # Theme, colors, typography
│   ├── navigation/         # Navigation components
│   ├── MainActivity.kt     # Main activity
│   └── CoffeeApplication.kt # Application class
├── assets/
│   └── database.json       # Local coffee data
└── res/                    # Android resources
```

### Technology Stack
- **Language**: Kotlin 1.8.22
- **UI Framework**: Jetpack Compose
- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase (Authentication + Realtime Database)
- **Image Loading**: Coil Compose
- **Serialization**: Kotlinx Serialization
- **Coroutines**: Kotlin Coroutines for async operations
- **Navigation**: Jetpack Navigation Compose

### Key Components

#### 🎯 ViewModels
- **AuthViewModel**: Handles user authentication state
- **FirebaseCoffeeViewModel**: Manages coffee data and cart operations
- **CoffeeViewModel**: Local coffee data management

#### 🔥 Firebase Services
- **FirebaseAuthService**: Authentication operations
- **FirebaseDatabaseService**: Database operations
- **Real-time Sync**: Automatic data synchronization

#### 📱 Screens
- **SplashScreen**: App launch screen
- **LoginScreen**: User authentication
- **SignUpScreen**: User registration
- **HomeScreen**: Main coffee catalog
- **CoffeeDetailScreen**: Detailed product view
- **CartScreen**: Shopping cart management
- **ProfileScreen**: User profile management

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Arctic Fox or newer
- **JDK 8** or higher
- **Android SDK** (API level 24+)
- **Firebase Project** setup

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/coffee-for-me.git
   cd coffee-for-me
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned repository folder

3. **Firebase Setup**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app to your Firebase project
   - Download `google-services.json` and place it in `app/` directory
   - Enable Authentication and Realtime Database

4. **Configure Firebase**
   ```json
   // Update google-services.json with your project details
   {
     "project_info": {
       "project_id": "your-project-id",
       "project_number": "your-project-number"
     }
   }
   ```

5. **Build and Run**
   ```bash
   ./gradlew clean build
   ./gradlew installDebug
   ```

### Firebase Database Structure
```json
{
  "coffee_data": {
    "banners": [...],
    "categories": [...],
    "coffee": [...],
    "popular": [...]
  },
  "users": {
    "userId": {
      "profile": {
        "name": "User Name",
        "email": "user@example.com"
      },
      "cart": {
        "coffeeId": {
          "coffeeItem": {...},
          "quantity": 2,
          "selectedSize": "Medium"
        }
      }
    }
  }
}
```

## 🧪 Demo Data

### Test Credentials
For testing purposes, you can use these demo credentials:

```
Email: demo@coffeefor.me
Password: password123
```

### Sample Coffee Data
The app comes with pre-loaded coffee data including:
- **5 Categories**: Espresso, Cappuccino, Latte, Americano, Hot Chocolate
- **Multiple Coffee Items** with detailed descriptions
- **High-quality Images** and ratings
- **Varied Pricing** from $3.50 to $6.99

## 🛠️ Configuration

### Build Configuration
```kotlin
// app/build.gradle.kts
android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.coffeeforme.coffeee"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
```

### Theme Customization
The app uses a warm, coffee-inspired color palette:

```kotlin
// Primary Colors - Coffee/Yellow theme
val Primary = Color(0xFFF59E0B) // Warm amber yellow
val Secondary = Color(0xFF92400E) // Coffee brown
val Background = Color(0xFFFEF3C7) // Warm cream
```

## 📊 Performance & Optimization

### Image Loading
- **Coil Integration** for efficient image loading
- **Caching Strategy** to reduce network calls
- **Placeholder Images** for better UX

### Data Management
- **Local Caching** of coffee data
- **Firebase Offline Support**
- **Efficient State Management** with StateFlow

### Memory Optimization
- **Lazy Loading** of images and data
- **Proper Lifecycle Management**
- **Memory-efficient ViewModels**

## 🔧 Troubleshooting

### Common Issues

1. **Build Errors**
   ```bash
   # Clean and rebuild
   ./gradlew clean build
   
   # Check Java version compatibility
   java -version  # Should be JDK 8+
   ```

2. **Firebase Connection Issues**
   - Verify `google-services.json` is in the correct location
   - Check Firebase project configuration
   - Ensure internet permissions are granted

3. **Image Loading Problems**
   - Check network connectivity
   - Verify image URLs in database.json
   - Review Coil configuration

### Debug Mode
Enable debug logging for detailed information:
```kotlin
// In CoffeeApplication.kt
override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
        // Enable debug logging
    }
}
```

## 🤝 Contributing

This is my college project so sorry for contributers.

### Code Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Maintain consistent formatting

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

```
MIT License

Copyright (c) 2025 Coffee For Me

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

## 👥 Authors

- **Kamlesh Kumar Sah**  - [ visnusah](https://github.com/visnusah)

## 🙏 Acknowledgments

- **Module Leader** for guidance and support  
- **Softwarica College** for resources and encouragement  
- **Community contributors** from Reddit, Stack Overflow, and DailyDev for helpful discussions and solutions  
- **Softwarica** for 
- **Firebase** for backend services
- **Jetpack Compose** for modern UI development
- **Coffee lovers** worldwide for inspiration

## 📞 Support

For support, email support@coffeefor.me or join our Slack channel.

## 🔮 Future Enhancements

- [ ] **Order History** tracking
- [ ] **Push Notifications** for order updates
- [ ] **Payment Integration** (Stripe/PayPal)
- [ ] **Location Services** for nearby coffee shops
- [ ] **Social Features** (reviews, ratings)
- [ ] **Loyalty Program** with rewards
- [ ] **Dark Mode** support
- [ ] **Multilingual** support
- [ ] **Coffee Customization** (milk type, sugar level)
- [ ] **Delivery Tracking** integration

---

<p align="center">
  Made with ❤️ and ☕ by the Coffee For Me Team
</p>

<p align="center">
  <a href="#top">Back to top</a>
</p>

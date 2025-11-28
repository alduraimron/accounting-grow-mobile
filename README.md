
# Accounting Grow - Personal Finance Management App

A modern Android application for managing personal finances, built with Jetpack Compose and Clean Architecture principle.

## Backend

GitHub: https://github.com/alduraimron/accounting-grow-backend.git 

## 🛠️ Tech Stack

### Core
- **Kotlin 2.1.0** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - UI components

### Architecture & DI
- **Clean Architecture** - Separation of concerns
- **MVVM Pattern** - ViewModel-based architecture
- **Hilt 2.52** - Dependency injection

## 📋 Requirements

- **Android Studio** Hedgehog (2023.1.1) or newer
- **Gradle** 8.7.3
- **JDK** 17
- **Min SDK** 24 (Android 7.0)
- **Target SDK** 35 (Android 15)

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/accounting-grow.git
cd accounting-grow
```

### 2. Configure Backend URL

Update the `BASE_URL` in `di/NetworkModule.kt`:
```kotlin
private const val BASE_URL = "http://YOUR_IP_ADDRESS:3000/"
```
### 3. Network Security Configuration

The app is configured to allow cleartext traffic for development. See `res/xml/network_security_config.xml`.

**For production**, update to use HTTPS

### 4. Build and Run
```bash
./gradlew assembleDebug
```

Or run directly from Android Studio.



# Quick Setup Guide for Coffee For Me App

## 🚀 Demo Data Setup

### Demo User Credentials
Use these credentials to test the app:

```
📧 Main Demo User:
Email: demo@coffeefor.me
Password: password123

👤 Admin User:
Email: admin@coffeefor.me  
Password: admin123

🏃 Regular User:
Email: user@coffeefor.me
Password: user123
```

### Coffee Menu Preview
The app includes these coffee varieties:

#### ☕ Popular Coffees
1. **Cappuccino** - $4.50 ⭐ 4.6
   - Traditional Italian coffee with espresso, steamed milk, and foam
   
2. **Espresso** - $3.50 ⭐ 4.0
   - Concentrated coffee served in small, strong shots
   
3. **Caffe Latte** - $5.00 ⭐ 4.7
   - Perfect balance of espresso and creamy steamed milk

#### 🎯 Full Menu
4. **Americano** - $3.80 ⭐ 4.2
5. **Hot Chocolate** - $4.20 ⭐ 4.4
6. **Macchiato** - $4.80 ⭐ 4.3
7. **Cafe Mocha** - $5.50 ⭐ 4.8
8. **Frappuccino** - $6.00 ⭐ 4.5

### Categories
- 🔥 Espresso
- ☕ Cappuccino  
- 🥛 Latte
- 💧 Americano
- 🍫 Hot Chocolate

## 📱 App Features to Test

### Authentication Flow
1. **Splash Screen** → **Login Screen**
2. Test login with demo credentials
3. Try "Forgot Password" functionality
4. Test new user registration

### Shopping Experience
1. **Browse Coffee** → View categories and items
2. **Coffee Details** → Tap any coffee for detailed view
3. **Add to Cart** → Select size and quantity
4. **Cart Management** → Update quantities, remove items
5. **Profile** → View and edit user information

### Firebase Features
1. **Real-time Sync** → Cart updates across sessions
2. **User Persistence** → Login state maintained
3. **Data Sync** → Coffee catalog from Firebase

## 🔧 Development Testing

### Local Testing
```bash
# Clean build
./gradlew clean build

# Install on device/emulator
./gradlew installDebug

# View logs
adb logcat | grep -i "coffee\|firebase"
```

### Firebase Console
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your "Coffee For Me" project
3. Check **Authentication** → Users (should show demo users)
4. Check **Realtime Database** → Data (should show coffee_data and users)

### Demo Data Verification
Look for these database nodes:
```
/coffee_data/
├── banners/
├── categories/
├── coffee/
└── popular/

/users/
└── {userId}/
    ├── profile/
    └── cart/
```

## 📊 Testing Checklist

### ✅ Core Functionality
- [ ] App launches without crashes
- [ ] Login with demo credentials works
- [ ] Coffee catalog displays properly
- [ ] Images load correctly
- [ ] Add to cart functionality works
- [ ] Cart persists across app restarts
- [ ] Profile screen shows user data
- [ ] Logout functionality works

### ✅ UI/UX Testing
- [ ] Coffee theme colors display correctly
- [ ] Navigation between screens is smooth
- [ ] Text is readable and properly styled
- [ ] Buttons and touch targets respond
- [ ] Loading states show appropriately
- [ ] Error states handle gracefully

### ✅ Firebase Integration
- [ ] User authentication works
- [ ] Real-time database syncs
- [ ] Cart data persists in Firebase
- [ ] Offline mode functions
- [ ] Data loads from Firebase

## 🐛 Common Issues & Solutions

### Build Errors
```bash
# Java version mismatch
# Solution: Ensure using Java 8
java -version

# Clean gradle cache
./gradlew clean
rm -rf .gradle/
```

### Firebase Connection
```bash
# Check google-services.json
# Should be in app/ directory
ls -la app/google-services.json

# Verify Firebase project settings
# Package name should match: com.coffeeforme.coffeee
```

### Image Loading Issues
```bash
# Check network connectivity
# Verify image URLs in database.json
# Clear app cache if needed
adb shell pm clear com.coffeeforme.coffeee
```

## 📈 Performance Notes

### Expected Behavior
- **App Launch**: < 3 seconds
- **Image Loading**: Progressive with placeholders
- **Navigation**: Smooth transitions
- **Data Sync**: Background operation
- **Memory Usage**: Optimized for mobile

### Monitoring
```bash
# Monitor memory usage
adb shell dumpsys meminfo com.coffeeforme.coffeee

# Check network requests
# Use Android Studio Network Inspector
```

## 🎯 Demo Presentation Tips

### Showcase Features
1. **Start with splash screen** - Shows branding
2. **Demo authentication** - Use provided credentials
3. **Browse coffee catalog** - Highlight UI design
4. **Add items to cart** - Show interaction
5. **View profile** - Demonstrate user management
6. **Show Firebase sync** - Real-time updates

### Talking Points
- **Modern UI** with Jetpack Compose
- **Firebase Integration** for real-time data
- **MVVM Architecture** for maintainability
- **Coffee-themed Design** with warm colors
- **Responsive Layout** for different screen sizes

---

**Happy Testing! ☕**

For issues, check the main README.md or create an issue in the repository.

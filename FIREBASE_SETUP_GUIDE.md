# Firebase Setup Guide for Coffee For Me App

## Prerequisites
1. Google account
2. Android Studio with this project opened
3. Internet connection

## Step-by-Step Firebase Setup

### 1. Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project" or "Add project"
3. Enter project name: `coffee-for-me` (or your preferred name)
4. Enable Google Analytics (optional)
5. Click "Create project"

### 2. Add Android App to Firebase Project
1. In Firebase Console, click "Add app" and select Android
2. Enter package name: `com.coffeeforme.coffeee`
3. Enter app nickname: `Coffee For Me`
4. Leave SHA-1 empty for now (can add later for advanced features)
5. Click "Register app"

### 3. Download and Add Configuration File
1. Download the `google-services.json` file
2. Copy it to your app module folder: `app/google-services.json`
3. Replace the template file `app/google-services.json.template`

### 4. Enable Firebase Services
1. **Authentication:**
   - Go to Authentication > Get started
   - Click "Sign-in method" tab
   - Enable "Email/Password" provider
   - Click "Save"

2. **Realtime Database:**
   - Go to Realtime Database > Create database
   - Choose location (closest to your users)
   - Start in "Test mode" (you can change rules later)
   - Click "Done"

### 5. Upload Initial Data (Optional)
Your app can work with the local JSON data initially. The Firebase integration will:
- Sync local data to Firebase when users are authenticated
- Use Firebase data when available
- Fall back to local data when offline

### 6. Database Structure
The app will create this structure in Firebase Realtime Database:
```
{
  "coffee_data": {
    "banners": [...],
    "categories": [...],
    "coffee": [...],
    "popular": [...]
  },
  "users": {
    "USER_ID": {
      "profile": {
        "name": "...",
        "email": "...",
        "lastUpdated": timestamp
      },
      "cart": {
        "ITEM_ID": {
          "coffeeItem": {...},
          "quantity": number,
          "selectedSize": "..."
        }
      }
    }
  }
}
```

### 7. Security Rules (Recommended)
Update your Realtime Database rules for security:
```json
{
  "rules": {
    "coffee_data": {
      ".read": true,
      ".write": "auth != null"
    },
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    }
  }
}
```

### 8. Testing
1. Build and run the app
2. Create a new account or login
3. Add items to cart
4. Check Firebase Console to see data sync

## Features Included

### Firebase Authentication
- Email/password signup and login
- User profile management
- Display name updates
- Automatic authentication state management

### Firebase Realtime Database
- Real-time cart synchronization across devices
- User profile storage
- Coffee data synchronization
- Offline support with local fallback

### Smart Data Management
- Local JSON data as fallback
- Automatic data upload to Firebase
- Real-time updates
- Cross-device synchronization

## Troubleshooting

### Common Issues
1. **App crashes on startup:**
   - Ensure `google-services.json` is in the correct location
   - Check package name matches exactly

2. **Authentication not working:**
   - Verify Email/Password provider is enabled
   - Check internet connection

3. **Database permission denied:**
   - Update security rules as shown above
   - Ensure user is authenticated

4. **Data not syncing:**
   - Check Firebase Console for errors
   - Verify database URL is correct

### Build Issues
If you encounter build issues:
1. Clean and rebuild project
2. Check all Firebase dependencies are properly added
3. Ensure google-services plugin is applied

## Next Steps
1. Set up proper security rules for production
2. Add password reset functionality
3. Implement order history
4. Add push notifications for order updates
5. Set up Firebase Analytics for user behavior tracking

## Support
If you need help:
1. Check Firebase documentation
2. Review Android Firebase guides
3. Check the troubleshooting section above

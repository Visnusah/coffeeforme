# Screenshot Guide for Coffee For Me App

This guide will help you take professional screenshots for the README file.

## 📸 Required Screenshots

### 1. Main App Screenshots
- `splash_screen.png` - App launch screen
- `home_screen.png` - Main coffee catalog view
- `coffee_details.png` - Detailed coffee product view
- `cart_screen.png` - Shopping cart with items
- `login_screen.png` - User login interface
- `signup_screen.png` - User registration screen
- `profile_screen.png` - User profile management
- `categories.png` - Coffee categories view

### 2. Optional Screenshots
- `app_logo.png` - App logo (200x200px recommended)
- `forgot_password.png` - Password reset screen
- `empty_cart.png` - Empty cart state
- `coffee_grid.png` - Coffee grid layout
- `search_results.png` - Search functionality

## 📱 Screenshot Specifications

### Recommended Settings
- **Device**: Use Android emulator or physical device
- **Resolution**: 1080x1920 (Full HD) or higher
- **Format**: PNG (for transparency support)
- **Orientation**: Portrait mode
- **Quality**: High quality/lossless

### Device Frames (Optional)
For professional presentation, consider adding device frames:
- Use tools like [Device Art Generator](https://developer.android.com/distribute/marketing-tools/device-art-generator)
- Or [MockDrop](https://mockdrop.io/) for modern device frames

## 🎯 Screenshot Taking Tips

### 1. Preparation
```bash
# Enable demo mode (if available)
adb shell settings put global sysui_demo_allowed 1

# Set consistent time
adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1015

# Hide notifications
adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false
```

### 2. Content Guidelines
- Use demo data with appealing coffee images
- Ensure cart has 2-3 items for demonstration
- Show realistic user profile information
- Use consistent lighting and image quality

### 3. UI Elements to Highlight
- **Color Theme**: Showcase the warm coffee/yellow theme
- **Navigation**: Show bottom navigation clearly
- **Interactive Elements**: Buttons, cards, and touch targets
- **Typography**: Clear, readable text
- **Loading States**: Show shimmer effects if present

## 📐 Image Dimensions

### README Table Images
- **Width**: 250-300px
- **Height**: 400-500px (maintain aspect ratio)
- **Format**: PNG with transparent backgrounds if needed

### Hero Image
- **App Logo**: 200x200px, PNG with transparent background
- **Banner**: 1200x400px for social media sharing

## 🔧 Screenshot Tools

### Android Studio
1. Open AVD Manager
2. Start emulator
3. Run the app
4. Use camera icon in emulator controls

### ADB Commands
```bash
# Take screenshot via ADB
adb exec-out screencap -p > screenshot.png

# Take screenshot of specific activity
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png
```

### Physical Device
- Use device's screenshot functionality (Power + Volume Down)
- Transfer via USB or cloud storage

## 🖼️ Image Optimization

### Compression
```bash
# Using ImageOptim (macOS)
imageoptim *.png

# Using TinyPNG (online)
# Upload to https://tinypng.com/

# Using pngquant (command line)
pngquant --quality=65-90 screenshot.png
```

### Batch Processing
```bash
# Resize all screenshots to consistent width
for file in *.png; do
    convert "$file" -resize 300x -quality 90 "resized_$file"
done
```

## 📋 Screenshot Checklist

### Before Taking Screenshots
- [ ] App is in demo mode with sample data
- [ ] UI theme is consistent (coffee colors)
- [ ] Text is readable and professional
- [ ] No debug information visible
- [ ] Consistent status bar appearance
- [ ] Good contrast and lighting

### Content Verification
- [ ] Coffee images are high quality
- [ ] Prices are realistic ($3.50 - $6.99)
- [ ] User information is appropriate
- [ ] Cart contains sample items
- [ ] No empty or error states (unless intentional)

### Technical Quality
- [ ] High resolution (1080p minimum)
- [ ] PNG format for best quality
- [ ] Proper aspect ratio maintained
- [ ] No compression artifacts
- [ ] Consistent device frame (if used)

## 🎨 Styling Guidelines

### Color Consistency
Ensure screenshots show the app's coffee theme:
- Primary: Warm amber yellow (#F59E0B)
- Secondary: Coffee brown (#92400E)
- Background: Warm cream (#FEF3C7)

### Typography
- Clear, readable fonts
- Proper contrast ratios
- Consistent text sizes

### Layout
- Proper spacing and margins
- Aligned elements
- Professional appearance

## 📤 Final Steps

1. **Review**: Check all screenshots meet quality standards
2. **Rename**: Use consistent naming convention
3. **Optimize**: Compress while maintaining quality
4. **Upload**: Place in `/screenshots/` directory
5. **Update**: Verify README.md links work correctly

## 🔗 Helpful Resources

- [Android Design Guidelines](https://developer.android.com/design)
- [Material Design Screenshots](https://material.io/design/communication/imagery.html)
- [Google Play Screenshots Guide](https://support.google.com/googleplay/android-developer/answer/1078870)
- [App Store Screenshot Guidelines](https://developer.apple.com/app-store/product-page/)

---

Remember: High-quality screenshots significantly impact user perception and GitHub repository professionalism!

package com.coffeeforme.coffeee.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object FirebaseTest : Screen("firebase_test")
}

object NavigationRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"
    const val HOME = "home"
    const val CART = "cart"
    const val PROFILE = "profile"
    const val FIREBASE_TEST = "firebase_test"
}

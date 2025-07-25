package com.coffeeforme.coffeee.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffeeforme.coffeee.presentation.screen.ForgotPasswordScreen
import com.coffeeforme.coffeee.presentation.screen.LoginScreen
import com.coffeeforme.coffeee.presentation.screen.SignUpScreen
import com.coffeeforme.coffeee.presentation.screen.SplashScreen
import com.coffeeforme.coffeee.presentation.viewmodel.AuthViewModel
import com.coffeeforme.coffeee.ui.theme.GigGOTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    GigGOTheme {
        SplashScreen(
            onNavigateToLogin = {},
            onNavigateToHome = {},
            isAuthenticated = false
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    GigGOTheme {
        LoginScreen(
            authViewModel = viewModel(),
            onNavigateToSignUp = {},
            onNavigateToForgotPassword = {},
            onNavigateToHome = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    GigGOTheme {
        SignUpScreen(
            authViewModel = viewModel(),
            onNavigateToLogin = {},
            onNavigateToHome = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForgotPasswordScreenPreview() {
    GigGOTheme {
        ForgotPasswordScreen(
            onNavigateBack = {}
        )
    }
}

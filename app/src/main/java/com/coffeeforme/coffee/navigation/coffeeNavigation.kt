package com.coffeeforme.coffeee.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coffeeforme.coffeee.presentation.screen.*
import com.coffeeforme.coffeee.presentation.viewmodel.AuthViewModel
import com.coffeeforme.coffeee.presentation.viewmodel.CoffeeViewModel
import com.coffeeforme.coffeee.presentation.viewmodel.CoffeeViewModelFactory
@Composable
fun GigGoNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    coffeeViewModel: CoffeeViewModel
) {
    val context = LocalContext.current
    val application = context.applicationContext as android.app.Application
    val coffeeViewModel: CoffeeViewModel = viewModel(factory = CoffeeViewModelFactory(application))
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                isAuthenticated = isLoggedIn
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                coffeeViewModel = coffeeViewModel
            )
        }
        
        composable(Screen.Cart.route) {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                coffeeViewModel = coffeeViewModel
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToFirebaseTest = {
                    navController.navigate(Screen.FirebaseTest.route)
                },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.FirebaseTest.route) {
            FirebaseTestScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

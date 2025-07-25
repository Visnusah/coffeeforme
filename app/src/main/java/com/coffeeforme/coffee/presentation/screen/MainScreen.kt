package com.coffeeforme.coffeee.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coffeeforme.coffeee.navigation.GigGoNavigation
import com.coffeeforme.coffeee.presentation.components.CoffeeBottomNavigation
import com.coffeeforme.coffeee.presentation.viewmodel.AuthViewModel
import com.coffeeforme.coffeee.presentation.viewmodel.CoffeeViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = viewModel(),
    coffeeViewModel: CoffeeViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // List of routes that should show bottom navigation
    val bottomNavRoutes = listOf("home", "cart", "profile")
    val showBottomNav = currentRoute in bottomNavRoutes
    
    val cartItemCount by remember {
        derivedStateOf { coffeeViewModel.getCartItemCount() }
    }
    
    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                CoffeeBottomNavigation(
                    currentRoute = currentRoute ?: "",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Avoid multiple copies of the same destination
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    cartItemCount = cartItemCount
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(if (showBottomNav) paddingValues else PaddingValues())) {
            GigGoNavigation(
                navController = navController,
                authViewModel = authViewModel,
                coffeeViewModel = coffeeViewModel
            )
        }
    }
}

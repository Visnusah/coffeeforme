package com.example.coffeeforme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coffeeforme.database.CoffeeDatabase
import com.example.coffeeforme.navigation.Screen
import com.example.coffeeforme.repository.ProductRepositoryImpl
import com.example.coffeeforme.repository.UserRepositoryImpl
import com.example.coffeeforme.ui.screens.*
import com.example.coffeeforme.ui.theme.CoffeeformeTheme
import com.example.coffeeforme.viewmodel.ProductViewModel
import com.example.coffeeforme.viewmodel.UserViewModel
import com.example.coffeeforme.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeformeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CoffeeApp()
                }
            }
        }
    }
}

@Composable
fun CoffeeApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    
    // Initialize database and repositories
    val database = remember { CoffeeDatabase.getDatabase(context) }
    val userRepository = remember { UserRepositoryImpl(database.userDao()) }
    val productRepository = remember { ProductRepositoryImpl(database.productDao()) }
    val viewModelFactory = remember { ViewModelFactory(userRepository, productRepository) }
    
    // Create default admin user if not exists
    LaunchedEffect(Unit) {
        scope.launch {
            val existingUser = userRepository.getUserByEmail("admin@coffee.com")
            if (existingUser == null) {
                userRepository.insertUser(
                    com.example.coffeeforme.model.UserModel(
                        name = "Admin",
                        email = "admin@coffee.com",
                        password = "admin123"
                    )
                )
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }
        
        composable(Screen.Signup.route) {
            val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)
            SignupScreen(
                userViewModel = userViewModel,
                onSignupSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Dashboard.route) {
            val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)
            DashboardScreen(
                userViewModel = userViewModel,
                onNavigateToProducts = {
                    navController.navigate(Screen.ProductList.route)
                },
                onLogout = {
                    userViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.ProductList.route) {
            val productViewModel: ProductViewModel = viewModel(factory = viewModelFactory)
            ProductListScreen(
                productViewModel = productViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAddProduct = {
                    navController.navigate(Screen.AddProduct.route)
                },
                onNavigateToEditProduct = { productId ->
                    navController.navigate(Screen.EditProduct.createRoute(productId))
                }
            )
        }
        
        composable(Screen.AddProduct.route) {
            val productViewModel: ProductViewModel = viewModel(factory = viewModelFactory)
            AddProductScreen(
                productViewModel = productViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            Screen.EditProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val productViewModel: ProductViewModel = viewModel(factory = viewModelFactory)
            
            // For simplicity, we'll reuse AddProductScreen with edit functionality
            // In a real app, you'd create a separate EditProductScreen
            AddProductScreen(
                productViewModel = productViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

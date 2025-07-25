package com.coffeeforme.coffeee.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeeforme.coffeee.presentation.components.GigGoButton
import com.coffeeforme.coffeee.presentation.components.GigGoTextField
import com.coffeeforme.coffeee.presentation.viewmodel.AuthViewModel
import com.coffeeforme.coffeee.ui.theme.Gray500
import com.coffeeforme.coffeee.ui.theme.Primary
import com.coffeeforme.coffeee.ui.theme.PrimaryLight

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val email by authViewModel.loginEmail.collectAsState()
    val password by authViewModel.loginPassword.collectAsState()
    val isError = authState.error != null
    
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            onNavigateToHome()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Primary.copy(alpha = 0.1f),
                        Color.White
                    )
                )
            )
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // Logo Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Primary, PrimaryLight)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "☕",
                    fontSize = 48.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Welcome Text
            Text(
                text = "Welcome Back!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Sign in to Coffee For Me",
                fontSize = 16.sp,
                color = Gray500,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Login Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Email Field
                    GigGoTextField(
                        value = email,
                        onValueChange = authViewModel::updateLoginEmail,
                        label = "Email Address",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Email,
                        leadingIcon = Icons.Default.Email,
                        isError = isError
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Password Field
                    GigGoTextField(
                        value = password,
                        onValueChange = authViewModel::updateLoginPassword,
                        label = "Password",
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = true,
                        leadingIcon = Icons.Default.Lock,
                        isError = isError
                    )
                    
                    // Error Message
                    authState.error?.let { error ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Forgot Password
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Forgot Password?",
                        color = Primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToForgotPassword() }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Login Button
                    GigGoButton(
                        text = "Sign In",
                        onClick = { authViewModel.login(onNavigateToHome) },
                        isLoading = authState.isLoading,
                        enabled = email.isNotBlank() && password.isNotBlank()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Sign Up Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Gray500,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign Up",
                    color = Primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

package com.coffeeforme.coffeee.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.coffeeforme.coffeee.ui.theme.Gray500
import com.coffeeforme.coffeee.ui.theme.Primary
import com.coffeeforme.coffeee.ui.theme.PrimaryLight
import com.coffeeforme.coffeee.ui.theme.Success

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isEmailSent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    fun handleSendResetLink() {
        if (email.isBlank()) {
            errorMessage = "Please enter your email address"
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Please enter a valid email address"
            return
        }
        
        isLoading = true
        errorMessage = null
        
        // Simulate API call
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            isLoading = false
            isEmailSent = true
        }, 2000)
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
        // Back Button
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            
            if (isEmailSent) {
                // Success State
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Success, Success.copy(alpha = 0.8f))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MarkEmailRead,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Check Your Email",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "We've sent a password reset link to",
                    fontSize = 16.sp,
                    color = Gray500,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = email,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Please check your email and click on the link to reset your password. The link will expire in 24 hours.",
                    fontSize = 14.sp,
                    color = Gray500,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                GigGoButton(
                    text = "Back to Login",
                    onClick = onNavigateBack
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                GigGoButton(
                    text = "Resend Email",
                    onClick = {
                        isEmailSent = false
                        handleSendResetLink()
                    },
                    backgroundColor = Color.Transparent,
                    contentColor = Primary
                )
                
            } else {
                // Initial State
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Primary, PrimaryLight)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Forgot Password?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Don't worry! Enter your email address and we'll send you a link to reset your password.",
                    fontSize = 16.sp,
                    color = Gray500,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        val isError = false
                        GigGoTextField(
                            value = email,
                            onValueChange = { 
                                email = it
                                errorMessage = null
                            },
                            label = "Email Address",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Email,
                            leadingIcon = Icons.Default.Email,
                            errorMessage = errorMessage,
                            isError = isError
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        GigGoButton(
                            text = "Send Reset Link",
                            onClick = { handleSendResetLink() },
                            isLoading = isLoading,
                            enabled = email.isNotBlank()
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

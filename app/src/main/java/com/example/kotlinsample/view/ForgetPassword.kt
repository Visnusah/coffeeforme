package com.example.kotlinsample.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinsample.R
import com.example.kotlinsample.view.ui.theme.KotlinsampleTheme
import com.google.firebase.auth.FirebaseAuth

// Yellowish color scheme
private val Yellow = Color(0xFFFFD600)
private val YellowDark = Color(0xFFFFB300)
private val YellowLight = Color(0xFFFFF9C4)
private val BentoBg = Color(0xFFFFF8E1)
private val BentoShadow = Color(0x33FFD600)
private val BentoAccent = Color(0xFFFFE082)
private val TextOnYellow = Color(0xFF3E2723)
private val ErrorRed = Color(0xFFE53935)
private val White = Color(0xFFFFFFFF)
private val LightGray = Color(0xFFF5F5F5)

class ForgetPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinsampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    PasswordResetScreen(
                        onSubmit = { email ->
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Reset email sent to $email", Toast.LENGTH_LONG).show()
                                    } else {
                                        val error = task.exception?.message ?: "Failed to send reset email"
                                        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PasswordResetScreen(
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Yellow,
                        YellowLight
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header with back button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { (context as? ComponentActivity)?.finish() },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.2f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = YellowDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Main content card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo image in a bento block
                    Card(
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(8.dp, CircleShape),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = YellowLight)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.img),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Title
                    Text(
                        text = "Forgot Password?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = YellowDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Subtitle
                    Text(
                        text = "No worries! Enter your email and we'll send you reset instructions.",
                        fontSize = 16.sp,
                        color = TextOnYellow.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Email input field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                "Email Address",
                                color = TextOnYellow.copy(alpha = 0.7f),
                                fontSize = 16.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = if (isEmailValid) YellowDark else TextOnYellow.copy(alpha = 0.5f)
                            )
                        },
                        singleLine = true,
                        isError = email.isNotEmpty() && !isEmailValid,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = YellowDark,
                            unfocusedBorderColor = Yellow,
                            focusedTextColor = TextOnYellow,
                            unfocusedTextColor = TextOnYellow,
                            cursorColor = YellowDark,
                            focusedLabelColor = YellowDark,
                            unfocusedLabelColor = Yellow,
                            focusedContainerColor = BentoBg,
                            unfocusedContainerColor = BentoBg
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            color = TextOnYellow
                        )
                    )

                    if (email.isNotEmpty() && !isEmailValid) {
                        Text(
                            text = "Please enter a valid email address",
                            color = ErrorRed,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Submit button
                    Button(
                        onClick = {
                            isLoading = true
                            onSubmit(email)
                            isLoading = false
                        },
                        enabled = isEmailValid && !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = YellowDark,
                            disabledContainerColor = YellowLight
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Send Reset Link",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = TextOnYellow
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Additional info
                    Text(
                        text = "Remember your password?",
                        fontSize = 14.sp,
                        color = TextOnYellow.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Sign in here",
                        fontSize = 14.sp,
                        color = YellowDark,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { (context as? ComponentActivity)?.finish() }
                            .padding(top = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordResetScreenPreview() {
    KotlinsampleTheme {
        PasswordResetScreen(
            onSubmit = { email -> }
        )
    }
}

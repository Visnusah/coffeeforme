package com.example.kotlinsample.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinsample.R
import com.example.kotlinsample.repository.UserRepositoryImpl
import com.example.kotlinsample.ui.theme.KotlinsampleTheme
import com.example.kotlinsample.viewmodel.UserViewModel
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.draw.shadow

class SkillsLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinsampleTheme {
                SkillsSewaLoginScreen {
                    startActivity(Intent(this, signup::class.java))
                }
            }
        }
    }
}

@Composable
fun SkillsSewaLoginScreen(
    onSignupClick: () -> Unit = {}
) {
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)

    // Modern yellowish color palette
    val yellow = Color(0xFFFFD600)
    val yellowDark = Color(0xFFFFB300)
    val yellowLight = Color(0xFFFFF9C4)
    val bentoBg = Color(0xFFFFF8E1)
    val bentoShadow = Color(0x33FFD600)
    val bentoAccent = Color(0xFFFFE082)
    val textOnYellow = Color(0xFF3E2723)

    LaunchedEffect(Unit) {
        val savedEmail = sharedPref.getString("email", "") ?: ""
        val savedPassword = sharedPref.getString("password", "") ?: ""
        val rememberMePref = sharedPref.getBoolean("remember_me", false)
        if (rememberMePref && savedEmail.isNotEmpty() && savedPassword.isNotEmpty())  {
            email = savedEmail
            password = savedPassword
            rememberMe = true
            isLoading = true
            userViewModel.login(savedEmail, savedPassword) { success, message ->
                isLoading = false
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                if (success) {
                    context.startActivity(Intent(context, NavigationActivity::class.java))
                }
            }
        }
    }

    // Modern bento-style background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(yellow, yellowLight)
                )
            )
    ) {
        // Decorative bento blocks
        Box(
            modifier = Modifier
                .size(180.dp, 120.dp)
                .offset(x = 30.dp, y = 0.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(bentoAccent)
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .size(120.dp, 80.dp)
                .offset(x = (-20).dp, y = 500.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(bentoShadow)
                .align(Alignment.BottomEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Logo in a bento block
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .background(bentoBg)
                    .padding(16.dp)
                    .shadow(8.dp, RoundedCornerShape(32.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Coffeeforme Logo",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                "Coffeeforme",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = yellowDark,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "“Coffee is the best thing to warm you up”",
                fontSize = 16.sp,
                color = textOnYellow,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Bento login card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(width = 1.dp, color = yellow, shape = RoundedCornerShape(16.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            )
             // Box(
            //     modifier = Modifier
            //         .fillMaxWidth()
            //         .padding(16.dp)
            //         .background(Color.White)
            // )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Sign In",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = yellowDark
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = textOnYellow) },
                        placeholder = { Text("your@email.com", color = yellowLight) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = yellowDark,
                            unfocusedBorderColor = yellow,
                            focusedTextColor = textOnYellow,
                            unfocusedTextColor = textOnYellow,
                            cursorColor = yellowDark,
                            focusedLabelColor = yellowDark,
                            unfocusedLabelColor = yellow,
                            focusedContainerColor = bentoBg,
                            unfocusedContainerColor = bentoBg
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = textOnYellow) },
                        placeholder = { Text("********", color = yellowLight) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    icon,
                                    contentDescription = if (showPassword) "Hide Password" else "Show Password",
                                    tint = yellowDark
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = yellowDark,
                            unfocusedBorderColor = yellow,
                            focusedTextColor = textOnYellow,
                            unfocusedTextColor = textOnYellow,
                            cursorColor = yellowDark,
                            focusedLabelColor = yellowDark,
                            unfocusedLabelColor = yellow,
                            focusedContainerColor = bentoBg,
                            unfocusedContainerColor = bentoBg
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = yellowDark,
                                uncheckedColor = yellow
                            )
                        )
                        Text("Remember Me", color = yellowDark, fontSize = 15.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Forgot Password?",
                            color = yellowDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(context, ForgetPassword::class.java)
                                    context.startActivity(intent)
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    if (isLoading) {
                        CircularProgressIndicator(color = yellowDark)
                    } else {
                        Button(
                            onClick = {
                                isLoading = true
                                userViewModel.login(email, password) { success, message ->
                                    isLoading = false
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    if (success) {
                                        if (rememberMe) {
                                            sharedPref.edit().putString("email", email)
                                                .putString("password", password)
                                                .putBoolean("remember_me", true)
                                                .apply()
                                        } else {
                                            sharedPref.edit().clear().apply()
                                        }
                                        context.startActivity(Intent(context, NavigationActivity::class.java))
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = yellowDark),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Login", color = textOnYellow, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("New user? ", color = textOnYellow, fontWeight = FontWeight.Medium)
                Text(
                    text = "Signup here",
                    color = yellowDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onSignupClick)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreviewBody() {
    KotlinsampleTheme {
        SkillsSewaLoginScreen()
    }
}

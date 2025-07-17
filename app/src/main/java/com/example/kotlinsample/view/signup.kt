package com.example.kotlinsample.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinsample.model.UserModel
import com.example.kotlinsample.repository.UserRepositoryImpl
import com.example.kotlinsample.ui.theme.KotlinsampleTheme
import com.example.kotlinsample.viewmodel.UserViewModel
import java.util.*

class signup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinsampleTheme {
                SignUpScreen()
            }
        }
    }
}

@Composable
fun SignUpScreen() {
    // Color palette matching skilsslogin.kt
    val yellow = Color(0xFFFFD600)
    val yellowDark = Color(0xFFFFB300)
    val yellowLight = Color(0xFFFFF9C4)
    val bentoBg = Color(0xFFFFF8E1)
    val bentoShadow = Color(0x33FFD600)
    val bentoAccent = Color(0xFFFFE082)
    val textOnYellow = Color(0xFF3E2723)

    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dob = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val genderOptions = listOf("Male", "Female", "Other")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(yellow, yellowLight)
                )
            )
    ) {
        // Decorative bento blocks (simple, subtle)
        Box(
            modifier = Modifier
                .size(120.dp, 80.dp)
                .offset(x = 24.dp, y = 0.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(bentoAccent)
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .size(80.dp, 60.dp)
                .offset(x = (-16).dp, y = 520.dp)
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
            // Icon in a bento block
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .background(bentoBg)
                    .padding(16.dp)
                    .shadow(8.dp, RoundedCornerShape(32.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Sign Up Icon",
                    tint = yellowDark,
                    modifier = Modifier
                        .size(64.dp)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                "Create Account",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = yellowDark,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Join us and enjoy your coffee journey!",
                fontSize = 16.sp,
                color = textOnYellow,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Bento signup card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(width = 1.dp, color = yellow, shape = RoundedCornerShape(12.dp))
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .padding(all = 12.dp) // Equal padding on all 4 sides
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Sign Up",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = yellowDark
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name", color = textOnYellow) },
                        placeholder = { Text("Your Name", color = yellowLight) },
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
                    Spacer(modifier = Modifier.height(12.dp))
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
                    Spacer(modifier = Modifier.height(12.dp))
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
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = dob,
                        onValueChange = {},
                        label = { Text("Date of Birth", color = textOnYellow) },
                        placeholder = { Text("DD/MM/YYYY", color = yellowLight) },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { datePickerDialog.show() },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = textOnYellow,
                            disabledContainerColor = bentoBg,
                            disabledLabelColor = yellowDark,
                            disabledBorderColor = yellow
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Select Gender",
                        style = MaterialTheme.typography.bodyMedium.copy(color = textOnYellow)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        genderOptions.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (gender == option) yellowLight else Color.Transparent
                                    )
                                    .clickable { gender = option }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (gender == option),
                                    onClick = { gender = option },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = yellowDark,
                                        unselectedColor = yellow
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(option, color = textOnYellow)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    if (isLoading) {
                        CircularProgressIndicator(color = yellowDark)
                    } else {
                        Button(
                            onClick = {
                                if (name.isBlank() || email.isBlank() || password.isBlank() || dob.isBlank() || gender.isBlank()) {
                                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                isLoading = true
                                userViewModel.register(email, password) { success, message, userId ->
                                    isLoading = false
                                    if (success) {
                                        val user = UserModel(
                                            name = name.trim(),
                                            email = email,
                                            password = password,
                                            dob = dob,
                                            gender = gender
                                        )
                                        userViewModel.addUserToDatabase(userId, user) { successDb, messageDb ->
                                            Toast.makeText(context, messageDb, Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = yellowDark),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Sign Up", color = textOnYellow, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

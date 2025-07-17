package com.example.kotlinsample.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinsample.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import java.util.*

// Yellowish bento style colors
private val CoffeeYellow = Color(0xFFFFE082)
private val CoffeeBrown = Color(0xFF795548)
private val CoffeeLight = Color(0xFFFFF8E1)
private val CoffeeDark = Color(0xFF4E342E)

class UserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = CoffeeYellow,
                    onPrimary = CoffeeDark,
                    background = CoffeeLight,
                    surface = CoffeeYellow,
                    onSurface = CoffeeDark
                )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CoffeeLight),
                    color = CoffeeLight
                ) {
                    EditUserProfileScreen()
                }
            }
        }
    }
}

@Composable
fun EditUserProfileScreen() {
    val context = LocalContext.current
    val repo = remember { UserRepositoryImpl() }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("hello@reallygreatsite.com") }
    var dateOfBirth by remember { mutableStateOf("Select") }

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateOfBirth = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoffeeLight)
            .padding(horizontal = 28.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Your Profile",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = CoffeeDark,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = CoffeeYellow,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(horizontal = 28.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            LabelText("Full Name")
            RoundedInputField(
                value = name,
                onValueChange = { name = it }
            )

            LabelText("Email")
            RoundedInputField(
                value = email,
                onValueChange = { email = it }
            )

            LabelText("Date of Birth")
            RoundedInputField(
                value = dateOfBirth,
                onValueChange = {},
                readOnly = true,
                onClick = { datePicker.show() }
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                if (userId.isEmpty()) {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val userMap = mutableMapOf<String, Any>(
                    "name" to name,
                    "email" to email,
                    "dob" to dateOfBirth
                )

                repo.editProfile(userId, userMap) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) (context as? ComponentActivity)?.finish()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                "UPDATE",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 22.sp,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun LabelText(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 16.sp,
        color = CoffeeDark,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 2.sp,
        modifier = Modifier.padding(bottom = 2.dp)
    )
}

@Composable
fun RoundedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
                else Modifier
            )
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            enabled = true,
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxSize(),
            textStyle = TextStyle(fontSize = 20.sp, color = CoffeeDark),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = CoffeeBrown,
                unfocusedBorderColor = CoffeeYellow,
                cursorColor = CoffeeBrown,
                focusedTextColor = CoffeeDark,
                unfocusedTextColor = CoffeeDark,
                disabledTextColor = CoffeeDark
            )
        )
    }
}

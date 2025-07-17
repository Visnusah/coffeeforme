package com.example.kotlinsample.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinsample.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth

// Yellowish bento style colors
private val CoffeeYellow = Color(0xFFFFE082)
private val CoffeeBrown = Color(0xFF795548)
private val CoffeeLight = Color(0xFFFFF8E1)
private val CoffeeDark = Color(0xFF4E342E)

class UserProfileViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            finish()
            return
        }

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
                    ViewUserProfileScreen(userId = currentUserId)
                }
            }
        }
    }
}

@Composable
fun ViewUserProfileScreen(userId: String) {
    val repo = remember { UserRepositoryImpl() }
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }
    var dob by remember { mutableStateOf("Loading...") }
    var gender by remember { mutableStateOf("Loading...") }

    LaunchedEffect(userId) {
        repo.getProfile(userId) { data ->
            fullName = data["name"] as? String ?: "N/A"
            email = data["email"] as? String ?: "N/A"
            dob = data["dob"] as? String ?: "N/A"
            gender = data["gender"] as? String ?: "N/A"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CoffeeLight)
            .padding(horizontal = 24.dp, vertical = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Profile Icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(CoffeeYellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = CoffeeBrown,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            // Name
            Text(
                text = fullName,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = CoffeeDark
            )
            // Email
            Text(
                text = email,
                fontSize = 16.sp,
                color = CoffeeBrown,
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            // Card for details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CoffeeYellow),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InfoRowSimple(label = "Date of Birth", value = dob)
                    InfoRowSimple(label = "Gender", value = gender)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Edit Profile Button
            Button(
                onClick = {
                    context.startActivity(Intent(context, UserProfileActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
            ) {
                Text("Edit Profile", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Logout Button
            Button(
                onClick = {
                    logoutAndReturnToLogin(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Logout", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun InfoRowSimple(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = CoffeeDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = CoffeeBrown,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

fun logoutAndReturnToLogin(context: Context) {
    FirebaseAuth.getInstance().signOut()
    val sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    val intent = Intent(context, SkillsLoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

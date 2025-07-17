package com.example.kotlinsample.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kotlinsample.R
import com.example.kotlinsample.ui.theme.KotlinsampleTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlin.jvm.java

class Splash : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinsampleTheme {
                Scaffold { innerPadding ->
                    SplashScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun SplashScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current
    val activity = context as Activity

    var hasNavigated by remember { mutableStateOf(false) }

    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "splash_anim")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "logo_scale"
    )
    val colorAnim by infiniteTransition.animateColor(
        initialValue = Color(0xFFFFF8E1),
        targetValue = Color(0xFFFFECB3),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bg_color"
    )
    val dotOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "dot_offset"
    )

    LaunchedEffect(Unit) {
        delay(3000L)

        val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("email", "")
        val savedPassword = sharedPref.getString("password", "")
        val currentUser = FirebaseAuth.getInstance().currentUser

        Log.d("Splash", "SavedEmail: $savedEmail | currentUser: $currentUser")

        if (!hasNavigated) {
            hasNavigated = true
            when {
                currentUser != null -> {
                    // Firebase says user is already authenticated
                    context.startActivity(Intent(context, NavigationActivity::class.java))
                }
                !savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty() -> {
                    // Credentials are remembered, go to login screen which auto-fills & logs in
                    context.startActivity(Intent(context, SkillsLoginActivity::class.java))
                }
                else -> {
                    // No user, go to login screen
                    context.startActivity(Intent(context, SkillsLoginActivity::class.java))
                }
            }
            activity.finish()
        }
    }

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(colorAnim)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Animated logo with scale
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Animated bouncing dots below the progress indicator
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(32.dp)
            ) {
                for (i in 0..2) {
                    val thisDotOffset = if (i == 1) dotOffset else 0f
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .offset(y = thisDotOffset.dp)
                            .background(
                                color = Color(0xFFFFC107),
                                shape = CircleShape
                            )
                    )
                    if (i != 2) Spacer(modifier = Modifier.width(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Progress indicator
            CircularProgressIndicator(
                color = Color(0xFFFFC107),
                strokeWidth = 4.dp,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
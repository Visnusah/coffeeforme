package com.coffeeforme.coffeee.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffeeforme.coffeee.presentation.viewmodel.FirebaseAuthViewModel
import com.coffeeforme.coffeee.ui.theme.*
import com.coffeeforme.coffeee.util.DataMigrationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseTestScreen(
    onNavigateBack: () -> Unit,
    firebaseAuthViewModel: FirebaseAuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val migrationHelper = remember { DataMigrationHelper(context) }
    
    var statusMessage by remember { mutableStateOf("Ready to test Firebase") }
    var isLoading by remember { mutableStateOf(false) }
    
    val isLoggedIn by firebaseAuthViewModel.isLoggedIn.collectAsState()
    val currentUser by firebaseAuthViewModel.currentUser.collectAsState()
    val userName by firebaseAuthViewModel.currentUserName.collectAsState()
    val userEmail by firebaseAuthViewModel.currentUserEmail.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding()
    ) {
        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Primary,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                Text(
                    text = "Firebase Testing",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Status",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = statusMessage,
                        fontSize = 14.sp,
                        color = if (isLoggedIn) Primary else Color.Gray
                    )
                    
                    if (isLoading) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Primary
                        )
                    }
                }
            }
            
            // User Info Card
            if (isLoggedIn) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Current User",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Name: $userName",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        
                        Text(
                            text = "Email: $userEmail",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        
                        Text(
                            text = "UID: ${currentUser?.uid ?: "N/A"}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            // Test Buttons
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Firebase Tests",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    
                    // Test Authentication
                    if (!isLoggedIn) {
                        Button(
                            onClick = {
                                isLoading = true
                                statusMessage = "Testing authentication..."
                                firebaseAuthViewModel.login("test@coffee.com", "password123") { success, message ->
                                    isLoading = false
                                    statusMessage = if (success) "✅ Authentication successful!" else "❌ Auth failed: $message"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isLoading
                        ) {
                            Text("Test Login", color = Color.White)
                        }
                        
                        Button(
                            onClick = {
                                isLoading = true
                                statusMessage = "Creating test account..."
                                firebaseAuthViewModel.signUp("Coffee Tester", "test@coffee.com", "password123") { success, message ->
                                    isLoading = false
                                    statusMessage = if (success) "✅ Account created!" else "❌ Signup failed: $message"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isLoading
                        ) {
                            Text("Create Test Account", color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = {
                                firebaseAuthViewModel.logout()
                                statusMessage = "Logged out successfully"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Logout", color = Color.White)
                        }
                    }
                    
                    // Test Database Upload
                    Button(
                        onClick = {
                            isLoading = true
                            statusMessage = "Uploading coffee data to Firebase..."
                            migrationHelper.uploadLocalDataToFirebase { success, message ->
                                isLoading = false
                                statusMessage = if (success) "✅ Data uploaded!" else "❌ Upload failed: $message"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Tertiary),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading
                    ) {
                        Text("Upload Coffee Data", color = Color.White)
                    }
                    
                    // Check Firebase Data
                    Button(
                        onClick = {
                            isLoading = true
                            statusMessage = "Checking Firebase database..."
                            migrationHelper.checkFirebaseData { hasData ->
                                isLoading = false
                                statusMessage = if (hasData) "✅ Firebase has data!" else "❌ No data in Firebase"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading
                    ) {
                        Text("Check Database", color = Color.White)
                    }
                }
            }
            
            // Instructions Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Instructions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = """
                        🚧 Firebase Implementation Status:
                        
                        Currently using STUB implementations for:
                        • Authentication (always succeeds)
                        • Database operations (local only)
                        • Data migration (simulated)
                        
                        To implement Firebase:
                        1. Create a Firebase project at console.firebase.google.com
                        2. Add your Android app to the project
                        3. Download and replace google-services.json
                        4. Enable Authentication and Realtime Database
                        5. Replace stub services with actual Firebase implementations
                        
                        The buttons above will work with stub data until Firebase is properly configured.
                        """.trimIndent(),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

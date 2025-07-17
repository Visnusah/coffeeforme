package com.example.coffeeforme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeeforme.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userViewModel: UserViewModel = viewModel(),
    onNavigateToProducts: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFFF9800).copy(alpha = 0.1f))
                )
            )
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Coffee For Me",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            actions = {
                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFFF9800)
            )
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Welcome Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "User",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Welcome back!",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = currentUser?.name ?: "Coffee Lover",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF1976D2)
                        )
                    }
                }
            }
            
            // Quick Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    title = "Total Products",
                    value = "0",
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                DashboardCard(
                    title = "Categories",
                    value = "5",
                    color = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Main Actions
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(top = 16.dp)
            )
            
            // Product Management Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToProducts() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "☕",
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Manage Products",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add, edit, and manage your coffee products",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // App Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
            ) {
                Text(
                    text = "Coffee For Me - Your Personal Coffee Management App\nVersion 1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

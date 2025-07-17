package com.example.kotlinsample.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Yellowish bento style colors (should match your theme)
private val CoffeeYellow = Color(0xFFFFE082)
private val CoffeeBrown = Color(0xFF795548)
private val CoffeeDark = Color(0xFF4E342E)

@Composable
fun SearchScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CoffeeYellow),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .background(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 32.dp, vertical = 40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Empty Cart",
                tint = CoffeeBrown,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "No items added yet",
                color = CoffeeDark,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your cart is empty.\nStart adding your favorite coffee!",
                color = CoffeeBrown,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        }
    }
}

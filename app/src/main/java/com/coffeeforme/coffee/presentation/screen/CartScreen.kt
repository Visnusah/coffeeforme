package com.coffeeforme.coffeee.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffeeforme.coffeee.presentation.components.CartItemCard
import com.coffeeforme.coffeee.presentation.components.GigGoButton
import com.coffeeforme.coffeee.presentation.viewmodel.CoffeeViewModel
import com.coffeeforme.coffeee.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    coffeeViewModel: CoffeeViewModel = viewModel()
) {
    val cartItems by coffeeViewModel.cartItems.collectAsState()
    val totalPrice by remember {
        derivedStateOf { coffeeViewModel.getTotalPrice() }
    }
    
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
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "My Cart",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                if (cartItems.isNotEmpty()) {
                    IconButton(
                        onClick = { coffeeViewModel.clearCart() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear cart",
                            tint = Color.White
                        )
                    }
                }
            }
        }
        
        if (cartItems.isEmpty()) {
            // Empty Cart State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "☕",
                        fontSize = 64.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Your cart is empty",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Add some delicious coffee to get started!",
                        fontSize = 16.sp,
                        color = Gray600,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    GigGoButton(
                        text = "Browse Coffee",
                        onClick = onNavigateBack,
                        backgroundColor = Primary
                    )
                }
            }
        } else {
            // Cart Content
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Cart Items
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            onQuantityChange = { newQuantity ->
                                coffeeViewModel.updateQuantity(cartItem, newQuantity)
                            },
                            onRemove = {
                                coffeeViewModel.removeFromCart(cartItem)
                            }
                        )
                    }
                }
                
                // Bottom Summary
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Surface,
                    shadowElevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Order Summary
                        Text(
                            text = "Order Summary",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Subtotal (${cartItems.sumOf { it.quantity }} items)",
                                fontSize = 14.sp,
                                color = Gray600
                            )
                            Text(
                                text = "$${String.format("%.2f", totalPrice)}",
                                fontSize = 14.sp,
                                color = OnSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Delivery Fee",
                                fontSize = 14.sp,
                                color = Gray600
                            )
                            Text(
                                text = "$2.50",
                                fontSize = 14.sp,
                                color = OnSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Divider(color = Gray300)
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                text = "$${String.format("%.2f", totalPrice + 2.50)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Checkout Button
                        GigGoButton(
                            text = "Proceed to Checkout",
                            onClick = {
                                // TODO: Implement checkout
                            },
                            backgroundColor = Primary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
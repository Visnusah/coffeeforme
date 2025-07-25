package com.coffeeforme.coffeee.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffeeforme.coffeee.data.model.Category
import com.coffeeforme.coffeee.presentation.components.*
import com.coffeeforme.coffeee.presentation.viewmodel.CoffeeViewModel
import com.coffeeforme.coffeee.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    coffeeViewModel: CoffeeViewModel = viewModel()
) {
    val coffeeDatabase by coffeeViewModel.coffeeDatabase.collectAsState()
    val selectedCategory by coffeeViewModel.selectedCategory.collectAsState()
    val cartItemCount by remember {
        derivedStateOf { coffeeViewModel.getCartItemCount() }
    }
    val isLoading by coffeeViewModel.isLoading.collectAsState()
    
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good Morning ☕",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Coffee For Me",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Icon
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                    
                    // Cart Icon
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(
                                    containerColor = Error
                                ) {
                                    Text(
                                        text = cartItemCount.toString(),
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onNavigateToCart) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else if (coffeeDatabase != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Banner
                item {
                    coffeeDatabase?.Banner?.firstOrNull()?.let { banner ->
                        BannerImage(
                            imageUrl = banner.url,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                // Categories Section
                item {
                    Text(
                        text = "Categories",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Popular category
                        item {
                            PopularCoffeeChip(
                                isSelected = selectedCategory == -1,
                                onClick = { coffeeViewModel.selectCategory(-1) }
                            )
                        }
                        
                        // Regular categories
                        items(coffeeDatabase?.Category ?: emptyList()) { category ->
                            CategoryChip(
                                category = category,
                                isSelected = selectedCategory == category.id,
                                onClick = { coffeeViewModel.selectCategory(category.id) }
                            )
                        }
                    }
                }
                
                // Coffee Items Section
                item {
                    val sectionTitle = if (selectedCategory == -1) "Popular Coffees" else {
                        coffeeDatabase?.Category?.find { it.id == selectedCategory }?.title ?: "Coffee"
                    }
                    
                    Text(
                        text = sectionTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    val filteredItems = coffeeViewModel.getFilteredItems()
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.height((filteredItems.size / 2 + filteredItems.size % 2) * 280.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredItems) { coffee ->
                            CoffeeCard(
                                coffeeItem = coffee,
                                onAddToCart = { 
                                    coffeeViewModel.addToCart(coffee)
                                },
                                onClick = {
                                    // TODO: Navigate to coffee details
                                }
                            )
                        }
                    }
                }
            }
        } else {
            // Error State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Failed to load coffee data",
                        fontSize = 18.sp,
                        color = Error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    GigGoButton(
                        text = "Retry",
                        onClick = { /* TODO: Retry logic */ }
                    )
                }
            }
        }
    }
}

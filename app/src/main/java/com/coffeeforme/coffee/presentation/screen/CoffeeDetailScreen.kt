package com.coffeeforme.coffeee.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.coffeeforme.coffeee.data.model.CoffeeItem
import com.coffeeforme.coffeee.presentation.components.GigGoButton
import com.coffeeforme.coffeee.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailScreen(
    coffeeItem: CoffeeItem,
    onNavigateBack: () -> Unit,
    onAddToCart: (String, Int) -> Unit
) {
    var selectedSize by remember { mutableStateOf("Medium") }
    var quantity by remember { mutableIntStateOf(1) }
    
    val sizes = listOf("Small", "Medium", "Large")
    val sizeMultipliers = mapOf("Small" to 0.8, "Medium" to 1.0, "Large" to 1.2)
    
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
                    text = coffeeItem.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Coffee Image
            item {
                AsyncImage(
                    model = coffeeItem.picUrl.firstOrNull(),
                    contentDescription = coffeeItem.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Coffee Info
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = coffeeItem.title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Warning,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = coffeeItem.rating.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = OnSurface
                            )
                            
                            Spacer(modifier = Modifier.weight(1f))
                            
                            Text(
                                text = "$${String.format("%.2f", coffeeItem.price * sizeMultipliers[selectedSize]!!)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Ingredients",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurface
                        )
                        
                        Text(
                            text = coffeeItem.extra,
                            fontSize = 14.sp,
                            color = Gray600,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
            
            // Description
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Description",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = coffeeItem.description,
                            fontSize = 14.sp,
                            color = Gray700,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            // Size Selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Size",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            sizes.forEach { size ->
                                FilterChip(
                                    onClick = { selectedSize = size },
                                    label = {
                                        Text(
                                            text = size,
                                            color = if (selectedSize == size) Color.White else OnSurface
                                        )
                                    },
                                    selected = selectedSize == size,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Primary,
                                        containerColor = Surface
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
            
            // Quantity Selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Quantity",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                enabled = quantity > 1
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Decrease",
                                    tint = if (quantity > 1) Primary else Gray400
                                )
                            }
                            
                            Text(
                                text = quantity.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            
                            IconButton(
                                onClick = { quantity++ }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Increase",
                                    tint = Primary
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Bottom Add to Cart Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Surface,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Price",
                            fontSize = 14.sp,
                            color = Gray600
                        )
                        Text(
                            text = "$${String.format("%.2f", coffeeItem.price * sizeMultipliers[selectedSize]!! * quantity)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                    
                    GigGoButton(
                        text = "Add to Cart",
                        onClick = { onAddToCart(selectedSize, quantity) },
                        backgroundColor = Primary,
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
        }
    }
}

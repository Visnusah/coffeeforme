package com.example.kotlinsample.view.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

// --- Dummy Data Section (from database.json) ---

data class Banner(val url: String)
data class Category(val id: Int, val title: String)
data class PopularCoffee(
    val title: String,
    val description: String,
    val extra: String,
    val picUrl: List<String>,
    val price: Double,
    val rating: Double
)

// Dummy data (normally loaded from assets/database.json)
private val banners = listOf(
    Banner("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598162/banner_pnixuo.png")
)
private val categories = listOf(
    Category(0, "Esspersso"),
    Category(1, "Cappuccino"),
    Category(2, "Latte"),
    Category(3, "Americano"),
    Category(4, "Hot Chocolate")
)
private val popularCoffees = listOf(
    PopularCoffee(
        title = "Cappoccino",
        description = "Cappuccino is a traditional Italian coffee drink made with equal parts espresso, steamed milk, and milk foam. It has a strong espresso flavor, balanced by the creamy texture of the steamed milk and the light, airy foam on top. Cappuccinos are often garnished with a sprinkle of cocoa powder or cinnamon.",
        extra = "Essperso,Milk",
        picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598189/4_vcxvtv.png"),
        price = 4.5,
        rating = 4.6
    ),
    PopularCoffee(
        title = "Espersso",
        description = "Espresso is a concentrated form of coffee served in small, strong shots and is the base for many coffee drinks. It is made by forcing a small amount of nearly boiling water through finely-ground coffee beans under high pressure. The result is a rich, robust flavor with a thick, creamy layer on top called crema.",
        extra = "Espersso",
        picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598194/5_gdnijm.png"),
        price = 3.5,
        rating = 4.0
    ),
    PopularCoffee(
        title = "Affagato",
        description = "Espresso is a concentrated form of coffee served in small,strongshots and is the base for many coffee drinks. It is made by forcing a small amount of nearly boiling water through finely-ground coffee beans under high pressure.The result is a rich, robust flavor with a thick, creamy layer on top called crema",
        extra = "Espersso,milk,IceCream",
        picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598184/3_o0g3jz.png"),
        price = 8.0,
        rating = 4.1
    ),
    PopularCoffee(
        title = "Americano",
        description = "Espresso is a concentrated form of coffee served in small,strongshots and is the base for many coffee drinks. It is made by forcing a small amount of nearly boiling water through finely-ground coffee beans under high pressure.The result is a rich, robust flavor with a thick, creamy layer on top called crema",
        extra = "Espersso, milk",
        picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598188/6_syir34.png"),
        price = 5.5,
        rating = 4.4
    )
)

// --- UI Section ---

@Composable
fun HomeScreen() {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.first().id) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1)) // CoffeeLight
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Search Bar
        SearchBar(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Promo Banner Carousel
        PromoBannerCarousel(
            banners = banners,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Category Tabs
        CategoryTabs(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Popular Coffees
        Text(
            text = "Popular Coffees",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF4E342E), // CoffeeDark
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        PopularCoffeeList(
            coffees = popularCoffees.filter {
                (searchText.isBlank() || it.title.contains(searchText, ignoreCase = true) || it.description.contains(searchText, ignoreCase = true))
                && (selectedCategory == 0 || categories[selectedCategory].title in it.title)
            }
        )
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Search for coffee...") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .height(48.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFFE082),
            unfocusedBorderColor = Color(0xFF795548),
            cursorColor = Color(0xFF795548)
        )
    )
}

@Composable
fun PromoBannerCarousel(
    banners: List<Banner>,
    modifier: Modifier = Modifier
) {
    // For simplicity, just show the first banner (carousel can be added with Pager)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFFFE082), Color(0xFF795548).copy(alpha = 0.1f))
                )
            )
    ) {
        if (banners.isNotEmpty()) {
            AsyncImage(
                model = banners[0].url,
                contentDescription = "Promo Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
            )
        }
        // Overlay promo text
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = "Buy One, Get One Free!",
                color = Color(0xFF4E342E),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.background(
                    Color(0xFFFFE082).copy(alpha = 0.85f),
                    shape = RoundedCornerShape(8.dp)
                ).padding(horizontal = 8.dp, vertical = 2.dp)
            )
            Text(
                text = "Limited time offer on all drinks.",
                color = Color(0xFF4E342E),
                fontSize = 14.sp,
                modifier = Modifier
                    .background(
                        Color(0xFFFFF8E1).copy(alpha = 0.85f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Int,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp)
    ) {
        categories.forEach { category ->
            val isSelected = selectedCategory == category.id
            val background = if (isSelected) Color(0xFFFFE082) else Color.White
            val textColor = if (isSelected) Color(0xFF4E342E) else Color(0xFF795548)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = background,
                shadowElevation = if (isSelected) 4.dp else 0.dp,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onCategorySelected(category.id) }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = category.title,
                        color = textColor,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PopularCoffeeList(coffees: List<PopularCoffee>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        coffees.forEach { coffee ->
            PopularCoffeeCard(coffee)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PopularCoffeeCard(coffee: PopularCoffee) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Coffee Image
            AsyncImage(
                model = coffee.picUrl.firstOrNull(),
                contentDescription = coffee.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFE082))
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Coffee Info
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = coffee.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF4E342E),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = coffee.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF795548),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${coffee.price}",
                        color = Color(0xFF4E342E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = coffee.rating.toString(),
                            color = Color(0xFF4E342E),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

package com.example.kotlinsample.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinsample.view.pages.HomeScreen

// You can define your own theme colors here for yellowish bento style
private val CoffeeYellow = Color(0xFFFFE082)
private val CoffeeBrown = Color(0xFF795548)
private val CoffeeLight = Color(0xFFFFF8E1)
private val CoffeeDark = Color(0xFF4E342E)

class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeNavigationBody()
        }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeNavigationBody() {
    val context = LocalContext.current

    // Use coffee-related icons and labels
    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Default.Home),
        BottomNavItem("Card Items", Icons.Default.ShoppingCart),
        BottomNavItem("Profile", Icons.Default.Person)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    // Custom theme for yellowish bento style
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = CoffeeYellow,
            onPrimary = CoffeeDark,
            background = CoffeeLight,
            surface = CoffeeYellow,
            onSurface = CoffeeDark
        )
    ) {
        Scaffold(
            containerColor = CoffeeLight,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Coffee Bento",
                            color = CoffeeDark,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CoffeeYellow,
                        titleContentColor = CoffeeDark
                    ),
                    navigationIcon = {
                        // Optionally remove or keep a minimal back icon
                        Spacer(modifier = Modifier.width(8.dp))
                    },
                    actions = {
                        // No actions (remove add product, etc)
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = CoffeeYellow,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .height(60.dp)
                        .background(CoffeeYellow)
                ) {
                    bottomNavItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.label,
                                    tint = if (selectedIndex == index) CoffeeDark else CoffeeBrown,
                                    modifier = Modifier.size(35.dp)
                                )
                            },
                            label = {
                                Text(
                                    item.label,
                                    color = if (selectedIndex == index) CoffeeDark else CoffeeBrown,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = CoffeeYellow,
                                selectedIconColor = CoffeeDark,
                                selectedTextColor = CoffeeDark,
                                unselectedIconColor = CoffeeBrown,
                                unselectedTextColor = CoffeeBrown
                            )
                        )
                    }
                }
            },
            // Remove floatingActionButton
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(CoffeeLight),
                contentAlignment = Alignment.Center
            ) {
                when (selectedIndex) {
                    0 -> HomeScreen()
                    1 -> {
                        // TODO: Replace with your Card Items screen composable
                        Text(
                            text = "Card Items",
                            color = CoffeeDark,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    2 -> {
                        // Launch UserProfileActivity
                        LaunchedEffect(Unit) {
                            val intent = Intent(context, UserProfileViewActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoffeeNavigationBodyPreview() {
    CoffeeNavigationBody()
}

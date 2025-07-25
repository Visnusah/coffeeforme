package com.coffeeforme.coffeee.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.coffeeforme.coffeee.ui.theme.*

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun CoffeeBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    cartItemCount: Int = 0
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Cart", Icons.Default.ShoppingCart, "cart"),
        BottomNavItem("Profile", Icons.Default.Person, "profile")
    )
    
    NavigationBar(
        containerColor = Surface,
        contentColor = Primary,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    if (item.route == "cart" && cartItemCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Error
                                ) {
                                    Text(
                                        text = cartItemCount.toString(),
                                        color = Color.White
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = Gray500,
                    unselectedTextColor = Gray500,
                    indicatorColor = Primary.copy(alpha = 0.1f)
                )
            )
        }
    }
}

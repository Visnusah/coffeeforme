package com.coffeeforme.coffeee.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.coffeeforme.coffeee.data.model.Coffee
import com.coffeeforme.coffeee.data.model.CoffeeData
import com.coffeeforme.coffeee.data.model.Banner
import com.coffeeforme.coffeee.data.model.Category
import kotlinx.coroutines.tasks.await

/**
 * Utility class to set up demo data in Firebase for the Coffee For Me app
 * This includes creating demo user accounts and populating coffee data
 */
object FirebaseDemoDataSetup {
    
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    
    /**
     * Demo user credentials for testing
     */
    object DemoCredentials {
        const val EMAIL = "demo@coffeefor.me"
        const val PASSWORD = "password123"
        const val NAME = "Coffee Lover"
        
        // Additional demo users
        const val ADMIN_EMAIL = "admin@coffeefor.me"
        const val ADMIN_PASSWORD = "admin123"
        const val ADMIN_NAME = "Coffee Admin"
        
        const val USER_EMAIL = "user@coffeefor.me"
        const val USER_PASSWORD = "user123"
        const val USER_NAME = "Regular User"
    }
    
    /**
     * Creates demo user accounts in Firebase Auth
     */
    suspend fun createDemoUsers(): Result<Unit> {
        return try {
            // Create main demo user
            createUserIfNotExists(DemoCredentials.EMAIL, DemoCredentials.PASSWORD, DemoCredentials.NAME)
            
            // Create admin user
            createUserIfNotExists(DemoCredentials.ADMIN_EMAIL, DemoCredentials.ADMIN_PASSWORD, DemoCredentials.ADMIN_NAME)
            
            // Create regular user
            createUserIfNotExists(DemoCredentials.USER_EMAIL, DemoCredentials.USER_PASSWORD, DemoCredentials.USER_NAME)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun createUserIfNotExists(email: String, password: String, name: String) {
        try {
            // Try to create user
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            
            if (user != null) {
                // Create user profile in database
                val userProfile = mapOf(
                    "name" to name,
                    "email" to email,
                    "createdAt" to System.currentTimeMillis(),
                    "isDemo" to true
                )
                
                database.child("users").child(user.uid).child("profile")
                    .setValue(userProfile).await()
                
                println("Demo user created: $email")
            }
        } catch (e: Exception) {
            // User might already exist, which is fine
            println("User $email might already exist: ${e.message}")
        }
    }
    
    /**
     * Populates Firebase with demo coffee data
     */
    suspend fun populateDemoCoffeeData(): Result<Unit> {
        return try {
            val demoData = createDemoCoffeeData()
            database.child("coffee_data").setValue(demoData).await()
            
            // Add some demo cart items for the demo user
            populateDemoCartData()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun populateDemoCartData() {
        try {
            // Sign in as demo user to get UID
            val authResult = auth.signInWithEmailAndPassword(DemoCredentials.EMAIL, DemoCredentials.PASSWORD).await()
            val userId = authResult.user?.uid ?: return
            
            // Add demo cart items
            val demoCartItems = listOf(
                createDemoCartItem("cappuccino", "Cappuccino", 4.5, 2, "Medium"),
                createDemoCartItem("espresso", "Espresso", 3.5, 1, "Small"),
                createDemoCartItem("latte", "Caffe Latte", 5.0, 1, "Large")
            )
            
            demoCartItems.forEach { (coffeeId, cartItem) ->
                database.child("users").child(userId).child("cart").child(coffeeId)
                    .setValue(cartItem).await()
            }
            
            println("Demo cart data populated for user: $userId")
        } catch (e: Exception) {
            println("Error populating demo cart: ${e.message}")
        }
    }
    
    private fun createDemoCartItem(id: String, title: String, price: Double, quantity: Int, size: String): Pair<String, Map<String, Any>> {
        val coffee = Coffee(
            id = id,
            title = title,
            price = price,
            description = "Demo $title description",
            rating = 4.5,
            picUrl = listOf("https://example.com/coffee/$id.jpg"),
            extra = "Demo coffee",
            categoryId = "1"
        )
        
        val cartItem = mapOf(
            "coffeeItem" to coffee,
            "quantity" to quantity,
            "selectedSize" to size,
            "timestamp" to System.currentTimeMillis()
        )
        
        return id to cartItem
    }
    
    private fun createDemoCoffeeData(): CoffeeData {
        return CoffeeData(
            banners = listOf(
                Banner("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598162/banner_pnixuo.png")
            ),
            categories = listOf(
                Category(0, "Espresso"),
                Category(1, "Cappuccino"),
                Category(2, "Latte"),
                Category(3, "Americano"),
                Category(4, "Hot Chocolate")
            ),
            popular = createPopularCoffees(),
            coffee = createAllCoffees()
        )
    }
    
    private fun createPopularCoffees(): List<Coffee> {
        return listOf(
            Coffee(
                id = "cappuccino",
                categoryId = "1",
                title = "Cappuccino",
                description = "Cappuccino is a traditional Italian coffee drink made with equal parts espresso, steamed milk, and milk foam. It has a strong espresso flavor, balanced by the creamy texture of the steamed milk and the light, airy foam on top.",
                price = 4.5,
                rating = 4.6,
                picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598189/4_vcxvtv.png"),
                extra = "Espresso, Milk"
            ),
            Coffee(
                id = "espresso",
                categoryId = "0",
                title = "Espresso",
                description = "Espresso is a concentrated form of coffee served in small, strong shots and is the base for many coffee drinks. Made by forcing nearly boiling water through finely-ground coffee beans under high pressure.",
                price = 3.5,
                rating = 4.0,
                picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598194/5_gdnijm.png"),
                extra = "Pure Espresso"
            ),
            Coffee(
                id = "latte",
                categoryId = "2",
                title = "Caffe Latte",
                description = "A coffee drink made with espresso and steamed milk. The word comes from the Italian caffè e latte, which means 'coffee and milk'. Perfect balance of coffee and creamy milk.",
                price = 5.0,
                rating = 4.7,
                picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598201/6_bz0wgr.png"),
                extra = "Espresso, Steamed Milk"
            )
        )
    }
    
    private fun createAllCoffees(): List<Coffee> {
        return listOf(
            // All popular coffees plus additional ones
            *createPopularCoffees().toTypedArray(),
            Coffee(
                id = "americano",
                categoryId = "3",
                title = "Americano",
                description = "Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavor from, traditionally brewed coffee.",
                price = 3.8,
                rating = 4.2,
                picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598207/7_qjvnzm.png"),
                extra = "Espresso, Hot Water"
            ),
            Coffee(
                id = "hot_chocolate",
                categoryId = "4",
                title = "Hot Chocolate",
                description = "Rich and creamy hot chocolate made with premium cocoa beans and steamed milk. Topped with whipped cream and a sprinkle of cocoa powder.",
                price = 4.2,
                rating = 4.4,
                picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598213/8_xkjfnr.png"),
                extra = "Cocoa, Milk, Whipped Cream"
            ),
            Coffee(
                id = "macchiato",
                categoryId = "0",
                title = "Macchiato",
                description = "Espresso macchiato is a coffee drink consisting of a shot of espresso marked or stained with a dollop of steamed, foamed milk.",
                price = 4.8,
                rating = 4.3,
                picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598219/9_mnbvxz.png"),
                extra = "Espresso, Foamed Milk"
            ),
            Coffee(
                id = "mocha",
                categoryId = "2",
                title = "Cafe Mocha",
                description = "A chocolate-flavored warm beverage that is a variant of a caffè latte, commonly served in a glass rather than a mug.",
                price = 5.5,
                rating = 4.8,
                picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598225/10_plqrst.png"),
                extra = "Espresso, Chocolate, Steamed Milk"
            ),
            Coffee(
                id = "frappuccino",
                categoryId = "2",
                title = "Frappuccino",
                description = "A blended ice coffee drink made with coffee, milk, and ice, topped with whipped cream. Perfect for hot summer days.",
                price = 6.0,
                rating = 4.5,
                picUrl = listOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598231/11_uvwxyz.png"),
                extra = "Coffee, Milk, Ice, Whipped Cream"
            )
        )
    }
    
    /**
     * Complete setup - creates users and populates data
     */
    suspend fun setupCompleteDemo(): Result<String> {
        return try {
            createDemoUsers()
            populateDemoCoffeeData()
            
            val message = """
                Demo setup completed successfully!
                
                Demo Credentials:
                Email: ${DemoCredentials.EMAIL}
                Password: ${DemoCredentials.PASSWORD}
                
                Admin Credentials:
                Email: ${DemoCredentials.ADMIN_EMAIL}
                Password: ${DemoCredentials.ADMIN_PASSWORD}
                
                Regular User:
                Email: ${DemoCredentials.USER_EMAIL}
                Password: ${DemoCredentials.USER_PASSWORD}
                
                The Firebase database has been populated with:
                - 8 coffee varieties
                - 5 categories
                - Sample cart data
                - User profiles
            """.trimIndent()
            
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

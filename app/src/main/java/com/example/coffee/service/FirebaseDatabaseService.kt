package com.example.giggo.service

import com.example.giggo.model.Banner
import com.example.giggo.model.CartItem
import com.example.giggo.model.Category
import com.example.giggo.model.CoffeeDatabase
import com.example.giggo.model.CoffeeItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseDatabaseService {
    private val database: DatabaseReference = Firebase.database.reference
    
    // Coffee data flows
    fun getCoffeeDatabase(): Flow<CoffeeDatabase?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val coffeeDatabase = snapshot.getValue(CoffeeDatabase::class.java)
                    trySend(coffeeDatabase)
                } catch (e: Exception) {
                    trySend(null)
                }
            }
            
            override fun onCancelled(error: DatabaseError) {
                trySend(null)
            }
        }
        
        database.child("coffee_data").addValueEventListener(listener)
        awaitClose { database.child("coffee_data").removeEventListener(listener) }
    }
    
    fun getBanners(): Flow<List<Banner>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val banners = mutableListOf<Banner>()
                snapshot.children.forEach { child ->
                    child.getValue(Banner::class.java)?.let { banner ->
                        banners.add(banner)
                    }
                }
                trySend(banners)
            }
            
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        
        database.child("coffee_data/banners").addValueEventListener(listener)
        awaitClose { database.child("coffee_data/banners").removeEventListener(listener) }
    }
    
    fun getCategories(): Flow<List<Category>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = mutableListOf<Category>()
                snapshot.children.forEach { child ->
                    child.getValue(Category::class.java)?.let { category ->
                        categories.add(category)
                    }
                }
                trySend(categories)
            }
            
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        
        database.child("coffee_data/categories").addValueEventListener(listener)
        awaitClose { database.child("coffee_data/categories").removeEventListener(listener) }
    }
    
    fun getCoffeeItems(): Flow<List<CoffeeItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<CoffeeItem>()
                snapshot.children.forEach { child ->
                    child.getValue(CoffeeItem::class.java)?.let { item ->
                        items.add(item)
                    }
                }
                trySend(items)
            }
            
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        
        database.child("coffee_data/coffee").addValueEventListener(listener)
        awaitClose { database.child("coffee_data/coffee").removeEventListener(listener) }
    }
    
    // User cart management
    fun getUserCart(userId: String): Flow<List<CartItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = mutableListOf<CartItem>()
                snapshot.children.forEach { child ->
                    child.getValue(CartItem::class.java)?.let { item ->
                        cartItems.add(item)
                    }
                }
                trySend(cartItems)
            }
            
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        
        database.child("users/$userId/cart").addValueEventListener(listener)
        awaitClose { database.child("users/$userId/cart").removeEventListener(listener) }
    }
    
    suspend fun addToCart(userId: String, cartItem: CartItem): Result<Unit> {
        return try {
            database.child("users/$userId/cart/${cartItem.id}").setValue(cartItem).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateCartItemQuantity(userId: String, itemId: String, quantity: Int): Result<Unit> {
        return try {
            if (quantity <= 0) {
                database.child("users/$userId/cart/$itemId").removeValue().await()
            } else {
                database.child("users/$userId/cart/$itemId/quantity").setValue(quantity).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun removeFromCart(userId: String, itemId: String): Result<Unit> {
        return try {
            database.child("users/$userId/cart/$itemId").removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun clearCart(userId: String): Result<Unit> {
        return try {
            database.child("users/$userId/cart").removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Upload coffee data (for admin/initial setup)
    suspend fun uploadCoffeeData(coffeeDatabase: CoffeeDatabase): Result<Unit> {
        return try {
            database.child("coffee_data").setValue(coffeeDatabase).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // User profile management
    suspend fun saveUserProfile(userId: String, name: String, email: String): Result<Unit> {
        return try {
            val userProfile = mapOf(
                "name" to name,
                "email" to email,
                "lastUpdated" to System.currentTimeMillis()
            )
            database.child("users/$userId/profile").setValue(userProfile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getUserProfile(userId: String): Flow<Map<String, Any>?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile = snapshot.getValue() as? Map<String, Any>
                trySend(profile)
            }
            
            override fun onCancelled(error: DatabaseError) {
                trySend(null)
            }
        }
        
        database.child("users/$userId/profile").addValueEventListener(listener)
        awaitClose { database.child("users/$userId/profile").removeEventListener(listener) }
    }
}

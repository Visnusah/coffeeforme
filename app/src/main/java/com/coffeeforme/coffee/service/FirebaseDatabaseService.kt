package com.coffeeforme.coffeee.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.coffeeforme.coffeee.data.model.Coffee
import com.coffeeforme.coffeee.data.model.CartItem
import com.coffeeforme.coffeee.data.model.CoffeeData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FirebaseDatabaseService {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    
    suspend fun uploadCoffeeData(coffeeData: CoffeeData): Result<Unit> {
        return try {
            database.child("coffee_data").setValue(coffeeData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getCoffeeDatabase(): Flow<CoffeeData?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val coffeeData = snapshot.getValue(CoffeeData::class.java)
                trySend(coffeeData)
            }
            
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        database.child("coffee_data").addValueEventListener(listener)
        awaitClose { database.child("coffee_data").removeEventListener(listener) }
    }
    
    suspend fun addToCart(userId: String, coffee: Coffee, quantity: Int, selectedSize: String): Result<Unit> {
        return try {
            val cartItem = mapOf(
                "coffeeItem" to coffee,
                "quantity" to quantity,
                "selectedSize" to selectedSize,
                "timestamp" to System.currentTimeMillis()
            )
            database.child("users").child(userId).child("cart").child(coffee.id)
                .setValue(cartItem).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getUserCart(userId: String): Flow<List<CartItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = mutableListOf<CartItem>()
                for (itemSnapshot in snapshot.children) {
                    try {
                        val coffee = itemSnapshot.child("coffeeItem").getValue(Coffee::class.java)
                        val quantity = itemSnapshot.child("quantity").getValue(Int::class.java) ?: 1
                        val selectedSize = itemSnapshot.child("selectedSize").getValue(String::class.java) ?: "Medium"
                        
                        if (coffee != null) {
                            cartItems.add(CartItem(coffee, quantity, selectedSize))
                        }
                    } catch (e: Exception) {
                        // Skip invalid items
                    }
                }
                trySend(cartItems)
            }
            
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        database.child("users").child(userId).child("cart").addValueEventListener(listener)
        awaitClose { database.child("users").child(userId).child("cart").removeEventListener(listener) }
    }
    
    suspend fun removeFromCart(userId: String, coffeeId: String): Result<Unit> {
        return try {
            database.child("users").child(userId).child("cart").child(coffeeId)
                .removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserProfile(userId: String, name: String, email: String): Result<Unit> {
        return try {
            val profile = mapOf(
                "name" to name,
                "email" to email,
                "lastUpdated" to System.currentTimeMillis()
            )
            database.child("users").child(userId).child("profile")
                .setValue(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun clearCart(userId: String): Result<Unit> {
        return try {
            database.child("users").child(userId).child("cart").removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun syncOfflineData(coffees: List<Coffee>): Result<Unit> {
        return try {
            database.child("offline_data").setValue(coffees).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

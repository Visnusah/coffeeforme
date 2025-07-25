package com.coffeeforme.coffeee.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.coffeeforme.coffeee.data.model.Coffee
import com.coffeeforme.coffeee.data.model.CartItem
import com.coffeeforme.coffeee.data.repository.CoffeeRepository
import com.coffeeforme.coffeee.service.FirebaseAuthService
import com.coffeeforme.coffeee.service.FirebaseDatabaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FirebaseCoffeeViewModel(application: Application) : AndroidViewModel(application) {
    private val coffeeRepository = CoffeeRepository(application.applicationContext)
    private val firebaseAuth = FirebaseAuthService()
    private val firebaseDatabase = FirebaseDatabaseService()
    
    private val _coffees = MutableStateFlow<List<Coffee>>(emptyList())
    val coffees: StateFlow<List<Coffee>> = _coffees
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    init {
        loadCoffees()
        observeUserCart()
    }
    
    private fun loadCoffees() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // First load local data
                val localCoffees = coffeeRepository.getAllCoffees()
                _coffees.value = localCoffees
                
                // Try to upload local data to Firebase if user is logged in
                firebaseAuth.currentUser?.let { user ->
                    val coffeeData = coffeeRepository.getCoffeeData()
                    firebaseDatabase.uploadCoffeeData(coffeeData)
                }
                
                // Listen for Firebase data updates
                firebaseDatabase.getCoffeeDatabase().collect { firebaseData ->
                    if (firebaseData != null) {
                        _coffees.value = firebaseData.coffee
                    }
                }
            } catch (e: Exception) {
                // Handle error - stick with local data
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun observeUserCart() {
        viewModelScope.launch {
            firebaseAuth.authState.collect { user ->
                if (user != null) {
                    // User is logged in, observe Firebase cart
                    firebaseDatabase.getUserCart(user.uid).collect { cartItems ->
                        _cartItems.value = cartItems
                    }
                } else {
                    // User is logged out, clear cart
                    _cartItems.value = emptyList()
                }
            }
        }
    }
    
    fun addToCart(coffee: Coffee, quantity: Int = 1, selectedSize: String = "Medium") {
        viewModelScope.launch {
            val user = firebaseAuth.currentUser
            if (user != null) {
                // Add to Firebase cart
                firebaseDatabase.addToCart(user.uid, coffee, quantity, selectedSize)
            } else {
                // Handle local cart (for offline/non-logged-in users)
                val currentCart = _cartItems.value.toMutableList()
                val existingItem = currentCart.find { it.coffee.id == coffee.id }
                
                if (existingItem != null) {
                    val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
                    currentCart[currentCart.indexOf(existingItem)] = updatedItem
                } else {
                    currentCart.add(CartItem(coffee, quantity, selectedSize))
                }
                _cartItems.value = currentCart
            }
        }
    }
    
    fun removeFromCart(coffee: Coffee) {
        viewModelScope.launch {
            val user = firebaseAuth.currentUser
            if (user != null) {
                // Remove from Firebase cart
                firebaseDatabase.removeFromCart(user.uid, coffee.id)
            } else {
                // Handle local cart
                val currentCart = _cartItems.value.toMutableList()
                currentCart.removeAll { it.coffee.id == coffee.id }
                _cartItems.value = currentCart
            }
        }
    }
    
    fun updateCartItemQuantity(coffee: Coffee, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(coffee)
        } else {
            viewModelScope.launch {
                val user = firebaseAuth.currentUser
                if (user != null) {
                    // Update Firebase cart
                    val cartItem = _cartItems.value.find { it.coffee.id == coffee.id }
                    if (cartItem != null) {
                        firebaseDatabase.addToCart(user.uid, coffee, newQuantity, cartItem.selectedSize)
                    }
                } else {
                    // Handle local cart
                    val currentCart = _cartItems.value.toMutableList()
                    val existingItemIndex = currentCart.indexOfFirst { it.coffee.id == coffee.id }
                    if (existingItemIndex != -1) {
                        currentCart[existingItemIndex] = currentCart[existingItemIndex].copy(quantity = newQuantity)
                        _cartItems.value = currentCart
                    }
                }
            }
        }
    }
    
    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { 
            it.coffee.price * it.quantity 
        }
    }
    
    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            val user = firebaseAuth.currentUser
            if (user != null) {
                // Clear Firebase cart
                firebaseDatabase.clearCart(user.uid)
            }
            _cartItems.value = emptyList()
        }
    }
}

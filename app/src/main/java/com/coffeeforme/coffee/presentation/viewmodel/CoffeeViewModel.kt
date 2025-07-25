package com.coffeeforme.coffeee.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.coffeeforme.coffeee.data.model.CartItem
import com.coffeeforme.coffeee.data.model.Category
import com.coffeeforme.coffeee.data.model.CoffeeDatabase
import com.coffeeforme.coffeee.data.model.CoffeeItem
import com.coffeeforme.coffeee.data.repository.CoffeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoffeeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = CoffeeRepository(application.applicationContext)
    
    private val _coffeeDatabase = MutableStateFlow<CoffeeDatabase?>(null)
    val coffeeDatabase: StateFlow<CoffeeDatabase?> = _coffeeDatabase.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow(0)
    val selectedCategory: StateFlow<Int> = _selectedCategory.asStateFlow()
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadCoffeeData()
    }
    
    private fun loadCoffeeData() {
        viewModelScope.launch {
            _isLoading.value = true
            val data = repository.loadCoffeeData()
            _coffeeDatabase.value = data
            _isLoading.value = false
        }
    }
    
    fun selectCategory(categoryId: Int) {
        _selectedCategory.value = categoryId
    }
    
    fun getFilteredItems(): List<CoffeeItem> {
        val database = _coffeeDatabase.value ?: return emptyList()
        return if (_selectedCategory.value == -1) {
            database.Popular
        } else {
            database.Items.filter { it.categoryId == _selectedCategory.value.toString() }
        }
    }
    
    fun addToCart(coffeeItem: CoffeeItem, size: String = "Medium") {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { 
            it.coffeeItem.title == coffeeItem.title && it.selectedSize == size 
        }
        
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentCart.add(CartItem(coffeeItem, 1, size))
        }
        
        _cartItems.value = currentCart
    }
    
    fun removeFromCart(cartItem: CartItem) {
        val currentCart = _cartItems.value.toMutableList()
        currentCart.remove(cartItem)
        _cartItems.value = currentCart
    }
    
    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(cartItem)
            return
        }
        
        val currentCart = _cartItems.value.toMutableList()
        val index = currentCart.indexOf(cartItem)
        if (index != -1) {
            currentCart[index] = cartItem.copy(quantity = newQuantity)
            _cartItems.value = currentCart
        }
    }
    
    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.coffeeItem.price * it.quantity }
    }
    
    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
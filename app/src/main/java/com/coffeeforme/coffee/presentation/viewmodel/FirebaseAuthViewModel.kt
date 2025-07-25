package com.coffeeforme.coffeee.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeeforme.coffeee.service.FirebaseAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FirebaseAuthViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuthService()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    val isLoggedIn: StateFlow<Boolean> = firebaseAuth.authState.map { it != null }
        .let { flow ->
            val stateFlow = MutableStateFlow(false)
            viewModelScope.launch {
                flow.collect { stateFlow.value = it }
            }
            stateFlow
        }
    
    val currentUser = firebaseAuth.authState
    
    val currentUserName: StateFlow<String> = firebaseAuth.authState.map { 
        it?.displayName ?: "Coffee Lover" 
    }.let { flow ->
        val stateFlow = MutableStateFlow("Coffee Lover")
        viewModelScope.launch {
            flow.collect { stateFlow.value = it }
        }
        stateFlow
    }
    
    val currentUserEmail: StateFlow<String> = firebaseAuth.authState.map { 
        it?.email ?: "guest@coffee.com" 
    }.let { flow ->
        val stateFlow = MutableStateFlow("guest@coffee.com")
        viewModelScope.launch {
            flow.collect { stateFlow.value = it }
        }
        stateFlow
    }
    
    fun login(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password)
                _isLoading.value = false
                if (result.isSuccess) {
                    onComplete(true, "Login successful")
                } else {
                    onComplete(false, result.exceptionOrNull()?.message ?: "Login failed")
                }
            } catch (e: Exception) {
                _isLoading.value = false
                onComplete(false, e.message ?: "Login failed")
            }
        }
    }
    
    fun signUp(name: String, email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password)
                if (result.isSuccess) {
                    // Update profile with name
                    firebaseAuth.updateUserProfile(name)
                    _isLoading.value = false
                    onComplete(true, "Account created successfully")
                } else {
                    _isLoading.value = false
                    onComplete(false, result.exceptionOrNull()?.message ?: "Sign up failed")
                }
            } catch (e: Exception) {
                _isLoading.value = false
                onComplete(false, e.message ?: "Sign up failed")
            }
        }
    }
    
    fun updateProfile(name: String, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = firebaseAuth.updateUserProfile(name)
                _isLoading.value = false
                if (result.isSuccess) {
                    onComplete(true, "Profile updated successfully")
                } else {
                    onComplete(false, result.exceptionOrNull()?.message ?: "Update failed")
                }
            } catch (e: Exception) {
                _isLoading.value = false
                onComplete(false, e.message ?: "Update failed")
            }
        }
    }
    
    fun logout() {
        firebaseAuth.signOut()
    }
}
        _currentUserEmail.value = "user@coffeefor.me"
    }
}

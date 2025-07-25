package com.coffeeforme.coffeee.data.repository

import com.coffeeforme.coffeee.data.model.AuthResult
import com.coffeeforme.coffeee.data.model.LoginRequest
import com.coffeeforme.coffeee.data.model.SignUpRequest
import com.coffeeforme.coffeee.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository {
    
    // Simulate network calls with mock implementation
    suspend fun login(request: LoginRequest): Flow<AuthResult> = flow {
        emit(AuthResult.Loading)
        delay(2000) // Simulate network delay
        
        // Mock validation
        if (request.email.isValidEmail() && request.password.length >= 6) {
            emit(AuthResult.Success)
        } else {
            emit(AuthResult.Error("Invalid email or password"))
        }
    }
    
    suspend fun signUp(request: SignUpRequest): Flow<AuthResult> = flow {
        emit(AuthResult.Loading)
        delay(2000) // Simulate network delay
        
        when {
            request.firstName.isBlank() -> {
                emit(AuthResult.Error("First name is required"))
            }
            request.lastName.isBlank() -> {
                emit(AuthResult.Error("Last name is required"))
            }
            !request.email.isValidEmail() -> {
                emit(AuthResult.Error("Invalid email format"))
            }
            request.password.length < 6 -> {
                emit(AuthResult.Error("Password must be at least 6 characters"))
            }
            request.password != request.confirmPassword -> {
                emit(AuthResult.Error("Passwords do not match"))
            }
            else -> {
                emit(AuthResult.Success)
            }
        }
    }
    
    suspend fun getCurrentUser(): User? {
        // Mock implementation - in real app, this would check stored session
        return null
    }
    
    suspend fun logout() {
        // Mock implementation - in real app, this would clear stored session
        delay(500)
    }
    
    private fun String.isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}

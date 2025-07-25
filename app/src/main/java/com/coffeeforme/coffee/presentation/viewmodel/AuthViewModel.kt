package com.coffeeforme.coffeee.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeeforme.coffeee.data.model.AuthResult
import com.coffeeforme.coffeee.data.model.AuthState
import com.coffeeforme.coffeee.data.model.LoginRequest
import com.coffeeforme.coffeee.data.model.SignUpRequest
import com.coffeeforme.coffeee.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _loginEmail = MutableStateFlow("")
    val loginEmail: StateFlow<String> = _loginEmail.asStateFlow()
    
    private val _loginPassword = MutableStateFlow("")
    val loginPassword: StateFlow<String> = _loginPassword.asStateFlow()
    
    private val _signUpFirstName = MutableStateFlow("")
    val signUpFirstName: StateFlow<String> = _signUpFirstName.asStateFlow()
    
    private val _signUpLastName = MutableStateFlow("")
    val signUpLastName: StateFlow<String> = _signUpLastName.asStateFlow()
    
    private val _signUpEmail = MutableStateFlow("")
    val signUpEmail: StateFlow<String> = _signUpEmail.asStateFlow()
    
    private val _signUpPassword = MutableStateFlow("")
    val signUpPassword: StateFlow<String> = _signUpPassword.asStateFlow()
    
    private val _signUpConfirmPassword = MutableStateFlow("")
    val signUpConfirmPassword: StateFlow<String> = _signUpConfirmPassword.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    fun updateLoginEmail(email: String) {
        _loginEmail.value = email
        clearError()
    }
    
    fun updateLoginPassword(password: String) {
        _loginPassword.value = password
        clearError()
    }
    
    fun updateSignUpFirstName(firstName: String) {
        _signUpFirstName.value = firstName
        clearError()
    }
    
    fun updateSignUpLastName(lastName: String) {
        _signUpLastName.value = lastName
        clearError()
    }
    
    fun updateSignUpEmail(email: String) {
        _signUpEmail.value = email
        clearError()
    }
    
    fun updateSignUpPassword(password: String) {
        _signUpPassword.value = password
        clearError()
    }
    
    fun updateSignUpConfirmPassword(confirmPassword: String) {
        _signUpConfirmPassword.value = confirmPassword
        clearError()
    }
    
    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val request = LoginRequest(
                email = _loginEmail.value.trim(),
                password = _loginPassword.value
            )
            
            authRepository.login(request).collect { result ->
                when (result) {
                    is AuthResult.Loading -> {
                        _authState.value = _authState.value.copy(isLoading = true, error = null)
                    }
                    is AuthResult.Success -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            error = null
                        )
                        _isLoggedIn.value = true
                        onSuccess()
                    }
                    is AuthResult.Error -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
    
    fun signUp(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val request = SignUpRequest(
                firstName = _signUpFirstName.value.trim(),
                lastName = _signUpLastName.value.trim(),
                email = _signUpEmail.value.trim(),
                password = _signUpPassword.value,
                confirmPassword = _signUpConfirmPassword.value
            )
            
            authRepository.signUp(request).collect { result ->
                when (result) {
                    is AuthResult.Loading -> {
                        _authState.value = _authState.value.copy(isLoading = true, error = null)
                    }
                    is AuthResult.Success -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            error = null
                        )
                        _isLoggedIn.value = true
                        onSuccess()
                    }
                    is AuthResult.Error -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = AuthState()
            _isLoggedIn.value = false
            clearAllFields()
        }
    }
    
    private fun checkAuthStatus() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            val isAuthenticated = user != null
            _authState.value = _authState.value.copy(
                isAuthenticated = isAuthenticated,
                user = user
            )
            _isLoggedIn.value = isAuthenticated
        }
    }
    
    private fun clearError() {
        if (_authState.value.error != null) {
            _authState.value = _authState.value.copy(error = null)
        }
    }
    
    private fun clearAllFields() {
        _loginEmail.value = ""
        _loginPassword.value = ""
        _signUpFirstName.value = ""
        _signUpLastName.value = ""
        _signUpEmail.value = ""
        _signUpPassword.value = ""
        _signUpConfirmPassword.value = ""
    }
}

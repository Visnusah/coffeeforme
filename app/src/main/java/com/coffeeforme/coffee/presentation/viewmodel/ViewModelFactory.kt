package com.coffeeforme.coffeee.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class CoffeeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeViewModel::class.java)) {
            return CoffeeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FirebaseCoffeeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirebaseCoffeeViewModel::class.java)) {
            return FirebaseCoffeeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

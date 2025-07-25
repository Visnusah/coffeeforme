package com.coffeeforme.coffeee.util

import android.content.Context
import com.coffeeforme.coffeee.data.repository.CoffeeRepository
import com.coffeeforme.coffeee.service.FirebaseDatabaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataMigrationHelper(private val context: Context) {
    private val coffeeRepository = CoffeeRepository(context)
    private val firebaseDatabase = FirebaseDatabaseService()
    
    fun uploadLocalDataToFirebase(callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val coffeeData = coffeeRepository.getCoffeeData()
                val result = firebaseDatabase.uploadCoffeeData(coffeeData)
                
                if (result.isSuccess) {
                    callback(true, "✅ Data uploaded successfully!")
                } else {
                    callback(false, "❌ Upload failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                callback(false, "❌ Upload failed: ${e.message}")
            }
        }
    }
    
    fun checkFirebaseData(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                firebaseDatabase.getCoffeeDatabase().collect { data ->
                    callback(data != null && data.coffee.isNotEmpty())
                }
            } catch (e: Exception) {
                callback(false)
            }
        }
    }
    
    fun migrateLocalToFirebase(callback: (Boolean, String) -> Unit) {
        checkFirebaseData { hasData ->
            if (hasData) {
                callback(true, "✅ Firebase already has data")
            } else {
                uploadLocalDataToFirebase(callback)
            }
        }
    }
}
                    } else {
                        onComplete(false, "Failed to upload data: ${result.exceptionOrNull()?.message}")
                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(false, "Error: ${e.message}")
                }
            }
        }
    }
    
    /**
     * Check if Firebase database has coffee data
     */
    fun checkFirebaseData(onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDatabase.getCoffeeDatabase().collect { data ->
                CoroutineScope(Dispatchers.Main).launch {
                    onResult(data != null)
                }
            }
        }
    }
}

package com.coffeeforme.coffeee

import android.app.Application
import com.google.firebase.FirebaseApp

class CoffeeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}

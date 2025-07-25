package com.coffeeforme.coffeee.data.repository

import android.content.Context
import com.coffeeforme.coffeee.data.model.CoffeeDatabase
import com.coffeeforme.coffeee.data.model.Coffee
import com.coffeeforme.coffeee.data.model.CoffeeData
import kotlinx.serialization.json.Json
import java.io.IOException

class CoffeeRepository(private val context: Context) {
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    suspend fun loadCoffeeData(): CoffeeDatabase? {
        return try {
            val jsonString = context.assets.open("database.json").bufferedReader().use { it.readText() }
            json.decodeFromString<CoffeeDatabase>(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun getAllCoffees(): List<Coffee> {
        return try {
            val database = loadCoffeeData()
            if (database != null) {
                // Convert from CoffeeItem to Coffee
                database.Items.map { item ->
                    Coffee(
                        id = item.title.replace(" ", "_").lowercase(),
                        categoryId = item.categoryId ?: "",
                        description = item.description,
                        extra = item.extra,
                        picUrl = item.picUrl,
                        price = item.price,
                        rating = item.rating,
                        title = item.title
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun getCoffeeData(): CoffeeData {
        return try {
            val database = loadCoffeeData()
            if (database != null) {
                // Convert to Firebase-compatible format
                CoffeeData(
                    banners = database.Banner,
                    categories = database.Category,
                    popular = database.Popular.map { item ->
                        Coffee(
                            id = item.title.replace(" ", "_").lowercase(),
                            categoryId = item.categoryId ?: "",
                            description = item.description,
                            extra = item.extra,
                            picUrl = item.picUrl,
                            price = item.price,
                            rating = item.rating,
                            title = item.title
                        )
                    },
                    coffee = database.Items.map { item ->
                        Coffee(
                            id = item.title.replace(" ", "_").lowercase(),
                            categoryId = item.categoryId ?: "",
                            description = item.description,
                            extra = item.extra,
                            picUrl = item.picUrl,
                            price = item.price,
                            rating = item.rating,
                            title = item.title
                        )
                    }
                )
            } else {
                // Return empty data structure
                CoffeeData(
                    banners = emptyList(),
                    categories = emptyList(),
                    coffee = emptyList(),
                    popular = emptyList()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Return empty data structure
            CoffeeData(
                banners = emptyList(),
                categories = emptyList(),
                coffee = emptyList(),
                popular = emptyList()
            )
        }
    }
}

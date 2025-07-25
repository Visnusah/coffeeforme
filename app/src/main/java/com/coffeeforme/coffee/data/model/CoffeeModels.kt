package com.coffeeforme.coffeee.data.model

import kotlinx.serialization.Serializable
import com.google.firebase.database.IgnoreExtraProperties

@Serializable
@IgnoreExtraProperties
data class CoffeeDatabase(
    val Banner: List<Banner>,
    val Category: List<Category>,
    val Popular: List<CoffeeItem>,
    val Items: List<CoffeeItem>
)

@Serializable
data class Banner(
    val url: String
)

@Serializable
data class Category(
    val id: Int,
    val title: String
)

@Serializable
data class CoffeeItem(
    val categoryId: String? = null,
    val description: String,
    val extra: String,
    val picUrl: List<String>,
    val price: Double,
    val rating: Double,
    val title: String
) {
    // No-argument constructor required for Firebase
    constructor() : this(
        categoryId = null,
        description = "",
        extra = "",
        picUrl = emptyList(),
        price = 0.0,
        rating = 0.0,
        title = ""
    )
}

// Firebase compatible data models
@Serializable
data class CoffeeData(
    val banners: List<Banner> = emptyList(),
    val categories: List<Category> = emptyList(),
    val popular: List<Coffee> = emptyList(),
    val coffee: List<Coffee> = emptyList()
) {
    constructor() : this(emptyList(), emptyList(), emptyList(), emptyList())
}

@Serializable
data class Coffee(
    val id: String = "",
    val categoryId: String = "",
    val description: String = "",
    val extra: String = "",
    val picUrl: List<String> = emptyList(),
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val title: String = ""
) {
    constructor() : this("", "", "", "", emptyList(), 0.0, 0.0, "")
}

// UI State models
data class CartItem(
    val coffee: Coffee,
    var quantity: Int = 1,
    val selectedSize: String = "Medium"
) {
    constructor() : this(Coffee(), 1, "Medium")
}

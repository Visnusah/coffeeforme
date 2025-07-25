package com.example.coffeeforme.repository

import com.example.coffeeforme.database.ProductDao
import com.example.coffeeforme.model.ProductModel
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(private val productDao: ProductDao) : ProductRepository {
    override fun getAllProducts(): Flow<List<ProductModel>> = productDao.getAllProducts()
    
    override fun getAvailableProducts(): Flow<List<ProductModel>> = productDao.getAvailableProducts()
    
    override fun getProductsByCategory(category: String): Flow<List<ProductModel>> {
        return productDao.getProductsByCategory(category)
    }
    
    override suspend fun getProductById(productId: Int): ProductModel? {
        return productDao.getProductById(productId)
    }
    
    override fun searchProducts(searchQuery: String): Flow<List<ProductModel>> {
        return productDao.searchProducts(searchQuery)
    }
    
    override suspend fun insertProduct(product: ProductModel): Long {
        return productDao.insertProduct(product)
    }
    
    override suspend fun updateProduct(product: ProductModel) {
        productDao.updateProduct(product)
    }
    
    override suspend fun deleteProduct(product: ProductModel) {
        productDao.deleteProduct(product)
    }
    
    override suspend fun deleteProductById(productId: Int) {
        productDao.deleteProductById(productId)
    }
}
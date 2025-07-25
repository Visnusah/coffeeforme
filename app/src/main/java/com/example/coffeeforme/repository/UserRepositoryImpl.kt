package com.example.coffeeforme.repository

import com.example.coffeeforme.database.UserDao
import com.example.coffeeforme.model.UserModel
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun getAllUsers(): Flow<List<UserModel>> = userDao.getAllUsers()
    
    override suspend fun loginUser(email: String, password: String): UserModel? {
        return userDao.loginUser(email, password)
    }
    
    override suspend fun getUserByEmail(email: String): UserModel? {
        return userDao.getUserByEmail(email)
    }
    
    override suspend fun getUserById(userId: Int): UserModel? {
        return userDao.getUserById(userId)
    }
    
    override suspend fun insertUser(user: UserModel): Long {
        return userDao.insertUser(user)
    }
    
    override suspend fun updateUser(user: UserModel) {
        userDao.updateUser(user)
    }
    
    override suspend fun deleteUser(user: UserModel) {
        userDao.deleteUser(user)
    }
}

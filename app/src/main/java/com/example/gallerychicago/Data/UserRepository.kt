package com.example.gallerychicago.Data

import android.app.Application
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(application: Application)
{
    private var userDao: UserDao = ArtworkDatabase.getDatabase(application).userDAO()

    /**
     * Methods for user data management
     */
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateUserName(userId: Int, name: String) {
        userDao.updateUserName(userId, name)
    }

    suspend fun updateUserDescription(userId: Int, description: String) {
        userDao.updateUserDescription(userId, description)
    }

    /**
     * methods to manage login in, get user by email, check if email exists
     */
    suspend fun loginUser(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        if (user?.password == password)
        {
            // update the logged user state
            userDao.loginUser(user.id)
            return user
        }
        return null
    }

    // get user by id
    suspend fun getUserById(userId: Int):User? {
        return userDao.getUserById(userId)
    }

    //get current user who's logged state is true
    suspend fun getCurrentUser(): User? {
        return userDao.getLoggedInUser()
    }

    // Check if the email exists in the database
    suspend fun isEmailExists(email: String): Boolean {
        return userDao.isEmailExists(email)
    }

    // get current logged user
    suspend fun logoutUser(user: User) {
        userDao.logoutUser(user.id)  // log out
    }
}
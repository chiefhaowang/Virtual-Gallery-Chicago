package com.example.gallerychicago.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao
{

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun updateUser(user: User)

    // get user by id
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    // get user by email
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    // update username
    @Query("UPDATE users SET name = :name WHERE id =  :userId")
    suspend fun updateUserName(userId: Int, name: String)

    // update description
    @Query("UPDATE users SET description = :description WHERE id = :userId")
    suspend fun updateUserDescription(userId: Int, description: String)

    // Check if the email exists in the users table
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean

    /**
     *  get logged user
     */
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): User?

    // check logged user, search the user by Id and set the log state true
    @Query("UPDATE users SET isLoggedIn = true WHERE id = :userId")
    suspend fun loginUser(userId: Int)


    // log out
    @Query("UPDATE users SET isLoggedIn = false WHERE id = :userId")
    suspend fun logoutUser(userId: Int)
}
package com.example.gallerychicago.Data

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.gallerychicago.firebaseInterface.CloudInterface


/**
 * UserViewModel manages all the methods used in UI
 * */

class UserViewModel (application: Application): AndroidViewModel(application)
{
    private val userRepository: UserRepository = UserRepository(application)
    val cloudInterface = CloudInterface()

    /**
     * manage login
     */
    // Mutable current user
    val currentUser = MutableLiveData<User?>()

    // initialize current user
    init {
        loadCurrentUser()
    }

    // load the user information
    fun loadCurrentUser() {
        viewModelScope.launch {
            currentUser.value = userRepository.getCurrentUser()
        }
    }

    fun logoutUser() {
        currentUser.value?.let {
            viewModelScope.launch {
                userRepository.logoutUser(it)
                currentUser.value = null
            }
        }
    }

    val allUsers: Flow<List<User>> = userRepository.allUsers

    // after login
    fun loginUser(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.loginUser(email, password)
            Log.d("UserViewModel", "Logged in user: $user")
            currentUser.value = user// update LiveData
            onResult(user)  // Invoke callback with the result
        }
    }

    /**
     * methods to manipulate user
     */
    // use emailAvailable to check the state of the email
    val emailAvailable = MutableLiveData<Boolean?>()

    // general method to add user
    fun insertUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    // update user
    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }
    // add user in Registration
    fun addUser(name: String, email: String, password: String, birthday: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!userRepository.isEmailExists(email)) {
                val newUser = User(name = name, email = email, password = password, birthday = birthday, description = null, isLoggedIn = false)
                userRepository.insertUser(newUser)
                // upload data to google firebase
                cloudInterface.initializaDbRef()
                println(email)
                cloudInterface.initializeUser(email)
                emailAvailable.postValue(true)  // email doesn't exist
            } else {
                emailAvailable.postValue(false)  // email exists in database
            }
        }
    }

    // update new user name
    fun updateUserName(userId: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUserName(userId, name)
            currentUser.postValue(userRepository.getUserById(userId))
        }
    }

    // update user description
    fun updateUserDescription(userId: Int, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUserDescription(userId, description)
            currentUser.postValue(userRepository.getUserById(userId))
        }
    }

}
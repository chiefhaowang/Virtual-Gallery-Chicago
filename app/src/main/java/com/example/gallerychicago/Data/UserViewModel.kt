package com.example.gallerychicago.Data

import android.app.Application
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


class UserViewModel (application: Application): AndroidViewModel(application)
{
    private val userRepository: UserRepository = UserRepository(application)


    val allUsers: Flow<List<User>> = userRepository.allUsers

    /** methods to manipulate user
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
                val newUser = User(name = name, email = email, password = password, birthday = birthday, description = null)
                userRepository.insertUser(newUser)
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
        }
    }

    // update user description
    fun updateUserDescription(userId: Int, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUserDescription(userId, description)
        }
    }

}
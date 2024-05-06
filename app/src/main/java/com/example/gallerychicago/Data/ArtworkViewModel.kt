package com.example.gallerychicago.Data

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtworkViewModel(application: Application) : AndroidViewModel(application)
{
    // Repository for accessing artwork data. Initializes with the application context and scope.
    private val cRepository: ArtworkRepository = ArtworkRepository(application, viewModelScope)

    init{
        viewModelScope.launch {
            // Check if the database is empty
            if (cRepository.isDatabaseEmpty()) {
                // If the database is empty, fetch artworks
                fetchArtworks(20, 1, arrayOf("id", "image_id", "artwork_type_id"))
            }
        }
    }
    val allArtworks: LiveData<List<Artwork>> = cRepository.allArtworks.asLiveData()
    fun insertArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(artwork)
    }
    fun updateArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(artwork)
    }
    fun deleteArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(artwork)
    }

    /** methods to manipulate user
     */
    // use emailAvailable to check the state of the email
    val emailAvailable = MutableLiveData<Boolean?>()
    fun addUser(name: String, email: String, password: String, birthday: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!cRepository.isEmailExists(email)) {
                val newUser = User(name = name, email = email, password = password, birthday = birthday, description = null)
                cRepository.insertUser(newUser)
                emailAvailable.postValue(true)  // email doesn't exist
            } else {
                emailAvailable.postValue(false)  // email exists in database
            }
        }
    }



    // update new user name
    fun updateUserName(userId: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cRepository.updateUserName(userId, name)
        }
    }

    // update user description
    fun updateUserDescription(userId: Int, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cRepository.updateUserDescription(userId, description)
        }
    }

    // methods to call api and store data in database
    private fun fetchArtworks(size: Int, artworkTypeId: Int, fields: Array<String>)
    {
        // Launch a new coroutine in the scope of ViewModel
        viewModelScope.launch {
            // this method is calling api, retrieving and  storing  data in the database
            cRepository.fetchArtworks(size, artworkTypeId, fields)
        }
    }
}
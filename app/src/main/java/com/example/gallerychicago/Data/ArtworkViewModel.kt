package com.example.gallerychicago.Data

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    private fun fetchArtworks(size: Int, artworkTypeId: Int, fields: Array<String>)
    {
        // Launch a new coroutine in the scope of ViewModel
        viewModelScope.launch {
            // this method is calling api, retrieving and  storing  data in the database
            cRepository.fetchArtworks(size, artworkTypeId, fields)
        }
    }
}
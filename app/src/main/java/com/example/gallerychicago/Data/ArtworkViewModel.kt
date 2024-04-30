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
class ArtworkViewModel(application: Application) : AndroidViewModel(application) {
    private val cRepository: ArtworkRepository

    val artworkResponse: MutableState<ArtworkResponse> =
        mutableStateOf(ArtworkResponse())
    init{
        cRepository = ArtworkRepository(application)
        viewModelScope.launch {
            fetchArtworks()
        }
    }
    val allSubjects: LiveData<List<Artwork>> = cRepository.allArtworks.asLiveData()
    fun insertArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(artwork)
    }
    fun updateArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(artwork)
    }
    fun deleteArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(artwork)
    }

    private suspend fun fetchArtworks() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("Fetching", "Fetching...")
                val response = cRepository.getResponse(1,10)
                Log.d("Response", response.toString())
                response.artworks.forEach { artwork ->
                    insertArtwork(artwork)
                }
            } catch (e: Exception) {
                Log.i("Error ", "Response failed")
            }
        }
    }
}
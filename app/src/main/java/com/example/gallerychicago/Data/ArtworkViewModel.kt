package com.example.gallerychicago.Data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for managing artwork data.
 * @param application The application context.
 */
class ArtworkViewModel(application: Application) : AndroidViewModel(application) {
    private val cRepository: ArtworkRepository

    // LiveData for holding all artworks
    val allArtworks = MutableLiveData<List<Artwork>>()

    // LiveData for holding the selected artwork ID
    val selectedId = MutableLiveData<Int>()

    val isLoading = MutableLiveData<Boolean>()

    /**
     * Initialize the ViewModel and fetch artworks for the default artwork type（painting）.
     */
    init {
        cRepository = ArtworkRepository(application)
        viewModelScope.launch {
//            clearArtwork()
            fetchAllTypes(listOf(1, 2, 18, 3, 34, 5, 6, 23))
        }
    }

    /**
     * Fetch all artworks and update LiveData.
     */
    fun getAllArtworks() {
        viewModelScope.launch {
            try {
                allArtworks.value = cRepository.getAllArtworks()
                Log.i("allSubject", "Artworks: ${allArtworks.value}")
            } catch (e: Exception) {
                Log.e("ArtworkRepository", "Error getting artworks", e)
            }
        }
    }

    /**
     * Insert an artwork into the database.
     * @param artwork The artwork to insert.
     */
    fun insertArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(artwork)
    }

    /**
     * Update an artwork in the database.
     * @param artwork The artwork to update.
     */
    fun updateArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(artwork)
    }

    /**
     * Delete an artwork from the database.
     * @param artwork The artwork to delete.
     */
    fun deleteArtwork(artwork: Artwork) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(artwork)
    }

    /**
     * Clear all artworks from the database.
     */
    private fun clearArtwork() = viewModelScope.launch(Dispatchers.IO) {
        cRepository.clearAll()
    }

    /**
     * Fetch artworks by type ID.
     * @param artworkTypeId The type ID of the artworks to fetch.
     */
    fun getArtworksByTypeId(artworkTypeId: Int) {
        viewModelScope.launch {
            try {
                fetchArtworks(artworkTypeId)
                val artworks = cRepository.getArtworksByType(artworkTypeId)
                allArtworks.value = artworks
                selectedId.value = artworkTypeId
                allArtworks.observeForever { artworks ->
                    Log.i("allSubject", "Artworks: $artworks")
                }
            } catch (e: Exception) {
                Log.e("ArtworkRepository", "Error fetching artworks: ${e.message}", e)
            }
        }
    }

    /**
     * Fetch artworks from the API and store them in the database.
     * @param typeId The type ID of the artworks to fetch.
     * @param size The number of artworks to fetch.
     */
    private suspend fun fetchArtworks(typeId: Int = 1, size: Int = 50) {
        viewModelScope.launch {
            try {
                val response = cRepository.getResponse(typeId, size)
                response.artworks.filter {
                    it.id != null
                    it.imageId != null
                }.forEach { artwork ->
                    insertArtwork(artwork)
                    Log.i("Fetch Artwork", artwork.toString())
                }
            } catch (e: Exception) {
                Log.e("Error ", "Response failed" + e.message)
                e.printStackTrace()
            }
        }
    }

//    /**
//     * Fetch all artworks from the API and store them in the database.
//     * @param size The number of artworks to fetch.
//     */
//    private suspend fun fetchAll(size: Int = 100) {
//        viewModelScope.launch {
//            try {
//                Log.i("Fetching", "Fetching...")
//                val response = cRepository.fetchAll(size)
//                Log.d("Response", response.toString())
//                response.artworks.filter {
//                    it.id != null
//                    it.imageId != null
//                }.forEach { artwork ->
//                    insertArtwork(artwork)
//                    Log.i("Fetch Artwork", artwork.toString())
//                }
//                allArtworks.value = cRepository.getAllArtworks()
//            } catch (e: Exception) {
//                Log.e("Error ", "Response failed" + e.message)
//                e.printStackTrace()
//            }
//        }
//    }

    /**
     * Fetch artworks for all specified types.
     * @param types The list of type IDs for which artworks will be fetched.
     * @param size The number of artworks to fetch for each type.
     */
    private suspend fun fetchAllTypes(types: List<Int>, size: Int = 50) {
        var isAllTypesFetched = false
        types.forEach { typeId ->
            fetchArtworks(typeId, size)
        }
        isAllTypesFetched = true
        isLoading.value = !isAllTypesFetched
    }

    /**
     * Search artworks by keyword, the search logic is handled by api.
     * @param size The number of artworks to fetch.
     * @param title The title keyword to search for.
     */
    fun getByKey(size: Int = 50, title: String) {
        viewModelScope.launch {
            try {
                Log.i("Fetching", "Search...")
                val response = cRepository.fetchByKey(selectedId.value ?: 0, size, title)
                Log.d("Response", response.toString())
                response.artworks.filter {
                    it.id != null
                    it.imageId != null
                }.forEach { artwork ->
                    insertArtwork(artwork)
                    Log.i("Search Artwork", artwork.toString())
                }
                allArtworks.value = response.artworks
            } catch (e: Exception) {
                Log.e("Error ", "Response failed" + e.message)
                e.printStackTrace()
            }
        }
    }

    /**
     * Handle click event on artwork image.
     * @param imageId The ID of the clicked artwork image.
     */
    fun onImageClick(imageId: Int) {
        Log.i("ImageClicked", "Image ID: $imageId")
    }
}

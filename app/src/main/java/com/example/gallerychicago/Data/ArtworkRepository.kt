package com.example.gallerychicago.Data

import android.app.Application
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class ArtworkRepository(application: Application) {
    private val field = "id,image_id,artwork_type_id"
    private var artworkDao: ArtworkDao =
        ArtworkDatabase.getDatabase(application).artworkDAO()

    // Retrieves all artworks from the database asynchronously.
    suspend fun getAllArtworks(): List<Artwork> {
        return withContext(Dispatchers.IO) {
            artworkDao.getAllArtworks()
        }
    }

    suspend fun insert(artwork: Artwork) {
        artworkDao.insertArtwork(artwork)
    }

    suspend fun delete(artwork: Artwork) {
        artworkDao.deleteArtwork(artwork)
    }

    suspend fun update(artwork: Artwork) {
        artworkDao.updateArtwork(artwork)
    }

    // Clears all artwork records from the database.
    suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            artworkDao.clearAll()
        }
    }

    // Retrieves artworks by their type from the database asynchronously.
    suspend fun getArtworksByType(artworkTypeId: Int): List<Artwork> {
        Log.i("ArtworkRepository", "getArtworksByType: $artworkTypeId")
        return withContext(Dispatchers.IO) {

            artworkDao.getArtworksByType(artworkTypeId)
        }
    }

    // Retrofit service instance for network API calls related to artworks.
    private val artworkService = RetrofitInstance.api

    // Fetches a list of artworks from a network API based on type and size.
    suspend fun getResponse(type: Int, size: Int): ArtworkResponse {

        return artworkService.getArtworks(type, size, field)
    }

    // Fetches artworks by type, size, and a specific title from a network API.
    suspend fun fetchByKey(type: Int, size: Int, title: String): ArtworkResponse {
        if (type == 0) return artworkService.getAllTypesByKey(size, field, title)
        else return artworkService.getArtworksByKey(type, size, field, title)
    }
}
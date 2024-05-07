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

    suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            artworkDao.clearAll()
        }
    }

    suspend fun getArtworksByType(artworkTypeId: Int): List<Artwork> {
        Log.i("ArtworkRepository", "getArtworksByType: $artworkTypeId")
        return withContext(Dispatchers.IO) {

            artworkDao.getArtworksByType(artworkTypeId)
        }
    }


    private val artworkService = RetrofitInstance.api

    suspend fun getResponse(type: Int, size: Int): ArtworkResponse {

        return artworkService.getArtworks(type, size, field)
    }

//    suspend fun fetchAll(size: Int): ArtworkResponse {
//        return artworkService.init(size, field)
//    }

    suspend fun fetchByKey(type: Int, size: Int, title: String): ArtworkResponse {
        if (type == 0) return artworkService.getAllTypesByKey(size, field, title)
        else return artworkService.getArtworksByKey(type, size, field, title)
    }
}
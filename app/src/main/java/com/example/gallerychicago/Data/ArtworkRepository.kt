package com.example.gallerychicago.Data

import android.app.Application
import kotlinx.coroutines.flow.Flow
class ArtworkRepository (application: Application) {
    private var artworkDao: ArtworkDao =
        ArtworkDatabase.getDatabase(application).artworkDAO()
    val allArtworks: Flow<List<Artwork>> = artworkDao.getAllArtworks()
    suspend fun insert(artwork: Artwork) {
        artworkDao.insertSubject(artwork)
    }
    suspend fun delete(artwork: Artwork) {
        artworkDao.deleteSubject(artwork)
    }
    suspend fun update(artwork: Artwork) {
        artworkDao.updateSubject(artwork)
    }

    private val artworkService = RetrofitInstance.artworkService

    suspend fun getResponse(type: Int, size: Int): ArtworkResponse {
        return artworkService.getArtworks(
            "{bool:{must:[{term:" +
                    "{artwork_type_id:1}},{exists:{field:image_id}}]}}",
            size,
            "[id, image_id, artwork_type_id]"
        )
    }
}
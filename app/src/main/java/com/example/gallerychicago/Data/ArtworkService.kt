package com.example.gallerychicago.Data

import retrofit2.http.GET
import retrofit2.http.Query

interface ArtworkService {
    @GET("artworks/search?")
    suspend fun getArtworks(
        @Query("query") query: String,
        @Query("size") size: Int,
        @Query("fields") fields: String
    ): ArtworkResponse
}

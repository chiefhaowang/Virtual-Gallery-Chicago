package com.example.gallerychicago.Data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtworkService {
    @GET("artworks/search")
    fun searchArtworks(
        @Query("params") params: String
    ): Call<ArtworkResponse>
}

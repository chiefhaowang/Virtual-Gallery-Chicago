package com.example.gallerychicago.Data

import com.google.gson.JsonElement
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ArtworkDetailsService {
    @GET("artworks")
    fun getArtworkDetails(
        @Query("ids") ids: Int,
        @Query("fields") fields: String
    ): Call<JsonElement>
}
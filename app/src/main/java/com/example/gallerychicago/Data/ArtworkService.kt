package com.example.gallerychicago.Data

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface ArtworkService {
    // get artworks by type
    @GET("artworks/search?")
    suspend fun getArtworks(
        @Query("query[term][artwork_type_id]") Id: Int,
        @Query("size") size: Int,
        @Query("fields") fields: String
    ): ArtworkResponse

//    @GET("artworks")
//    suspend fun init(
//        @Query("size") size: Int,
//        @Query("fields") fields: String
//    ): ArtworkResponse

    // get artworks by type and keywords
    @GET("artworks/search?")
    suspend fun getArtworksByKey(
        @Query("query[term][artwork_type_id]") Id: Int,
        @Query("size") size: Int,
        @Query("fields") fields: String,
        @Query("q") title: String
    ): ArtworkResponse

    // get artworks by keywords only
    @GET("artworks/search?")
    suspend fun getAllTypesByKey(
        @Query("size") size: Int,
        @Query("fields") fields: String,
        @Query("q") title: String
    ): ArtworkResponse

    @GET("artworks/search")
    fun searchArtworks(
        @Query("params") params: String
    ): ArtworkResponse
}

package com.example.gallerychicago.Data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// instance of Retrofit
object RetrofitInstance {
    private const val BASE_URL = "https://api.artic.edu/api/v1/"

    val api: ArtworkService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArtworkService::class.java)
    }
}
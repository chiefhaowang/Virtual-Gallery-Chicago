package com.example.gallerychicago.Data

import com.google.gson.annotations.SerializedName

// Setting up the database, using SerializedName to identify the field
data class ArtworkResponse(
    @SerializedName("data")
    val artworks: List<Artwork> = ArrayList()
)



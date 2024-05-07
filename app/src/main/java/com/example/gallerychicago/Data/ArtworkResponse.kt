package com.example.gallerychicago.Data

import com.google.gson.annotations.SerializedName

// Setting up the database, using SerializedName to identify the field
data class ArtworkResponse(
    @SerializedName("data")
    val artworks: List<Artwork> = ArrayList()
)

// Retrieving data for details
data class ArtworkDetails(
    val id: Int? = 0,
    val title: String? = "",
    val description: String? = "",
    val artist: String? = "",
    val typeId: Int? = 0,
    val imageId: String? = ""
)

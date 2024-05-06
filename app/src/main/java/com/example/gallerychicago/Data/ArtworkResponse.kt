package com.example.gallerychicago.Data

import com.google.gson.annotations.SerializedName

// using SerializedName to identify the field
data class ArtworkData(
    val id: Int,
    @SerializedName("image_id") val imageId: String,
    @SerializedName("artwork_type_id") val artworkTypeId: Int
)

// Data list contains artwork information
data class ArtworkResponse(
    val data: List<ArtworkData>
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

package com.example.gallerychicago.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "artworks")
data class Artwork(
    @PrimaryKey val id: Int,
    @SerializedName("image_id") val imageId: String,
    @SerializedName("artwork_type_id") val artworkTypeId: Int,
    val favourite: Boolean = false,
    val favouriteCount: Int = 0
)
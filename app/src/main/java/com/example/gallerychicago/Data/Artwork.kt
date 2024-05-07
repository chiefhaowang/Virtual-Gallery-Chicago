package com.example.gallerychicago.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


// Table "artworks"
@Entity(tableName = "artworks")
data class Artwork(
    @SerializedName("id")
    @PrimaryKey val id: Int,
    @SerializedName("image_id") val imageId: String,
    @SerializedName("artwork_type_id") val artworkTypeId: Int
)
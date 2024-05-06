package com.example.gallerychicago.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// Table "artworks"
@Entity(tableName = "artworks")
data class Artwork(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "image_id")
    val imageId: String,
    @ColumnInfo(name = "artwork_type_id")
    val artworkTypeId: Int,
)
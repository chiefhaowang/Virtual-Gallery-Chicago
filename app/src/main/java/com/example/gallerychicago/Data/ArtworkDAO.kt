package com.example.gallerychicago.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// methods to operate the database
@Dao
interface ArtworkDao {
    @Query("SELECT * FROM artworks")
    fun getAllArtworks(): List<Artwork>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtwork(artwork: Artwork)

    @Update
    suspend fun updateArtwork(artwork: Artwork)

    @Delete
    suspend fun deleteArtwork(artwork: Artwork)

    @Query("SELECT * FROM artworks WHERE artworkTypeId = :TypeId")
    suspend fun getArtworksByType(TypeId: Int): List<Artwork>

    @Query("DELETE FROM artworks")
    suspend fun clearAll()
}
package com.example.gallerychicago.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// methods to operate the database
@Dao
interface ArtworkDao {
    // using Flow to dynamically acquire live updates
    @Query ("SELECT * FROM artworks")
    fun getAllArtworks(): Flow<List<Artwork>>

    // Check if the database is empty
    @Query("SELECT COUNT(id) FROM artworks")
    suspend fun countArtworks(): Int
    @Insert
    fun insertArtwork(artwork: Artwork)

    @Update
    fun updateArtwork(artwork: Artwork)

    @Delete
    fun deleteArtwork(artwork: Artwork)
}
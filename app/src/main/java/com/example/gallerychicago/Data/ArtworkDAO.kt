package com.example.gallerychicago.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtworkDao {
    @Query("SELECT * FROM artworks")
    fun getAllArtworks(): Flow<List<Artwork>>

    @Insert
    suspend fun insertSubject(artwork: Artwork)
    @Update
    suspend fun updateSubject(artwork: Artwork)
    @Delete
    suspend fun deleteSubject(artwork: Artwork)
}
package com.example.gallerychicago.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Artwork::class, User::class], version = 4, exportSchema = false)
abstract class ArtworkDatabase : RoomDatabase() {
    abstract fun artworkDAO(): ArtworkDao
    abstract fun userDAO(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: ArtworkDatabase? = null
        fun getDatabase(context: Context): ArtworkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArtworkDatabase::class.java,
                    "artwork_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
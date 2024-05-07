package com.example.gallerychicago.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// user table (primary key: id, attributes: name, email, birthday, description)
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password:String,
    @ColumnInfo(name = "birthday") val birthday: String?,
    @ColumnInfo(name = "description") val description: String?
)
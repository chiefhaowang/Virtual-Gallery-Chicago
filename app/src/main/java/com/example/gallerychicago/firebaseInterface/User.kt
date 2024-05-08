package com.example.gallerychicago.firebaseInterface

data class User(
    val email: String? = null,
    val favouriteArtworks: MutableList<FavouriteArtwork>? = null,
    val likedArtworks: MutableList<LikedArtwork>? = null
)
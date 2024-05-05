package com.example.gallerychicago.firebaseInterface

data class User(
    val email: String? = null,
    val favouriteArtworks: List<FavouriteArtwork>? = null,
    val likedArtworks: List<LikedArtwork>? = null
)
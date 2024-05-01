package com.example.gallerychicago.fireBaseConfig

data class User(
    val userId: String? = null,
    val userName: String? = null,
    val userBirthday: String? = null,
    val favouriteArts: List<FavouriteArts>,
    val likedArts: List<LikedArts>
)
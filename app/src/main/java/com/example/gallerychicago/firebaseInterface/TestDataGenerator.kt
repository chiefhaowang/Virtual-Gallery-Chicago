package com.example.gallerychicago.firebaseInterface

class TestDataGenerator {
    val cloudInterface = CloudInterface()

    fun userDataGenerator(email: String, artworkId: Int, typeId: Int){
        val likedArtwork = LikedArtwork(artworkId)
        val favouriteArtwork = FavouriteArtwork(artworkId, typeId)

        val likedArtworks: MutableList<LikedArtwork> = mutableListOf()
        likedArtworks.add(likedArtwork)

        val favouriteArtworks: MutableList<FavouriteArtwork> = mutableListOf()
        favouriteArtworks.add(favouriteArtwork)

        val user = User(cloudInterface.userIdGenerator(email), favouriteArtworks, likedArtworks )

        cloudInterface.writeUserInfo(email, user)

    }

}
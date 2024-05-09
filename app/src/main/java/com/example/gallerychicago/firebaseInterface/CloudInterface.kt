package com.example.gallerychicago.firebaseInterface

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue


class CloudInterface {
    private lateinit var database: DatabaseReference

    init {
        database = Firebase.database.reference
    }
    fun initializaDbRef(){

    }

    // delete the "." in email: google databse dont permit the special character in index
    fun userIdGenerator(email: String): String {
        return email.replace(".", "")
    }


    /** User Data Streaming
     function for user registration
     Use case: Every user registration will call this function
     Functionality: Simply build a new user row with email*/
    fun initializeUser( email: String ){
        val favouriteArtworks: MutableList<FavouriteArtwork> = mutableListOf()
        val likedArtworks: MutableList<LikedArtwork> = mutableListOf()
        val likedArtwork = LikedArtwork(27992)
        val favouriteArtwork = FavouriteArtwork(27992, "Default", 1)

        likedArtworks.add(likedArtwork)
        favouriteArtworks.add(favouriteArtwork)

        val user = User(userIdGenerator(email), favouriteArtworks, likedArtworks)




        database.child("users").child(userIdGenerator(email)).setValue(user)
            .addOnSuccessListener {
                println("user data initialized")
            }
            .addOnFailureListener {
                println("Data adding refused")
            }
    }

    /**function for user google login
    // Use case: Every user google login will call this function
    // Functionality: check the email has been used then decide to create a user or not*/
    fun initializeUserGoogleLogin(email: String, callback: (User?) -> Unit){
        database.child("users").child(userIdGenerator(email)).get()
            .addOnSuccessListener { it ->
                if (it.exists()) {
                    val user = it.getValue(User::class.java)
                    callback(user)
                } else {
                    initializeUser(email)
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Failed to read user info")
                callback(null)
            }
    }

    // Comment to Cenglong: use this to write user data(generate test data manually)
    fun writeUserInfo(email: String, user: User){
        database.child("users").child(userIdGenerator(email)).setValue(user)
            .addOnSuccessListener {
                println("user data added")
            }
            .addOnFailureListener {
                println("Data adding refused")
            }
    }

    // Comment to Chenglong: Use this to read user data to get user likes and favourite list
    fun readUserInfo(email: String, callback: (User?) -> Unit){
        database.child("users").child(userIdGenerator(email)).get()
            .addOnSuccessListener { it ->
                if (it.exists()) {
                    val user = it.getValue(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Failed to read user info")
                callback(null)
            }
    }


    fun readUserFavourite(email: String, callback: (List<PieEntry>?) -> Unit) {
        // Use the provided types map directly
        val typeNames = mapOf(
            1 to "Painting",
            2 to "Photograph",
            18 to "Print",
            3 to "Sculpture",
            34 to "Architectural",
            5 to "Textile",
            6 to "Furniture",
            23 to "Vessel"
        )

        readUserInfo(email) { user ->
            if (user?.favouriteArtworks != null && user.favouriteArtworks.isNotEmpty()) {
                val total = user.favouriteArtworks.size.toDouble()
                val typeCount = user.favouriteArtworks.groupingBy { it.type ?: 0 }.eachCount()

                // Create a list of PieEntry objects based on the actual data
                val pieEntries = typeCount.map { (type, count) ->
                    val percentage = (count / total * 100).toFloat()
                    val label = typeNames[type] ?: "Unknown"
                    PieEntry(percentage, label)
                }


                //println(pieEntries) // Debugging output to ensure data is processed

                println("========" + pieEntries) // Debugging output to ensure data is processed

                callback(pieEntries) // Return the list of PieEntry objects via the callback
            } else {
                println("No user or favourite artworks found")
                callback(null) // Handle cases where no artworks are found
            }
        }
    }

//    fun readUserFavourite(email: String):List<PieEntry>{
//        val typeNames = mapOf(
//            1 to "Painting",
//            2 to "Photograph",
//            18 to "Print",
//            3 to "Sculpture",
//            34 to "Architectural",
//            5 to "Textile",
//            6 to "Furniture",
//            23 to "Vessel"
//        )
//
//        readUserInfo(email) { user ->
//            if (user?.favouriteArtworks != null && user.favouriteArtworks.isNotEmpty()) {
//                val total = user.favouriteArtworks.size.toDouble()
//                val typeCount = user.favouriteArtworks.groupingBy { it.type ?: 0 }.eachCount()
//
//                // Create a list of PieEntry objects based on the actual data
//                val pieEntries = typeCount.map { (type, count) ->
//                    val percentage = (count / total * 100).toFloat()
//                    val label = typeNames[type] ?: "Unknown"
//                    PieEntry(percentage, label)
//                }
//
//                println(pieEntries) // Debugging output to ensure data is processed
//                return pieEntries // Return the list of PieEntry objects via the callback
//            } else {
//                println("No user or favourite artworks found")
//
//            }
//        }
//    }


    // Favourite artworks changes data streaming
    fun addFavouriteArtworkUser(email: String, favouriteArtwork: FavouriteArtwork){
        println(favouriteArtwork)
        readUserInfo(email){
            var user = User()
            var favouriteArtworks: MutableList<FavouriteArtwork> = mutableListOf()
            if (it != null) {
                println(it.favouriteArtworks)
                favouriteArtworks = it.favouriteArtworks as MutableList<FavouriteArtwork>
            }
            favouriteArtworks.add(favouriteArtwork)
            if (it != null) {
                user = User(userIdGenerator(email),favouriteArtworks, it.likedArtworks)
            }
            if (it != null) {
                writeUserInfo(it.email.toString(), user)
                println("User favourite artwork ahs been added in cloud")
            }
        }
    }

    fun cancelFavouriteArtworkUser(email: String, artworkId: Int){
        readUserInfo(email){
            var user = User()
            var favouriteArtworks: MutableList<FavouriteArtwork> = mutableListOf()
            if (it != null) {
                println(it.favouriteArtworks)
                favouriteArtworks = it.favouriteArtworks as MutableList<FavouriteArtwork>
            }
            // Delete the artwork record
            val iterator = favouriteArtworks.iterator()
            while (iterator.hasNext()) {
                val artwork = iterator.next()
                if (artwork.artworkId == artworkId) {
                    iterator.remove()
                    break
                }
            }
            if (it != null) {
                user = User(userIdGenerator(email),favouriteArtworks, it.likedArtworks)
            }
            if (it != null) {
                writeUserInfo(it.email.toString(), user)
                println("User favourite artwork ahs been deleted in cloud")
            }
        }
    }

    // liked artworks data in user object
    // Not completed
    fun addLikedArtworkUser(email: String, artworkId: Int){
        readUserInfo(email){
            var user = User()
            val likedArtwork = LikedArtwork(artworkId)
            var likedArtworks: MutableList<LikedArtwork> = mutableListOf()
            if (it != null) {
                println(it.likedArtworks)
                likedArtworks = it.likedArtworks as MutableList<LikedArtwork>
            }

            likedArtworks.add(likedArtwork)
            if (it != null) {
                user = User(userIdGenerator(email),it.favouriteArtworks, likedArtworks)
            }
            if (it != null) {
                writeUserInfo(it.email.toString(), user)
                println("User likes data has been added into cloud")
            }
        }
    }

    fun cancelLikedArtworkUser(email: String, artworkId: Int){
        readUserInfo(email){
            var user = User()

            var likedArtworks: MutableList<LikedArtwork> = mutableListOf()
            if (it != null) {
                println(it.likedArtworks)
                likedArtworks = it.likedArtworks as MutableList<LikedArtwork>
            }
            val iterator = likedArtworks.iterator()
            while (iterator.hasNext()) {
                val artwork = iterator.next()
                if (artwork.artworkId == artworkId) {
                    iterator.remove()
                    break
                }
            }
            if (it != null) {
                user = User(userIdGenerator(email),it.favouriteArtworks, likedArtworks)
            }
            if (it != null) {
                writeUserInfo(it.email.toString(), user)
                println("User likes data has been deleted from cloud")
            }
        }
    }



    // all changes happened while add or cancel likes (user object change and likes amount change)
    fun addArtworkLikeCloud(userId: String, artworkId: Int){
        // Add liked artwork like in user object
        addLikedArtworkUser(userId, artworkId)

        // Change the entire user likes data
        readArtworkLikes(artworkId.toString()) {
            val likesAmount = it + 1
            println("Likes amount at present: $likesAmount")
            writeArtworkLikes(artworkId.toString(), likesAmount)
        }

    }

    fun cancelArtworkLikeCloud(email: String, artworkId: Int){

        // Add liked artwork like in user object
        cancelLikedArtworkUser(email, artworkId)

        // Change the entire user likes data
        readArtworkLikes("artwork-one") {
            val likesAmount = it - 1
            println("Likes amount at present: $likesAmount")
            writeArtworkLikes(artworkId.toString(), likesAmount)
        }
    }


    // ArtworkLikes amount Data Streaming
    // Enforce write the new value to existing or non-existing index
    fun writeArtworkLikes(artworksId: String, likesAmount: Int){
        //val artworkLikesAmount = LikesAmount(artworksId, likesAmount)

        database.child("likesAmount").child(artworksId).setValue(likesAmount)
            .addOnSuccessListener {
                println("Likes amount data added")
            }
            .addOnFailureListener {
                println("Likes amount data adding refused")
            }
    }

    /** Callback function: return likesAmount at present
     use this to read the likes amount data by artwork id. When you use the function, it would be like this:
            firebaseConnection.readArtworkLikes(artworkId){
               println(it) // "it" is the return value of readArtworkLikes
            }*/
    fun readArtworkLikes(artworksId: String, callback: (Int) -> Unit) {
        database.child("likesAmount").child(artworksId).get()
            .addOnSuccessListener { it ->
                if (it.exists()) {
                    val likesAmount = it.value.toString().toInt()
                    callback(likesAmount)
                } else {
                    callback(0)
                }
            }
            .addOnFailureListener {
                println("Data read refused")
                callback(0)
            }
    }


    // Firebase database connection test
    fun basicReadWrite() {

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("test-message-gallery")

        myRef.setValue("this is an initial test for the gallery project")

        // keep listening from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                println(value)
                Log.d(ContentValues.TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun userDataGeneratorInit(email: String, artworkId: Int, title: String, typeId: Int){
        val likedArtwork = LikedArtwork(artworkId)
        val favouriteArtwork = FavouriteArtwork(artworkId, title, typeId)

        val likedArtworks: MutableList<LikedArtwork> = mutableListOf()
        likedArtworks.add(likedArtwork)

        val favouriteArtworks: MutableList<FavouriteArtwork> = mutableListOf()
        favouriteArtworks.add(favouriteArtwork)

        val user = User(userIdGenerator(email), favouriteArtworks, likedArtworks )

        writeUserInfo(email, user)

    }

    fun userDataGeneratorAdd(email: String, artworkId: Int, title: String, typeId: Int){
        readUserInfo(email){
            var user = User()
            val favouriteArtwork = FavouriteArtwork(artworkId, title, typeId)
            var favouriteArtworks: MutableList<FavouriteArtwork> = mutableListOf()
            if (it != null) {
                println(it.favouriteArtworks)
                favouriteArtworks = it.favouriteArtworks as MutableList<FavouriteArtwork>
            }
            favouriteArtworks.add(favouriteArtwork)
            if (it != null) {
                user = User(userIdGenerator(email),favouriteArtworks, it.likedArtworks)
            }
            if (it != null) {
                writeUserInfo(it.email.toString(), user)
            }
        }
    }
}
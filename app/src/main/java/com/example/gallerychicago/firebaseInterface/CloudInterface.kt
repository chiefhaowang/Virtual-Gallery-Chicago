package com.example.gallerychicago.firebaseInterface

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class CloudInterface {
    private lateinit var database: DatabaseReference

    fun initializaDbRef(){
        database = Firebase.database.reference
    }

    // User Data Streaming

    // Comment to Garfield: function for user registration
    // Use case: Every user registration will call this function
    // Functionality: Simply build a new user row with email
    // userId: unique index for each user
    fun initializeUser( email: String ){
        val favouriteArtworks: List<FavouriteArtwork> = listOf()
        val likedArtworks: List<LikedArtwork> = listOf()
        val user = User(email, favouriteArtworks, likedArtworks)

        database.child("users").child(email).setValue(user)
            .addOnSuccessListener {
                println("user data initialized")
            }
            .addOnFailureListener {
                println("Data adding refused")
            }
    }

    // Comment to Garfield: function for user google login
    // Use case: Every user google login will call this function
    // Functionality: check the email has been used then decide to create a user or not
    fun initializeUserGoogleLogin(email: String, callback: (User?) -> Unit){
        database.child("users").child(email).get()
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

    fun writeUserInfo(email: String, user: User){
        database.child("users").child(email).setValue(user)
            .addOnSuccessListener {
                println("user data added")
            }
            .addOnFailureListener {
                println("Data adding refused")
            }
    }

    fun readUserInfo(email: String, callback: (User?) -> Unit){
        database.child("users").child(email).get()
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


    // User Liked artworks data streaming
    // Not completed
    fun addLikedArtwork(email: String, artworkId: String){

    }

    fun cancelLikedArtwork(email: String, artworkId: String){

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

    // Callback function: return likesAmount at present
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


    // Not completed
    fun addSingleArtworkLike(userId: String, artworkId: String){
        // Add liked artwork like in user object
        addLikedArtwork(userId, artworkId)

        // Change the entire user likes data
        readArtworkLikes(artworkId) {
            val likesAmount = it + 1
            println("Likes amount at present: $likesAmount")
            writeArtworkLikes(artworkId, likesAmount)
        }

    }

    fun cancelArtworkLike(email: String, artworkId: String){

        // Add liked artwork like in user object
        cancelLikedArtwork(email, artworkId)

        // Change the entire user likes data
        readArtworkLikes("artwork-one") {
            val likesAmount = it - 1
            println("Likes amount at present: $likesAmount")
            writeArtworkLikes(artworkId, likesAmount)
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
}
package com.example.gallerychicago.Data

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response

//Repository is a layer between UI and database
class ArtworkRepository (application: Application, private val scope: CoroutineScope) {

    private var artworkDao: ArtworkDao = ArtworkDatabase.getDatabase(application).artworkDAO()
    // get all artworks
    val allArtworks: Flow<List<Artwork>> = artworkDao.getAllArtworks()

    // return boolean
    suspend fun isDatabaseEmpty(): Boolean {
        return artworkDao.countArtworks() == 0
    }
    // insert new artwork
    suspend fun insert(artwork: Artwork) {
        artworkDao.insertArtwork(artwork)
    }

    //delete artwork
    suspend fun delete(artwork: Artwork) {
        artworkDao.deleteArtwork(artwork)
    }

    //update artwork
    suspend fun update(artwork: Artwork) {
        artworkDao.updateArtwork(artwork)
    }

    // generateParams contains three parameters to construct the request
     private fun generateParams(size: Int, artworkTypeId: Int, fields: Array<String>): String {
        val paramsMap = mapOf(
            "query" to mapOf(
                "bool" to mapOf(
                    "must" to listOf(
                        mapOf("term" to mapOf("artwork_type_id" to artworkTypeId)),
                        mapOf("exists" to mapOf("field" to "image_id"))
                    )
                )
            ),
           "size" to size,
            "fields" to fields
        )
        return Gson().toJson(paramsMap)
    }


    /**
     * Fetches artworks using the specified parameters and stores them in the database.
     * @param size The number of artworks to fetch.
     * @param artworkTypeId The type of artwork to fetch.
     * @param fields The data fields to retrieve for each artwork.
     */
    fun fetchArtworks(size: Int, artworkTypeId: Int, fields: Array<String>) {
        val params = generateParams(size, artworkTypeId, fields)
        RetrofitInstance.api.searchArtworks(params).enqueue(object : Callback<ArtworkResponse> {
            override fun onResponse(call: Call<ArtworkResponse>, response: Response<ArtworkResponse>) {
                if (response.isSuccessful) {
                    val artworks = response.body()?.data
                    if (artworks.isNullOrEmpty()) {
                        Log.e("ArtworkRepository", "No artworks received from the API.")
                    } else {
                        artworks.forEach { artworkData ->
                            storeArtworkInDatabase(artworkData)
                        }
                    }
                } else {
                    // Log error response
                    Log.e("ArtworkRepository", "Failed to fetch artworks: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ArtworkResponse>, t: Throwable) {
                // Log network or other errors
                Log.e("ArtworkRepository", "Network error or other failure while fetching artworks", t)
            }
        })
    }

    // the Artwork data will be stored in the database
    private fun storeArtworkInDatabase(artworkData: ArtworkData) {
        scope.launch(Dispatchers.IO) {
            val artwork = Artwork(
                id = artworkData.id,
                imageId = artworkData.imageId,
                artworkTypeId = artworkData.artworkTypeId,
                favourite = false,
                favouriteCount = 0
            )
            artworkDao.insertArtwork(artwork)
        }
    }
}
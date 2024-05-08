package com.example.gallerychicago.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gallerychicago.Data.ArtworkDetailsResponse
import com.example.gallerychicago.Data.ArtworkDetailsService
import com.example.gallerychicago.R
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// Enterence func: waiting for the data fetch operation done then display the data out
@Composable
fun DisplayArtworkDetails(artworkId: Int) {
    var artworkDetails by remember { mutableStateOf<ArtworkDetailsResponse?>(null) }
    println("Receive image ID from other pages: $artworkId")
    LaunchedEffect(Unit) {
        println("1")
        fetchArtworkDetails(artworkId) {
            println("2")
            if (it != null) {
                artworkDetails = it
                println("API Response: $artworkDetails")
            }
            else{
                println("Got a null json reply")
            }
        }
    }
    if (artworkDetails != null) {
        println("Artwork Details page can be called")
        ArtworkDetials(artworkDetails!!)
    } else {
        println("")
    }
}

@Composable
fun ArtworkDetials(artworkDetails: ArtworkDetailsResponse) {
    println("Artwork Details page has been called")
    var isLikedClicked by remember { mutableStateOf(false) }
    var isFavouriteClicked by remember { mutableStateOf(false) }
    val iconlikes = if (isLikedClicked) {
        Icons.Filled.Favorite
    } else {
        Icons.Filled.FavoriteBorder
    }
    val iconfavourite = if ( isFavouriteClicked) {
        Icons.Filled.Star
    } else {
        Icons.Outlined.AddCircle
    }

    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {

            Text(
                text = "Artwork Details",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
//            contentAlignment = Alignment.Center
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
            ) {
                ImageDisplay("https://www.artic.edu/iiif/2/${artworkDetails.imageId}/full/400,/0/default.jpg")
            }

            Text(
                text = artworkDetails.title.toString(),
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(20.dp, 0.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = artworkDetails.artist.toString(),
                    modifier = Modifier.padding(20.dp, 0.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontFamily = FontFamily.Serif
                )

                IconButton(
                    onClick = { isLikedClicked = !isLikedClicked },
                    modifier = Modifier
                        .padding(start = 180.dp, end = 10.dp, top = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(iconlikes, contentDescription = "Like")
                }

                IconButton(
                    onClick = { isFavouriteClicked = !isFavouriteClicked },
                    modifier = Modifier
                        .padding(start = 10.dp, end = 0.dp, top = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(iconfavourite, contentDescription = "Favourite")
                }

            }

            Text(
                text = artworkDetails.description.toString(),
                modifier = Modifier.padding(20.dp, 20.dp),
                fontFamily = FontFamily.Serif,
                //color = colorResource(id = R.color.text_sub)
            )

        }


    }
}

@Composable
fun ImageDisplay(url: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .padding(10.dp, 4.dp)
            .fillMaxSize(),
        contentScale = ContentScale.FillWidth
    )
}

// use retrofit to retrieve data for details
fun fetchArtworkDetails(artworkId: Int, callback: (ArtworkDetailsResponse?) -> Unit){
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.artic.edu/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ArtworkDetailsService::class.java)

    service.getArtworkDetails(
        artworkId,
        "id,title,short_description,artist_title,artwork_type_id,image_id"
    )
        .enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val jsonResponse = response.body()?.toString()
                println("JSON response is: $jsonResponse")
                val artworkDetails = parseArtworkDetails(jsonResponse)
                println("Parsed JSON pack: $artworkDetails")
                callback(artworkDetails)
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                callback(null)
            }
        })
}

// Convert the response json to object
//fun parseArtworkDetails(jsonResponse: String?): ArtworkDetailsResponse? {
//    if (jsonResponse == null){
//        println("Have got a empty json pack")
//        return null}
//
//    val jsonObject = JsonParser.parseString(jsonResponse).asJsonObject
//    println("JSON object is(After parsering): $jsonObject")
//    val data = jsonObject.getAsJsonArray("data")
//    if (data.size() > 0) {
//        val artworkData = data[0].asJsonObject
//        println("Finalised JSON is: $artworkData")
//        val artworkDetailsResponse = ArtworkDetailsResponse(
//            artworkData.get("id")?.asInt ?: 27992,
//            artworkData.get("title")?.asString ?: "",
//            artworkData.get("short_description")?.asString ?: "",
//            artworkData.get("artist_title")?.asString ?: "",
//            artworkData.get("artwork_type_id")?.asInt ?: 0,
//            artworkData.get("image_id")?.asString ?: ""
//            )
//        println("The returned object is $artworkDetailsResponse")
//
//        return artworkDetailsResponse
//    }
//
//    return null
//}

fun parseArtworkDetails(jsonResponse: String?): ArtworkDetailsResponse? {
    try {
        if (jsonResponse.isNullOrEmpty()) {
            println("JSON response is empty or null")
            return null
        }

        val jsonObject = JsonParser.parseString(jsonResponse).asJsonObject
        val data = jsonObject.getAsJsonArray("data")
        if (data.size() > 0) {
            val artworkData = data[0].asJsonObject
            val id = artworkData.get("id")?.asInt ?: return null
            val title = artworkData.get("title")?.asString ?: ""
            val shortDescription = artworkData.get("short_description")?.asString ?: ""
            val artistTitle = artworkData.get("artist_title")?.asString ?: ""
            val artworkTypeId = artworkData.get("artwork_type_id")?.asInt ?: 0
            val imageId = artworkData.get("image_id")?.asString ?: ""

            println("Parsed ArtworkDetailsResponse: { id=$id, title='$title', shortDescription='$shortDescription', artistTitle='$artistTitle', artworkTypeId=$artworkTypeId, imageId='$imageId' }")

            return ArtworkDetailsResponse(id, title, shortDescription, artistTitle, artworkTypeId, imageId)
        } else {
            println("No data found in JSON response")
        }
    } catch (e: Exception) {
        println("Error parsing JSON: ${e.message}")
    }

    return null
}
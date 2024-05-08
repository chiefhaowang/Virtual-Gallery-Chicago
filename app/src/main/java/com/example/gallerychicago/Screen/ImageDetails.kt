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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gallerychicago.Data.ArtworkDetailsResponse
import com.example.gallerychicago.Data.ArtworkDetailsService
import com.example.gallerychicago.R
import com.example.gallerychicago.firebaseInterface.CloudInterface
import com.example.gallerychicago.firebaseInterface.FavouriteArtwork
import com.example.gallerychicago.firebaseInterface.LikedArtwork
import com.example.gallerychicago.firebaseInterface.User
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// Enterence func: Async waiting for the data fetch operation done then display the data out
@Composable
fun DisplayArtworkDetails(artworkId: Int, navController: NavHostController) {
    var artworkDetails by remember { mutableStateOf<ArtworkDetailsResponse?>(null) }
    val cloudInterface = CloudInterface()
    cloudInterface.initializaDbRef()

    Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Artwork Details",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            // Fetching data and display logic
            val email = "wh.tenghe@gmail.com"
            var user = User()
            LaunchedEffect(Unit) {
                cloudInterface.readUserInfo(email) {
                    if (it != null) {
                        user = User(email, it.favouriteArtworks, it.likedArtworks)
                    } else {
                        println("The user info on cloud is null")
                    }
                }
            }

            println("Receive image ID from other pages: $artworkId")
            LaunchedEffect(Unit) {
                fetchArtworkDetails(artworkId) {
                    if (it != null) {
                        artworkDetails = it
                        println("API Response: $artworkDetails")
                    } else {
                        println("Got a null json reply")
                    }
                }
            }

            if (artworkDetails != null && user != null) {
                println("Artwork Details page can be called")
                ArtworkDetials(artworkDetails!!, email)
            } else {
                Text("")
            }
        }
    }
}


@Composable
fun ArtworkDetials(artworkDetails: ArtworkDetailsResponse, email: String) {
    println("Artwork Details page has been called")


    //Cloud service
    val cloudInterface = CloudInterface()
    cloudInterface.initializaDbRef()
    val likedArtwork = LikedArtwork(artworkId = artworkDetails.id)
    val favouriteArtwork = FavouriteArtwork(
        artworkId = artworkDetails.id,
        title = artworkDetails.title,
        type = artworkDetails.typeId
    )

    println("$email's art likes data has been given")
    //Icon tap event setting
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
                    onClick = {
                        if(isLikedClicked){
                            artworkDetails.id?.let { cloudInterface.cancelLikedArtworkUser(email.toString(), it.toInt()) }
                        }
                        else{
                            artworkDetails.id?.let { cloudInterface.addLikedArtworkUser(email.toString(), it.toInt()) }
                        }
                        isLikedClicked = !isLikedClicked
                              },
                    modifier = Modifier
                        .padding(start = 180.dp, end = 10.dp, top = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(iconlikes, contentDescription = "Like")
                }

                IconButton(
                    onClick = {
                        if(isFavouriteClicked){
                            artworkDetails.id?.let { cloudInterface.cancelFavouriteArtworkUser(email.toString(), artworkId = it.toInt()) }
                        }
                        else{
                            cloudInterface.addFavouriteArtworkUser(email = email.toString(),favouriteArtwork)
                        }
                        isFavouriteClicked = !isFavouriteClicked
                              },
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


fun parseArtworkDetails(jsonResponse: String?): ArtworkDetailsResponse? {

    var id = 27992
    var title = "Title not Found"
    var shortDescription = "Description not Found"
    var artistTitle = "Artist not Found"
    var artworkTypeId = 0
    var imageId = "2d484387-2509-5e8e-2c43-22f9981972eb"

    try {


        if (jsonResponse.isNullOrEmpty()) {
            println("JSON response is empty or null")
            return null
        }

        val jsonObject = JsonParser.parseString(jsonResponse).asJsonObject
        val data = jsonObject.getAsJsonArray("data")
        if (data.size() > 0) {
            val artworkData = data[0].asJsonObject
            id = artworkData.get("id")?.asInt ?: 27992
            title = artworkData.get("title")?.asString ?: "Title not Found"
            shortDescription = artworkData.get("short_description")?.asString ?: "Description not Found"
            artistTitle = artworkData.get("artist_title")?.asString ?: "Artist not Found"
            artworkTypeId = artworkData.get("artwork_type_id")?.asInt ?: 0
            imageId = artworkData.get("image_id")?.asString ?: "2d484387-2509-5e8e-2c43-22f9981972eb"

            println("Parsed ArtworkDetailsResponse: { id=$id, title='$title', shortDescription='$shortDescription', artistTitle='$artistTitle', artworkTypeId=$artworkTypeId, imageId='$imageId' }")

            return ArtworkDetailsResponse(id, title, shortDescription, artistTitle, artworkTypeId, imageId)
        } else {
            println("No data found in JSON response")
        }
    } catch (e: Exception) {
        println("Error parsing JSON: ${e.message}")
        return ArtworkDetailsResponse(id, title, shortDescription, artistTitle, artworkTypeId, imageId)
    }

    return null
}
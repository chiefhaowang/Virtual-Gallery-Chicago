package com.example.gallerychicago.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.gallerychicago.Data.ArtworkDetails
import com.example.gallerychicago.Data.ArtworkDetailsService
import com.example.gallerychicago.R
import com.example.gallerychicago.firebaseInterface.FavouriteArtwork
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
    var artworkDetails by remember { mutableStateOf<ArtworkDetails?>(null) }
    println("Receive image ID from other pages: $artworkId")
    LaunchedEffect(Unit) {
        fetchArtworkDetails(artworkId) {
            if (it != null) {
                artworkDetails = it
                println("API Response: $artworkDetails")
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
fun ArtworkDetials(artworkDetails: ArtworkDetails) {
    println("Artwork Details page has been called")
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
                    .height(250.dp),
//            contentAlignment = Alignment.Center
            ) {
                ImageDisplay("https://www.artic.edu/iiif/2/${artworkDetails.imageId}/full/400,/0/default.jpg")
            }

            Text(
                text = artworkDetails.title.toString(),
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(10.dp, 0.dp)
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
                Image(
                    painter = painterResource(id = R.drawable.icon_favourite),
                    contentDescription = "icon-favourite",
                    modifier = Modifier
                        .padding(start = 150.dp, end = 10.dp, top = 10.dp)
                        .size(20.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.icon_like),
                    contentDescription = "icon-favourite",
                    modifier = Modifier
                        .padding(start = 10.dp, end = 0.dp, top = 10.dp)
                        .size(20.dp)
                )
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
fun fetchArtworkDetails(artworkId: Int, callback: (ArtworkDetails?) -> Unit){
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
                callback(parseArtworkDetails(jsonResponse))
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                callback(null)
            }
        })
}

// Convert the response json to object
fun parseArtworkDetails(jsonResponse: String?): ArtworkDetails? {
    if (jsonResponse == null){
        println("Have got a empty json pack")
        return null}


    val jsonObject = JsonParser.parseString(jsonResponse).asJsonObject
    val data = jsonObject.getAsJsonArray("data")
    if (data.size() > 0) {
        val artworkData = data[0].asJsonObject
        return ArtworkDetails(
            artworkData.get("id").asInt,
            artworkData.get("title").asString,
            artworkData.get("short_description").asString,
            artworkData.get("artist_title").asString,
            artworkData.get("artwork_type_id").asInt,
            artworkData.get("image_id").asString
        )
    }

    return null
}
package com.example.gallerychicago.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gallerychicago.Data.ArtworkDetails
import com.example.gallerychicago.firebaseInterface.CloudInterface
import com.example.gallerychicago.firebaseInterface.FavouriteArtwork


@Composable
fun DisplayFavouriteList(email: String) {
    var favouriteArtworks by remember { mutableStateOf(emptyList<FavouriteArtwork>()) }

    LaunchedEffect(Unit) {
        fetchCloudData(email) { artworks ->
            if (artworks != null) {
                favouriteArtworks = artworks
            }
        }
    }

    UserFavourite(favouriteArtworks)
}
@Composable
fun UserFavourite(favouriteArtworks: List<FavouriteArtwork>) {
    println(favouriteArtworks)
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column (modifier = Modifier
            .fillMaxWidth()
        ){
            Text(
                text = "Favourite List",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = "Welcome to the favourite list!\nHow can I assist you today?",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            Text(
                text = "Your Favourite List",
                fontSize = 24.sp,
                color = Color.Black,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            LazyColumn {
                itemsIndexed(favouriteArtworks) { index, artwork ->
                    FavouriteArtworkItem(artwork, index + 1)
                }
            }

        }
    }

}

@Composable
fun FavouriteArtworkItem(artwork: FavouriteArtwork, index: Int) {
    Card(
        //elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Artwork: $index",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = artwork.title.toString(),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}


// Read cloud user data and callback a favourite artworks list
fun fetchCloudData(email: String, callback: (MutableList<FavouriteArtwork>?) -> Unit) {
    val cloudInterface = CloudInterface()
    cloudInterface.initializaDbRef()

    cloudInterface.readUserInfo(email) { userInfo ->
        if (userInfo != null) {
            val favouriteArtworks = userInfo.favouriteArtworks
            callback(favouriteArtworks)
        } else {
            println("User not found")
            callback(null)
        }
    }
}
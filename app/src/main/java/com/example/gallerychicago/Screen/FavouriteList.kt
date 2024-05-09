package com.example.gallerychicago.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gallerychicago.Data.UserViewModel
import com.example.gallerychicago.firebaseInterface.CloudInterface
import com.example.gallerychicago.firebaseInterface.FavouriteArtwork



@Composable
fun DisplayFavouriteList(navController: NavController, userViewModel: UserViewModel) {

    val currentUser by userViewModel.currentUser.observeAsState()
    //User Room retrieve data here
    val email = currentUser?.email ?: "not log in"
    var favouriteArtworks by remember { mutableStateOf(emptyList<FavouriteArtwork>()) }
    LaunchedEffect(Unit) {
        fetchCloudData(email) { artworks ->
            if (artworks != null) {
                favouriteArtworks = artworks
            }
        }
    }

    UserFavourite(favouriteArtworks, navController){
        //println("Image ID(passed from favourite list item): $it")
    }
}
@Composable
fun UserFavourite(favouriteArtworks: List<FavouriteArtwork>, navController: NavController,
                  onItemClick: (Int) -> Unit) {
    println("Favourite data retrieved: $favouriteArtworks")
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column (modifier = Modifier
            .fillMaxWidth().fillMaxHeight()
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
                    FavouriteArtworkItem(artwork, index + 1, navController, onItemClick)
                }
            }

        }
    }

}

@Composable
fun FavouriteArtworkItem(artwork: FavouriteArtwork, index: Int, navController: NavController,
                         onClick: (Int) -> Unit) {

    Card(
        //elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(artwork.artworkId?.toInt() ?: 0) }
            .clickable {
                navController.navigate("imageDetails/${artwork.artworkId}")
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "$index: ",
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = artwork.title.toString(),
                fontSize = 14.sp,
                fontFamily = FontFamily.Cursive,
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
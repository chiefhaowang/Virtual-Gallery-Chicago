package com.example.gallerychicago

import android.accounts.Account
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gallerychicago.ui.theme.GalleryChicagoTheme
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gallerychicago.Data.ArtworkViewModel
import com.example.gallerychicago.Data.UserViewModel
import com.example.gallerychicago.Screen.Registration
import com.example.gallerychicago.Screen.BottomNavigationBar
import com.example.gallerychicago.Screen.DisplayArtworkDetails
import com.example.gallerychicago.Screen.DisplayFavouriteList
import com.example.gallerychicago.Screen.UserFavourite
import com.example.gallerychicago.Screen.LoginScreen
import com.example.gallerychicago.Screen.ReportScreen

import com.example.gallerychicago.firebaseInterface.CloudInterface


class MainActivity : ComponentActivity() {
    private val artworkViewModel: ArtworkViewModel by viewModels()
    private  val userViewModel: UserViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GalleryChicagoTheme{
                // initialize navController
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    BottomNavigationBar(viewModel = artworkViewModel, userViewModel =userViewModel )
                    //ReportScreen(navController, userViewModel)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GalleryChicagoTheme{}
}
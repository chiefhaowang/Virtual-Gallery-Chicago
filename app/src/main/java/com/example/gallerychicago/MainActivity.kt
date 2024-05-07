package com.example.gallerychicago

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
import com.example.gallerychicago.Data.ArtworkViewModel
import com.example.gallerychicago.Screen.RegistrationScreen
import com.example.gallerychicago.Screen.BottomNavigationBar
import com.example.gallerychicago.Screen.UserFavourite

class MainActivity : ComponentActivity() {
    private val viewModel: ArtworkViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryChicagoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //BottomNavigationBar(viewModel = viewModel)
                    // UserFavourite()
                    //DisplayArtworkDetails(27992)
                    RegistrationScreen()

                }
            }
        }

        // Google firebase database interface test area ()
//        val firebaseConnection = CloudInterface()
//        firebaseConnection.initializaDbRef()
//        firebaseConnection.userDataGenerator("wh.tenghe@gmail.com", 27992, 1)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GalleryChicagoTheme{}
}
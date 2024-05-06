package com.example.gallerychicago

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
import com.example.gallerychicago.Data.ArtworkViewModel
import com.example.gallerychicago.Screen.BottomNavigationBar
import com.example.gallerychicago.Screen.Imagedetials
import com.example.gallerychicago.firebaseInterface.CloudInterface

class MainActivity : ComponentActivity() {
    private val viewModel: ArtworkViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryChicagoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    BottomNavigationBar(viewModel = viewModel)
                    Imagedetials()
                }
//                Imagedetials()
            }
        }

        // Google firebase database interface test area ()
//        val firebaseConnection = CloudInterface()
//        firebaseConnection.initializaDbRef()
//        //firebaseConnection.initializeUser("whtenghe@gmailcom")
//        firebaseConnection.readUserInfo("whtenghe@gmailcom"){
//            println(it)
//        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GalleryChicagoTheme{}
}
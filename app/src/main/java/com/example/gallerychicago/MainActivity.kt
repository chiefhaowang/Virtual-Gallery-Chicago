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
import com.example.gallerychicago.fireBaseConfig.Connection
import com.example.gallerychicago.ui.theme.GalleryChicagoTheme
import androidx.activity.viewModels
import com.example.gallerychicago.Data.ArtworkViewModel
import com.example.gallerychicago.Data.ArtworkViewModelFactory
import com.example.gallerychicago.Screen.BottomNavigationBar

class MainActivity : ComponentActivity() {
    private val viewModel: ArtworkViewModel by viewModels { ArtworkViewModelFactory(application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryChicagoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomNavigationBar(viewModel = viewModel)
                }
            }
        }
        // test the database connection
        val realtimeDatabaseConnection = Connection()
        realtimeDatabaseConnection.basicReadWrite()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GalleryChicagoTheme {
    }
}
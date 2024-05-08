package com.example.gallerychicago.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gallerychicago.Data.Artwork
import com.example.gallerychicago.Data.ArtworkViewModel

@Composable
// Composable function to display the exhibition screen.
fun Exhibition(navController: NavHostController, viewModel: ArtworkViewModel) {
    val artworks by viewModel.allArtworks.observeAsState()
    val selectedId by viewModel.selectedId.observeAsState()
    // Observe the loading state from the view model
    val isLoading by viewModel.isLoading.observeAsState(initial = true)

    // Fetch artworks of type 'painting' when the screen is launched
    LaunchedEffect(Unit) {
        viewModel.getArtworksByTypeId(1)
    }

    // Display the exhibition UI
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Display the exhibition title
            Text(
                text = "Artworks",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            // Display the art type selection dropdown
            ArtTypeSelection(onselect = { typeId ->
                viewModel.getArtworksByTypeId(typeId)
            })
            // Display the search bar
            SearchBar(onSearch = { search ->
                viewModel.getByKey(10, search)
            })
            // Display a loading indicator if isLoading is true
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                // Display the artworks
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                ) {
                    artworks?.let {
                        // Ensure artworks is not null and display the ExhibitionContent
                        ExhibitionContent(artworks = it, navController, onClick = { id ->
                            viewModel.onImageClick(id, navController)
                        })
                    }
                }
            }
        }
    }
}

@Composable
// Composable function to display the content of the exhibition.
fun ExhibitionContent(artworks: List<Artwork>, navController: NavHostController, onClick: (Int) -> Unit) {
    // Extract URLs of artworks' images
    val urls = artworks.mapNotNull { artwork ->
        artwork.imageId?.let { imageId ->
            "https://www.artic.edu/iiif/2/$imageId/full/200,/0/default.jpg"
        }
    }

    // Extract IDs of artworks
    val ids = artworks.mapNotNull { artwork ->
        artwork.id
    }

    // Display artworks in a staggered grid layout
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(urls) { url ->
            val index = urls.indexOf(url)
            val imageId = if (index != -1 && index < ids.size) ids[index] else null

            // Display each artwork item
            ExhibitionItem(
                url = url,
                onClick = {
                    imageId?.let { id ->
                        onClick(id)
                    }
                }
            )
            Log.i("Draw Artwork", url)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Composable function to display the dropdown menu for selecting artwork types.
fun ArtTypeSelection(onselect: (Int) -> Unit) {
    // Define map of artwork types with their IDs and names
    val types = mapOf(
        1 to "Painting",
        2 to "Photograph",
        18 to "Print",
        3 to "Sculpture",
        34 to "Architectural",
        5 to "Textile",
        6 to "Furniture",
        23 to "Vessel"
    )
    var isExpanded by remember { mutableStateOf(false) }
    var selectedState by remember { mutableStateOf(types.keys.first()) }
    Box {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .focusProperties {
                        canFocus = false
                    }
                    .padding(bottom = 8.dp),
                readOnly = true,
                value = types[selectedState] ?: "",
                onValueChange = { newvalue ->
                    selectedState = types.filterValues { it == newvalue }.keys.first()
                },
                label = {
                    Text(
                        text = "Artwork Type",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.surface)
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            )
            {
                // Display dropdown items for each artwork type
                types.forEach { (id, selectionOption) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectionOption,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            selectedState = id
                            isExpanded = false
                            Log.i("Selected", id.toString())
                            onselect(id)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@Composable
// Composable function to display each artwork item.
fun ExhibitionItem(url: String, onClick: (String) -> Unit) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .padding(0.dp, 4.dp)
            .fillMaxSize()
            .clickable {
                val imageId = url.substringAfterLast("/").substringBefore("/")
                onClick(imageId)
            },
        contentScale = ContentScale.FillWidth
    )
}

@Composable
// Composable function to display the search bar.
fun SearchBar(onSearch: (String) -> Unit) {
    var search by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { newValue ->
                    search = newValue
                    onSearch(newValue)
                },
                label = { Text("Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp)
            )
        }
    }
}

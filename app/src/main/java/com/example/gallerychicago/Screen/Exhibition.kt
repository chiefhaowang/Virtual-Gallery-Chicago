package com.example.gallerychicago.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.runtime.livedata.observeAsState
import com.example.gallerychicago.Data.Artwork
import com.example.gallerychicago.Data.ArtworkViewModel
import com.example.gallerychicago.R


@Composable
fun Exhibition(navController: NavHostController, viewModel: ArtworkViewModel) {
    val artworks by viewModel.allSubjects.observeAsState()
    val selectedSubject = remember { mutableStateOf<Artwork?>(null) }
    val insertDialog = remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Exhibition",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            ArtTypeSelection()
            SearchBar()
            Box(modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            ) {
//                LaunchedEffect(Unit) {
//                    gridState.scrollToItem(index = 0)
//                }
                LazyVerticalStaggeredGrid(
//                    state = gridState,
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val urls = listOf(
                        "https://www.artic.edu/iiif/2/2193cdda-2691-2802-0776-145dee77f7ea/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/ae62fa01-9823-797f-1780-2b965ee7d4f4/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/b0effb1c-ff23-bbaa-f809-9fd94e31c1a0/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/d0049e0b-bd55-020e-aa8c-b137d06ae7df/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/c12058f4-188f-c6ed-f0fe-52b32acfb296/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/66f95ea3-a11a-1cf4-6599-d0a49bb25744/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/95be2572-b53d-8e7b-abc9-10eb48d4fa5d/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/79b12530-6b59-0992-322d-149cf05d4ee5/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/1ef355df-6d67-cd4c-9791-4b959013b4d3/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/0f1cc0e0-e42e-be16-3f71-2022da38cb93/full/200,/0/default.jpg",
                        "https://www.artic.edu/iiif/2/8b3b54b1-2b35-d5c6-2577-914bceabd1ec/full/200,/0/default.jpg",
                    )
                    items(urls) { url ->
                        ExhibitionItem(url = url)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtTypeSelection() {
    val types = listOf("Painting", "Photograph", "Print", "Sculpture", "Architectural", "Textile", "Furniture", "Vessel")
    var isExpanded by remember { mutableStateOf(false) }
    var selectedState by remember { mutableStateOf(types[0]) }
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
                value = selectedState,
                onValueChange = {},
                label = {
                    Text(
                        text = "Artwork Type",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.surface)
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            )
            {
                types.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectionOption,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            selectedState = selectionOption
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@Composable
fun ExhibitionItem(url: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .padding(0.dp, 4.dp)
            .fillMaxSize(),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun SearchBar() {
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
                onValueChange = { search = it },
                label = { Text("Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.baseline_search_24),
            contentDescription = "icon-search",
            modifier = Modifier
                .size(34.dp)
                .padding(5.dp)
        )
    }
}

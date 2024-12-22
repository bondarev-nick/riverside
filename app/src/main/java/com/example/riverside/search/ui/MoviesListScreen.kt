package com.example.riverside.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.riverside.repository.MovieItem
import com.example.riverside.repository.MoviesSearchResult

@Composable
fun MovieListScreen(
    movieSearchResult: MoviesSearchResult,
    hasNextP: Boolean,
    hasPrevP: Boolean,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit,
    onSearch: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchField { onSearch(it) }
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(
                    bottom = 60.dp
                )
            ) {
                items(movieSearchResult.movieItems) { movie ->
                    MovieItem(movie = movie)
                }
            }

            NumberPager(
                modifier = Modifier.align(Alignment.BottomCenter),
                currentPage = movieSearchResult.currentPage,
                hasNextPage = hasNextP,
                hasPrevPage = hasPrevP,
                onPrev = onPrevPage,
                onNext = onNextPage
            )
        }
    }
}

@Composable
fun SearchField(onSearch: (String) -> Unit) {
    var query: String by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        shape = RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable { query = "" },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                placeholder = {
                    Text(text = "Search movies...")
                },
                modifier = Modifier
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent

                ),
                singleLine = true
            )

            IconButton(
                onClick = {
                    onSearch(query)
                    keyboardController?.hide()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun NumberPager(
    modifier: Modifier,
    currentPage: Int,
    hasNextPage: Boolean,
    hasPrevPage: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {

    Card(
        modifier
            .wrapContentSize()
            .padding(bottom = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(100f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Previous button
            IconButton(
                onClick = onPrev,
                enabled = hasPrevPage
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous Page"
                )
            }

            // Current page display
            Text(
                text = currentPage.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Next button
            IconButton(
                onClick = onNext,
                enabled = hasNextPage
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next Page"
                )
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            val painter = rememberAsyncImagePainter(movie.posterUrl)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .fillMaxHeight(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Year: ${movie.year}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Type: ${movie.type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewMovieListScreen() {
    MovieListScreen(
        MoviesSearchResult(
            movieItems = listOf(
                MovieItem(
                    title = "Rus Gelin",
                    year = "2003",
                    imdbID = "tt0352791",
                    type = "movie",
                    posterUrl = "https://m.media-amazon.com/images/M/MV5BZTVkNmM0ZDUtZDQ4ZS00ZmM0LWJkODUtNzdlYWI1ZDc4NWExXkEyXkFqcGc@._V1_SX300.jpg"
                ),
                MovieItem(
                    title = "Ayastefanos'taki Rus Abidesinin Yikilisi",
                    year = "1914",
                    imdbID = "tt0289081",
                    type = "movie",
                    posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE2NGQ5MTMtY2FlOS00ZmVjLTllM2ItM2RlNDA2NzZhNTQ4XkEyXkFqcGdeQXVyMjExNjgyMTc@._V1_SX300.jpg"
                )
            ),
            totalItems = 20,
            currentPage = 15,
            error = null
        ),
        hasNextP = true,
        hasPrevP = true,
        onNextPage = {},
        onPrevPage = {},
        onSearch = {}
    )
}

@Preview
@Composable
fun PreviewMovieListScreenSinglePage() {
    MovieListScreen(
        MoviesSearchResult(
            movieItems = listOf(
                MovieItem(
                    title = "Rus Gelin",
                    year = "2003",
                    imdbID = "tt0352791",
                    type = "movie",
                    posterUrl = "https://m.media-amazon.com/images/M/MV5BZTVkNmM0ZDUtZDQ4ZS00ZmM0LWJkODUtNzdlYWI1ZDc4NWExXkEyXkFqcGc@._V1_SX300.jpg"
                ),
                MovieItem(
                    title = "Ayastefanos'taki Rus Abidesinin Yikilisi",
                    year = "1914",
                    imdbID = "tt0289081",
                    type = "movie",
                    posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE2NGQ5MTMtY2FlOS00ZmVjLTllM2ItM2RlNDA2NzZhNTQ4XkEyXkFqcGdeQXVyMjExNjgyMTc@._V1_SX300.jpg"
                )
            ),
            totalItems = 1,
            currentPage = 1,
            error = null
        ),
        hasNextP = false,
        hasPrevP = false,
        onNextPage = {},
        onPrevPage = {},
        onSearch = {}
    )
}
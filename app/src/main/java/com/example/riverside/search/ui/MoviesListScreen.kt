package com.example.riverside.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.omdbservice.model.SearchItem

@Composable
fun MovieListScreen(movies: List<SearchItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
    }
}

@Composable
fun MovieItem(movie: SearchItem) {
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
        listOf(
            SearchItem(
                title = "Rus Gelin",
                year = "2003",
                imdbID = "tt0352791",
                type = "movie",
                posterUrl = "https://m.media-amazon.com/images/M/MV5BZTVkNmM0ZDUtZDQ4ZS00ZmM0LWJkODUtNzdlYWI1ZDc4NWExXkEyXkFqcGc@._V1_SX300.jpg"
            ),
            SearchItem(
                title = "Ayastefanos'taki Rus Abidesinin Yikilisi",
                year = "1914",
                imdbID = "tt0289081",
                type = "movie",
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE2NGQ5MTMtY2FlOS00ZmVjLTllM2ItM2RlNDA2NzZhNTQ4XkEyXkFqcGdeQXVyMjExNjgyMTc@._V1_SX300.jpg"
            )
        )
    )
}
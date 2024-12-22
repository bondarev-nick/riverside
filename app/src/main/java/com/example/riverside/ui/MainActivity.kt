package com.example.riverside.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.riverside.ui.search.MovieListScreen
import com.example.riverside.ui.theme.RiversideTheme
import com.example.riverside.viewModel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val movies by viewModel.searchResult.collectAsStateWithLifecycle()
            val selectedMovie by viewModel.selectedCardIndex.collectAsStateWithLifecycle()
            RiversideTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovieListScreen(
                        modifier = Modifier.padding(innerPadding),
                        movieSearchResult = movies,
                        hasNextP = viewModel.hasNextPage(),
                        hasPrevP = viewModel.hasPrevPage(),
                        selectedItem = selectedMovie,
                        onPrevPage = { viewModel.prev() },
                        onNextPage = { viewModel.next() },
                        onSearch = { viewModel.search(it) },
                        onMovieClick = { index -> viewModel.selectCard(index) },
                        onBottomSheetDismiss = { viewModel.deselectCard() }
                    )
                }
            }
        }
    }
}
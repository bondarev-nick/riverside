package com.example.riverside

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.riverside.search.ui.MovieListScreen
import com.example.riverside.ui.theme.RiversideTheme
import com.example.riverside.viewModel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val movies by viewModel.searchResult.collectAsStateWithLifecycle()
            RiversideTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        MovieListScreen(
                            movieSearchResult = movies,
                            hasNextP = viewModel.hasNextPage(),
                            hasPrevP = viewModel.hasPrevPage(),
                            onPrevPage = { viewModel.prev() },
                            onNextPage = { viewModel.next() },
                            onSearch = { viewModel.search(it) }
                        )
                    }
                }
            }
        }
    }
}
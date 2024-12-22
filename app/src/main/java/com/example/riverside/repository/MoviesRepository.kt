package com.example.riverside.repository

import com.example.omdbservice.MovieService
import com.example.omdbservice.model.SearchItem
import com.example.omdbservice.model.SearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val movieService: MovieService,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun search(query: String, page: Int): MoviesSearchResult = withContext(dispatcher) {
        movieService.search(query, page).toMoviesSearchResult(page)
    }

    suspend fun getDetails(movieId: String) = withContext(dispatcher) {

    }
}

fun SearchResult.toMoviesSearchResult(currentPage: Int): MoviesSearchResult {
    return MoviesSearchResult(
        movieItems = searchResults.map { it.toMovieItem() },
        totalItems = totalResults.toIntOrNull() ?: 0,
        currentPage = currentPage,
        error = null
    )
}

fun SearchItem.toMovieItem(): MovieItem {
    return MovieItem(title, year, imdbID, type, posterUrl)
}
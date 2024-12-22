package com.example.riverside.repository

import com.example.omdbservice.MovieService
import com.example.omdbservice.model.DetailsResponse
import com.example.omdbservice.model.SearchItem
import com.example.omdbservice.model.SearchResult
import com.example.riverside.repository.model.MovieDetails
import com.example.riverside.repository.model.MovieItem
import com.example.riverside.repository.model.MoviesSearchResult
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

    suspend fun getDetails(movieId: String): MovieDetails = withContext(dispatcher) {
        movieService.getDetails(movieId).toMovieDetails()
    }
}

fun DetailsResponse.toMovieDetails(): MovieDetails {
    return MovieDetails(
        title = title,
        coverUrl = poster,
        year = year,
        genre = genre,
        ratings = ratings?.firstOrNull()?.value
        // etc...
    )
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
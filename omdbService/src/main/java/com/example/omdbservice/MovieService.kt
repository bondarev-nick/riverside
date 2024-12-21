package com.example.omdbservice

import com.example.omdbservice.model.SearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieService @Inject constructor(
    private val api: OmdbApi
) {
    suspend fun search(query: String, page: Int): SearchResult {
        return api.searchMovies(query, page)
    }

    suspend fun getDetails(id: String) {
        // TODO
    }
}
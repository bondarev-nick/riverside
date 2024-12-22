package com.example.omdbservice

import com.example.omdbservice.error.OmdbException
import com.example.omdbservice.error.toOmdbException
import com.example.omdbservice.model.DetailsResponse
import com.example.omdbservice.model.SearchResult
import com.example.retrofit.error.ApiException
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieService @Inject constructor(
    private val api: OmdbApi
) {
    suspend fun search(query: String, page: Int): SearchResult {
        val result = kotlin.runCatching {
            api.searchMovies(query, page)
        }.onFailure { error ->
            when (error) {
                is ApiException -> throw error.toOmdbException()
                is IOException -> throw OmdbException("Network error. Try again later")
                else -> throw OmdbException("Unknown error. Try again later")
            }
        }

        return result.getOrThrow()
    }

    suspend fun getDetails(id: String): DetailsResponse {
        val result = kotlin.runCatching {
            api.fetchDetails(id)
        }.onFailure { error ->
            when (error) {
                is ApiException -> throw error.toOmdbException()
                is IOException -> throw OmdbException("Network error. Try again later")
                else -> throw OmdbException("Unknown error. Try again later")
            }
        }

        return result.getOrThrow()
    }
}
package com.example.omdbservice

import com.example.omdbservice.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET(".")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("page") page: Int = 1
    ): SearchResult
}
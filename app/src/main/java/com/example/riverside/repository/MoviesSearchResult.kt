package com.example.riverside.repository

data class MoviesSearchResult(
    val movieItems: List<MovieItem>,
    val totalItems: Int,
    val currentPage: Int,
    val error: String?
)

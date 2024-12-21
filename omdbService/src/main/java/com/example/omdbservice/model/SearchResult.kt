package com.example.omdbservice.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("Search") val searchResults: List<SearchItem>,
    @SerialName("totalResults") val totalResults: String,
    @SerialName("Response") val response: String
)

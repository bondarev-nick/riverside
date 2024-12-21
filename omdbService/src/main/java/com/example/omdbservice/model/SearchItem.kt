package com.example.omdbservice.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchItem(
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("imdbID") val imdbID: String,
    @SerialName("Type") val type: String,
    @SerialName("Poster") val posterUrl: String
)

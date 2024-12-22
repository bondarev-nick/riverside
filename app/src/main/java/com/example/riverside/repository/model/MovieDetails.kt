package com.example.riverside.repository.model

data class MovieDetails(
    val title: String,
    val coverUrl: String,
    val year: String? = null,
    val rated: String? = null,
    val released: String? = null,
    val runtime: String? = null,
    val genre: String? = null,
    val director: String? = null,
    val writer: String? = null,
    val actors: String? = null,
    val plot: String? = null,
    val language: String? = null,
    val country: String? = null,
    val awards: String? = null,
    val ratings: String? = null,
    val metascore: String? = null,
    val imdbRating: String? = null,
    val imdbVotes: String? = null,
    val imdbID: String? = null,
    val type: String? = null,
    val dvd: String? = null,
    val boxOffice: String? = null,
    val production: String? = null,
    val website: String? = null
)

fun DetailsPreview.asFull(): MovieDetails {
    return MovieDetails(
        title = title,
        coverUrl = coverUrl
    )
}

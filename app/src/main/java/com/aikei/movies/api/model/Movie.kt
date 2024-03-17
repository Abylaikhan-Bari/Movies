package com.aikei.movies.api.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double
)
data class MovieResponse(
    val results: List<Movie>
)

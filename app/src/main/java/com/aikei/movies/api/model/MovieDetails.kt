package com.aikei.movies.api.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val genres: List<Genre>,
    val runtime: Int
)

data class Genre(
    val id: Int,
    val name: String
)

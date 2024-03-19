package com.aikei.movies.presentation.model

data class PresentationMovie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val overview: String,
    val releaseDate: String,
    val voteAverage: Double
)

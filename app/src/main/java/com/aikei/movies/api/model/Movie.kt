package com.aikei.movies.api.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") // This annotation maps the JSON field to this Kotlin property
    val posterUrl: String,
    val overview: String,
    val release_date: String,
    val vote_average: Double
)

data class MovieResponse(
    val results: List<Movie>
)

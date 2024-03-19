package com.aikei.movies.data.api.model

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") // This annotation maps the JSON field to this Kotlin property
    val posterUrl: String,
    val release_date: String,
    val vote_average: Double,
    val genres: List<Genre>,
    val runtime: Int
)

data class Genre(
    val id: Int,
    val name: String
)

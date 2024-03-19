package com.aikei.movies.data.api.model

import com.google.gson.annotations.SerializedName

// теперь должна быть модель отдельная для presentation слоя
// и мапить должны в репозитории
// посмотри в гите на прошлых уроках примеры
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

package com.aikei.movies.presentation.model

import com.aikei.movies.data.api.model.Genre

data class PresentationMovie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val overview: String,
    val releaseDate: String,
    val rating: Double,
    val genres: List<Genre>, // Assuming Genre is a defined data class
    val runtime: Int
)



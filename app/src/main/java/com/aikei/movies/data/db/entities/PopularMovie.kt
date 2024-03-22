package com.aikei.movies.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_movies")
data class PopularMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String,
    val overview: String,
    val releaseDate: String?,
    val rating: Double
)

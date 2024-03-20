package com.aikei.movies.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_movies")
data class PopularMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: Double
    // Add any other fields specific to a popular movie that might differ from a favorite movie
)

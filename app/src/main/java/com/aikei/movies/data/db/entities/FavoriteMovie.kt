package com.aikei.movies.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey val movieId: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: Double
)



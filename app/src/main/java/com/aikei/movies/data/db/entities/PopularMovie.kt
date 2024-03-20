package com.aikei.movies.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "popular_movies")
data class PopularMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: Double,
    val overview: String
   )

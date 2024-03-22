package com.aikei.movies.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aikei.movies.data.db.entities.FavoriteMovie
import com.aikei.movies.data.db.entities.PopularMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteMovie: FavoriteMovie)

    @Query("DELETE FROM favorite_movies WHERE movieId = :movieId")
    suspend fun deleteFavoriteById(movieId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE movieId = :movieId LIMIT 1)")
    suspend fun isFavorite(movieId: Int): Boolean

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>>

    // Adjusted to use PopularMovie entity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<PopularMovie>)

    // Adjusted to return LiveData<List<PopularMovie>>
    @Query("SELECT * FROM popular_movies")
    fun getAllMovies(): Flow<List<PopularMovie>>

    @Query("DELETE FROM popular_movies")
    suspend fun clearMovies()
}

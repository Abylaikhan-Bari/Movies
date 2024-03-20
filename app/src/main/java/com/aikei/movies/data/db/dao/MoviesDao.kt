package com.aikei.movies.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aikei.movies.data.db.entities.FavoriteMovie
import com.aikei.movies.data.db.entities.PopularMovie

@Dao
interface MoviesDao {
    // Favorite Movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteMovie: FavoriteMovie)

    @Query("DELETE FROM favorite_movies WHERE movieId = :movieId")
    suspend fun deleteFavoriteById(movieId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE movieId = :movieId LIMIT 1)")
    suspend fun isFavorite(movieId: Int): Boolean

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>>

    // Popular Movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPopularMovies(popularMovies: List<PopularMovie>)

    @Query("DELETE FROM popular_movies")
    suspend fun deleteAllPopularMovies()

    @Query("SELECT * FROM popular_movies")
    fun getAllPopularMovies(): LiveData<List<PopularMovie>>
}

package com.aikei.movies.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aikei.movies.data.db.entities.FavoriteMovie

@Dao
interface MoviesDao {

    // Insert a new favorite movie into the database. If the movie already exists, replace it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovie)

    // Retrieve all favorite movies from the database.
    @Query("SELECT * FROM FavoriteMovie")
    suspend fun getAllFavorites(): List<FavoriteMovie>

    // Retrieve a favorite movie by its ID.
    @Query("SELECT * FROM FavoriteMovie WHERE id = :movieId")
    suspend fun getFavoriteById(movieId: Int): FavoriteMovie?

    // Delete a favorite movie from the database by its ID.
    @Query("DELETE FROM FavoriteMovie WHERE id = :movieId")
    suspend fun deleteFavoriteById(movieId: Int)
}

package com.aikei.movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.aikei.movies.data.api.service.MoviesApiService
import com.aikei.movies.data.db.dao.MoviesDao
import com.aikei.movies.data.db.entities.FavoriteMovie
import com.aikei.movies.presentation.model.PresentationMovie
import com.aikei.movies.util.MovieMapper.mapToPresentation
import com.aikei.movies.util.MovieMapper.toPopularMovieEntity
import com.aikei.movies.util.MovieMapper.toPresentationMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val moviesApiService: MoviesApiService,
    private val moviesDao: MoviesDao
) {
    private val _moviesFlow = MutableStateFlow<List<PresentationMovie>>(emptyList())
    val moviesFlow = _moviesFlow.asStateFlow()
    companion object {
        private const val TAG = "MoviesRepository"
    }
    suspend fun refreshMovies(
        needToRefresh: Boolean,
        apiKey: String
    ): Boolean {
        val localMovies = moviesDao.getAllMovies().first()
        if (localMovies.isEmpty() || needToRefresh) {
            return try {
                val apiMovies = moviesApiService.getPopularMovies(apiKey).results
                val popularMovies = apiMovies.map { it.toPopularMovieEntity() }
                moviesDao.insertAll(popularMovies)
                _moviesFlow.emit(popularMovies.map { it.toPresentationMovie() })
                true // Indicate success
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching from API", e)
                _moviesFlow.emit(emptyList()) // Emit an empty list in case of error
                false // Indicate failure
            }
        } else {
            _moviesFlow.emit(localMovies.map { it.toPresentationMovie() })
            return true // Indicate success as the local cache is already up to date
        }
    }



    fun getPopularMovies(needToRefresh: Boolean, apiKey: String): Flow<List<PresentationMovie>> = flow {
        val localMovies = moviesDao.getAllMovies().first()
        if (localMovies.isEmpty() || needToRefresh) {
            try {
                val apiMovies = moviesApiService.getPopularMovies(apiKey).results
                val popularMovies = apiMovies.map { it.toPopularMovieEntity() }
                moviesDao.insertAll(popularMovies)
                emit(popularMovies.map { it.toPresentationMovie() })
            } catch (e: Exception) {
                // Log the error here but do not emit anything.
                // Emitting from here would violate flow exception transparency.
                Log.e(TAG, "Error fetching from API", e)
            }
        } else {
            emit(localMovies.map { it.toPresentationMovie() })
        }
    }.catch { e ->
        // Handle or log the exception here. Optionally, you can emit a fallback value.
        Log.e(TAG, "Error in flow: ", e)
        emit(emptyList())
    }




    suspend fun getMovieDetails(movieId: Int, apiKey: String): PresentationMovie? {
        return try {
            val movieDetails = moviesApiService.getMovieDetails(movieId, apiKey)
            movieDetails.mapToPresentation()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                // Handle Unauthorized error here (e.g., log error, show message to user)
                Log.e(TAG, "getMovieDetails: Unauthorized access - Check API key", e)
            } else {
                // Handle other HTTP exceptions
                Log.e(TAG, "getMovieDetails: HTTP exception", e)
            }
            null
        } catch (e: Throwable) {
            // Handle other errors
            Log.e(TAG, "getMovieDetails: Error", e)
            null
        }
    }

    suspend fun addFavorite(movieId: Int, title: String, posterPath: String, releaseDate: String, rating: Double) {
        val favoriteMovie = FavoriteMovie(
            movieId = movieId,
            title = title,
            posterUrl = posterPath,
            releaseDate = releaseDate,
            rating = rating
        )
        moviesDao.insertFavorite(favoriteMovie)
    }



    suspend fun removeFavorite(movieId: Int) {
        moviesDao.deleteFavoriteById(movieId)
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return moviesDao.isFavorite(movieId)
    }

    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return moviesDao.getFavoriteMovies()
    }



}
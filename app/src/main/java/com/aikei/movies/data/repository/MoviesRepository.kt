package com.aikei.movies.data.repository

import android.util.Log
import com.aikei.movies.data.api.model.Genre
import com.aikei.movies.data.api.model.Movie
import com.aikei.movies.data.api.model.MovieDetails
import com.aikei.movies.data.api.model.MovieResponse
import com.aikei.movies.data.api.service.MoviesApiService
import com.aikei.movies.data.db.dao.MoviesDao
import com.aikei.movies.data.db.entities.FavoriteMovie
import com.aikei.movies.presentation.model.PresentationMovie
import retrofit2.HttpException

class MoviesRepository(private val moviesApiService: MoviesApiService,
                       private val moviesDao: MoviesDao
) {

    companion object {
        private const val TAG = "MoviesRepository"
    }

    suspend fun getPopularMovies(apiKey: String): List<Movie>? {
        return try {
            val response = moviesApiService.getPopularMovies(apiKey)
            response.results
        } catch (e: HttpException) {
            Log.e(TAG, "getPopularMovies: HTTP exception", e)
            null
        } catch (e: Throwable) {
            Log.e(TAG, "getPopularMovies: Error", e)
            null
        }
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
            posterPath = posterPath,
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

    // Extension function to map MovieResponse to List<PresentationMovie>
    private fun MovieResponse?.mapToPresentation(): List<PresentationMovie>? {
        return this?.results?.map { movie ->
            PresentationMovie(
                id = movie.id,
                title = movie.title,
                posterUrl = movie.posterUrl,
                overview = movie.overview,
                releaseDate = movie.release_date,
                voteAverage = movie.vote_average,
                genres = emptyList(), // Assuming no genre information is available here
                runtime = 0 // Assuming no runtime information is available here
            )
        }
    }


    // Extension function to map MovieDetails to PresentationMovie
    private fun MovieDetails?.mapToPresentation(): PresentationMovie? {
        return this?.let { movieDetails ->
            PresentationMovie(
                id = movieDetails.id,
                title = movieDetails.title,
                posterUrl = movieDetails.posterUrl,
                overview = movieDetails.overview,
                releaseDate = movieDetails.release_date,
                voteAverage = movieDetails.vote_average,
                genres = movieDetails.genres.map { genre -> Genre(genre.id, genre.name) }, // Convert each Genre to your Presentation Genre
                runtime = movieDetails.runtime
            )
        }
    }

}

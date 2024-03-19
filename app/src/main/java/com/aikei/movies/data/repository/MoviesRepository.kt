package com.aikei.movies.data.repository

import android.util.Log
import com.aikei.movies.data.api.model.Movie
import com.aikei.movies.data.api.model.MovieDetails
import com.aikei.movies.data.api.model.MovieResponse
import com.aikei.movies.data.api.service.MoviesApiService
import com.aikei.movies.presentation.model.PresentationMovie
import retrofit2.HttpException

class MoviesRepository(private val moviesApiService: MoviesApiService) {

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
            Log.e(TAG, "getMovieDetails: HTTP exception", e)
            null
        } catch (e: Throwable) {
            Log.e(TAG, "getMovieDetails: Error", e)
            null
        }
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
                voteAverage = movie.vote_average
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
                voteAverage = movieDetails.vote_average
            )
        }
    }
}

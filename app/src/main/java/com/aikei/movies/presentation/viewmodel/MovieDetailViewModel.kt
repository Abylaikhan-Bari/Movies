package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.aikei.movies.data.api.model.MovieDetails
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.presentation.model.PresentationMovie

class MovieDetailViewModel(private val repository: MoviesRepository) : ViewModel() {

    suspend fun getMovieDetails(movieId: Int, apiKey: String): PresentationMovie? {
        val movieDetails = repository.getMovieDetails(movieId, apiKey)
        return movieDetails?.let { details ->
            PresentationMovie(
                id = details.id,
                title = details.title,
                posterUrl = details.posterUrl,
                overview = details.overview,
                releaseDate = details.releaseDate,
                voteAverage = details.voteAverage
            )
        }
    }
}

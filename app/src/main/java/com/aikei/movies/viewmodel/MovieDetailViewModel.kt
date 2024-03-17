package com.aikei.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aikei.movies.api.model.MovieDetails
import com.aikei.movies.repository.MoviesRepository

class MovieDetailViewModel(private val repository: MoviesRepository) : ViewModel() {

    fun getMovieDetails(movieId: Int, apiKey: String) = repository.getMovieDetails(movieId, apiKey)
}
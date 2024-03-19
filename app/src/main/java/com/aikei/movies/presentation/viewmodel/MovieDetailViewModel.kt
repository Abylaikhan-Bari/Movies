package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.aikei.movies.data.repository.MoviesRepository

class MovieDetailViewModel(private val repository: MoviesRepository) : ViewModel() {

    fun getMovieDetails(movieId: Int, apiKey: String) = repository.getMovieDetails(movieId, apiKey)
}
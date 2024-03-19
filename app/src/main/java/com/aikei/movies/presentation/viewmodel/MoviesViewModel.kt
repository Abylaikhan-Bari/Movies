package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.aikei.movies.data.repository.MoviesRepository

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    fun getPopularMovies(apiKey: String) = repository.getPopularMovies(apiKey)
}


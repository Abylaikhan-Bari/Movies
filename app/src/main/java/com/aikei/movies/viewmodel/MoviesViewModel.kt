package com.aikei.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aikei.movies.api.model.Movie
import com.aikei.movies.api.model.MovieDetails
import com.aikei.movies.repository.MoviesRepository

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    fun getPopularMovies(apiKey: String) = repository.getPopularMovies(apiKey)
}


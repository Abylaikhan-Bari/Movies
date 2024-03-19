package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aikei.movies.data.api.model.Movie
import com.aikei.movies.data.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    fun getPopularMovies(apiKey: String): LiveData<List<Movie>?> = liveData(Dispatchers.IO) {
        val movies = repository.getPopularMovies(apiKey)
        emit(movies)
    }
}

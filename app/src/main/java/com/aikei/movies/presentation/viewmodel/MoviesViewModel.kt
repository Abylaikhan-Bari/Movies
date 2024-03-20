package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.aikei.movies.data.api.model.Movie
import com.aikei.movies.data.db.entities.PopularMovie
import com.aikei.movies.data.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getPopularMovies(needToRefresh: Boolean, apiKey: String): LiveData<List<Movie>> = liveData(Dispatchers.IO) {
        try {
            val popularMovies = repository.getPopularMovies(needToRefresh, apiKey)
            val movies = popularMovies.map { it.toMovie() }
            emit(movies)
        } catch (e: Exception) {
            _error.value = "Error fetching popular movies: ${e.stackTraceToString()}"
        }
    }


    private fun PopularMovie.toMovie(): Movie {
        return Movie(
            id = this.id,
            title = this.title,
            posterUrl = this.posterPath,
            overview = this.overview, // Populate overview from PopularMovie
            releaseDate = this.releaseDate,
            rating = this.rating
        )
    }
}

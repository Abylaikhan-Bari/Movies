package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aikei.movies.data.api.model.Movie
import com.aikei.movies.data.db.entities.PopularMovie
import com.aikei.movies.data.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    fun getPopularMovies(needToRefresh: Boolean, apiKey: String): LiveData<List<Movie>> = liveData(Dispatchers.IO) {
        val popularMovies = repository.getPopularMovies(needToRefresh, apiKey)
        val movies = popularMovies.map { popularMovie ->
            popularMovie.toMovie() // Convert PopularMovie to Movie
        }
        emit(movies)
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

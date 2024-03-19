package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.aikei.movies.data.db.dao.MoviesDao
import com.aikei.movies.data.repository.MoviesRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: MoviesRepository) : ViewModel() {

    // Assuming you have a method in the repository to check if a movie is favorite
    fun isMovieFavorite(movieId: Int): LiveData<Boolean> = liveData {
        val isFavorite = repository.isFavorite(movieId) // Assume this calls your DAO's isFavorite method
        emit(isFavorite)
    }

    // Method to add a movie to favorites
    fun addFavorite(movieId: Int, title: String, posterPath: String, releaseDate: String, rating: Double) {
        viewModelScope.launch {
            repository.addFavorite(movieId, title, posterPath, releaseDate, rating)
        }
    }


    // Method to remove a movie from favorites
    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            repository.removeFavorite(movieId)
        }
    }
}

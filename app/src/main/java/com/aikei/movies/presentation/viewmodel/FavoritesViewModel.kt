package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.aikei.movies.data.db.entities.FavoriteMovie
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

    // Update or add this function in FavoritesViewModel
    val favoriteMovies: LiveData<List<FavoriteMovie>> = repository.getFavoriteMovies()


    // Method to remove a movie from favorites
    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            repository.removeFavorite(movieId)
        }
    }
}

class ViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

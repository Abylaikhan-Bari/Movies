package com.aikei.movies.presentation.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.presentation.model.PresentationMovie
import com.aikei.movies.util.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MoviesViewModel(private val repository: MoviesRepository, private val networkHelper: NetworkHelper) : ViewModel() {
    private val _movies = MutableLiveData<List<PresentationMovie>>()
    val movies: LiveData<List<PresentationMovie>> = _movies
    val apiKey = "16d4b76831709bc650217ad5df094731"
    // LiveData to track loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            if (networkHelper.isNetworkConnected()) {
                // Make sure to switch back to the Main dispatcher when updating LiveData
                _movies.postValue(repository.getPopularMovies(true, apiKey).first())
            } else {
                _movies.postValue(repository.getPopularMovies(false, apiKey).first())
            }
        }
    }
    fun refreshMovies(needToRefresh: Boolean) {
        _isLoading.value = true // Start loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Directly call the refresh method without passing the CoroutineScope
                repository.refreshMovies(needToRefresh, apiKey)
                // Load movies after refresh to update LiveData
                val movies = repository.getPopularMovies(needToRefresh, apiKey).first()
                _movies.postValue(movies)
            } catch (e: Exception) {
                Log.e(TAG, "Error refreshing movies: ", e)
                // Optionally handle the error, e.g., by showing a message to the user
            } finally {
                _isLoading.postValue(false) // Stop loading irrespective of success or error
            }
        }
    }




}

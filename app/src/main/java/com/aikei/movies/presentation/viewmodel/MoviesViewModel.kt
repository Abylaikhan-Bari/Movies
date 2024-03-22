package com.aikei.movies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.aikei.movies.data.api.model.Movie
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
    fun refreshMovies(needToRefresh: Boolean, apiKey: String) {
        repository.refreshMovies(needToRefresh, apiKey, viewModelScope)
    }

}

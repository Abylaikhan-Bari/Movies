package com.aikei.movies.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aikei.movies.api.model.Movie
import com.aikei.movies.api.model.MovieDetails
import com.aikei.movies.api.model.MovieResponse
import com.aikei.movies.api.service.MoviesApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(private val moviesApiService: MoviesApiService) {

    companion object {
        private const val TAG = "MoviesRepository"
    }

    fun getPopularMovies(apiKey: String): MutableLiveData<List<Movie>?> {
        val data = MutableLiveData<List<Movie>?>()

        moviesApiService.getPopularMovies(apiKey).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    // Note the change to get the list of movies from the response
                    data.postValue(response.body()?.results)
                } else {
                    Log.e(TAG, "getPopularMovies: error ${response.code()} ${response.message()}")
                    data.postValue(null)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(TAG, "getPopularMovies: onFailure", t)
                data.postValue(null)
            }
        })

        return data
    }

    fun getMovieDetails(movieId: Int, apiKey: String): MutableLiveData<MovieDetails?> {
        val data = MutableLiveData<MovieDetails?>()

        moviesApiService.getMovieDetails(movieId, apiKey).enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.isSuccessful) {
                    data.postValue(response.body())
                } else if (response.code() == 404) {
                    Log.e(TAG, "getMovieDetails: Movie not found")
                    data.postValue(null) // No movie found
                } else {
                    Log.e(TAG, "getMovieDetails: error ${response.code()} ${response.message()}")
                    data.postValue(null)
                }
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Log.e(TAG, "getMovieDetails: onFailure", t)
                data.postValue(null)
            }
        })

        return data
    }



}

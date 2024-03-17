package com.aikei.movies.api.service

import com.aikei.movies.api.model.Movie
import com.aikei.movies.api.model.MovieDetails
import com.aikei.movies.api.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {
//    @GET("movie/popular")
//    fun getPopularMovies(@Query("api_key") apiKey: String): Call<List<Movie>>
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Call<MovieResponse> // Note the change to MovieResponse

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): Call<MovieDetails>
}


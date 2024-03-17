package com.aikei.movies

import android.app.Application
import com.aikei.movies.api.service.MoviesApiService
import com.aikei.movies.repository.MoviesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp : Application() {
    lateinit var repository: MoviesRepository
        private set

    override fun onCreate() {
        super.onCreate()

        // Configure logging interceptor to log the body of the response
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Create OkHttpClient and add the logging interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Initialize Retrofit with the OkHttpClient that includes logging interceptor
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the MoviesApiService
        val moviesApiService = retrofit.create(MoviesApiService::class.java)

        // Initialize your MoviesRepository with the API service
        repository = MoviesRepository(moviesApiService)
    }
}

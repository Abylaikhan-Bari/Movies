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

        // Configure logging interceptor to log the body of the responses
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Create OkHttpClient and add the logging interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Initialize Retrofit with the OkHttpClient that includes the logging interceptor
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/") // Ensure this is the correct base URL for your API
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the MoviesApiService using Retrofit
        val moviesApiService = retrofit.create(MoviesApiService::class.java)

        // Initialize the MoviesRepository with the MoviesApiService
        repository = MoviesRepository(moviesApiService)
    }
}

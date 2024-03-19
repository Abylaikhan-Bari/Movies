package com.aikei.movies

import android.app.Application
import androidx.room.Room
import com.aikei.movies.data.api.service.MoviesApiService
import com.aikei.movies.data.db.AppDatabase
import com.aikei.movies.data.repository.MoviesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp : Application() {
    lateinit var repository: MoviesRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val moviesApiService = retrofit.create(MoviesApiService::class.java)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favorite_movies"
        ).build()
        // Initialize Room Database
        val database = AppDatabase.getDatabase(this)
        val moviesDao = database.moviesDao()

        // Now, include the MoviesDao in the MoviesRepository initialization
        repository = MoviesRepository(moviesApiService, moviesDao)
    }
}


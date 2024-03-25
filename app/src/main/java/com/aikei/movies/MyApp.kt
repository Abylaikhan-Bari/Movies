package com.aikei.movies

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.aikei.movies.data.api.service.MoviesApiService
import com.aikei.movies.data.db.AppDatabase
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.work.UpdateCacheWorker
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyApp : Application() {
    lateinit var repository: MoviesRepository
        private set

    override fun onCreate() {
        super.onCreate()
        setupPeriodicWork()

        // Setup logging interceptor for OkHttpClient
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Initialize OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of MoviesApiService
        val moviesApiService = retrofit.create(MoviesApiService::class.java)

        // Initialize Room Database using the singleton pattern
        val database = AppDatabase.getDatabase(this)
        val moviesDao = database.moviesDao()

        // Initialize MoviesRepository with the MoviesApiService and MoviesDao
        repository = MoviesRepository(moviesApiService, moviesDao)
    }
    private fun setupPeriodicWork() {
        val workRequest = PeriodicWorkRequestBuilder<UpdateCacheWorker>(2, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "updateCacheWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
        Log.d("MyApp", "Periodic Work for cache update enqueued")
    }
}

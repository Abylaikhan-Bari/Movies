package com.aikei.movies.work

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aikei.movies.R
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.receivers.CacheUpdateReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class UpdateCacheWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val moviesRepository: MoviesRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val apiKey = applicationContext.getString(R.string.api_key) // Use the resource value for the API key

        return if (moviesRepository.refreshMovies(true, apiKey)) {
            val intent = Intent(applicationContext, CacheUpdateReceiver::class.java).apply {
                action = applicationContext.getString(R.string.action_cache_updated)
            }
            applicationContext.sendBroadcast(intent)
            Result.success()
        } else {
            Result.failure()
        }
    }
}


package com.aikei.movies.work

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aikei.movies.R
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.receivers.CacheUpdateReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext

class UpdateCacheWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val moviesRepository: MoviesRepository
) : CoroutineWorker(appContext, workerParams) {

    private val TAG = UpdateCacheWorker::class.java.simpleName

    override suspend fun doWork(): Result {
        Log.i(TAG, "Starting cache refresh work")

        val apiKey = applicationContext.getString(R.string.api_key) // Use the resource value for the API key

        return try {
            // Update the cache by fetching from the repository
            if (moviesRepository.refreshMovies(true, apiKey)) {
                Log.i(TAG, "Cache successfully refreshed")

                // When done, send a broadcast to the BroadcastReceiver
                val intent = Intent(applicationContext, CacheUpdateReceiver::class.java).apply {
                    action = applicationContext.getString(R.string.action_cache_updated)
                }
                applicationContext.sendBroadcast(intent)
                Log.i(TAG, "Broadcast sent to CacheUpdateReceiver")
                Result.success()
            } else {
                Log.e(TAG, "Failed to refresh cache")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during cache refresh", e)
            Result.failure()
        }
    }
}

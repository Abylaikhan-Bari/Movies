package com.aikei.movies.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aikei.movies.R
import com.aikei.movies.util.NotificationHelper

class CacheUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check for the correct action
        if (intent.action == context.getString(R.string.action_cache_updated)) {
            NotificationHelper.showCacheUpdatedNotification(context)
        }
    }
}
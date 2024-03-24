package com.aikei.movies.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aikei.movies.R

object NotificationHelper {

    private const val CHANNEL_ID = "cache_update_channel"
    private const val NOTIFICATION_ID = 1

    fun showCacheUpdatedNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name_cache_update)
            val descriptionText = context.getString(R.string.channel_description_cache_update)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                // Customize the notification channel as needed
                lightColor = Color.BLUE
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification and issue it
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notification_title_cache_updated))
            .setContentText(context.getString(R.string.notification_content_cache_updated))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Remove the notification after it's tapped

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}

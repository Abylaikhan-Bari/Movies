package com.aikei.movies.presentation.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aikei.movies.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.moviesListFragment, R.id.favoritesFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Handle cache update button click
        val cacheUpdateButton: ImageButton = findViewById(R.id.cache_update_button)
        cacheUpdateButton.setOnClickListener {
            updateCacheAndNotify()
        }

        // Observe the NavController to update BottomNavigationView's selected item
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.menu.findItem(destination.id)?.isChecked = true
            when (destination.id) {
                R.id.movieDetailFragment -> {
                    // Set MoviesListFragment as the selected item when in MovieDetailFragment
                    bottomNavigationView.menu.findItem(R.id.moviesListFragment)?.isChecked = true
                }
            }
        }

        // Prevent reselection of the current item
        bottomNavigationView.setOnItemReselectedListener { /* No-op */ }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun updateCacheAndNotify() {
        // Implement your cache update logic here

        // For demonstration purposes, let's create a simple notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "cache_update_channel",
                "Cache Update Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification builder
        val builder = NotificationCompat.Builder(this, "cache_update_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Cache Updated")
            .setContentText("The cache has been updated.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        notificationManager.notify(1, builder.build())
    }

}

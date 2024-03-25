package com.aikei.movies.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aikei.movies.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
}

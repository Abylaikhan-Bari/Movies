package com.aikei.movies.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // Corrected import
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.aikei.movies.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Ensuring this is correctly retrieving the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment?.navController
        if (navController != null) {
            setupActionBarWithNavController(navController)
        } else {
            throw RuntimeException("NavController not found")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Ensure safe call
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        return navController?.navController?.navigateUp() ?: false || super.onSupportNavigateUp()
    }
}


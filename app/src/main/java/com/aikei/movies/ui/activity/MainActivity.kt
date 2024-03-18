package com.aikei.movies.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aikei.movies.MyApp
import com.aikei.movies.R
import com.aikei.movies.ui.fragment.MoviesListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of accessing MyApp and its repository property
        // This is just a demonstration and might not be necessary for your current implementation
        val app = application as MyApp
        val repository = app.repository

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MoviesListFragment.newInstance())
                .commit()
        }
    }
}

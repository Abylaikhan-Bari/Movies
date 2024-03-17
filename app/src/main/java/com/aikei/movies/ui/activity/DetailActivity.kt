package com.aikei.movies.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aikei.movies.R
import com.aikei.movies.ui.fragment.MovieDetailFragment

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Assuming you have a fragment to display the movie details
        // And you pass the movie ID via intent
        val movieId = intent.getIntExtra("MOVIE_ID", 0)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_container, MovieDetailFragment.newInstance(movieId))
                .commitNow()
        }
    }
}

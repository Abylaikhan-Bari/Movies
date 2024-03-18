package com.aikei.movies.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aikei.movies.databinding.ItemMovieBinding
import com.aikei.movies.api.model.Movie

class MoviesAdapter(
    private var movies: List<Movie>,
    private val onItemClick: ((Movie) -> Unit)? = null, // Click listener for movie item, optional
    private val baseImageUrl: String = "https://image.tmdb.org/t/p/w500"
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onItemClick, baseImageUrl)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        Log.d("MoviesAdapter", "Binding movie: ${movie.title}")
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onItemClick: ((Movie) -> Unit)?, // Injecting click listener
        private val baseImageUrl: String // Injecting base URL for images
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            Log.d("MoviesAdapter", "Loading URL: ${baseImageUrl + movie.posterUrl}")
            binding.movieTitleText.text = movie.title
            // Load movie poster using Coil with the full URL
            binding.moviePosterImage.load(baseImageUrl + movie.posterUrl) {
                crossfade(true)
                placeholder(android.R.drawable.stat_sys_download) // Replace with your placeholder
                error(android.R.drawable.stat_notify_error) // Replace with your error image
            }
            // Set click listener for movie item, if provided
            onItemClick?.let { click ->
                binding.root.setOnClickListener {
                    click(movie)
                }
            }
        }
    }
}

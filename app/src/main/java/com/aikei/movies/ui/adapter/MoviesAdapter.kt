package com.aikei.movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aikei.movies.R
import com.aikei.movies.api.model.Movie
import com.aikei.movies.databinding.ItemMovieBinding

class MoviesAdapter(
    private var movies: List<Movie>,
    private val onItemClick: ((Movie) -> Unit)? // Optional click listener for movie item
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    // Optional: You can make the baseImageUrl a companion object if it doesn't change
    val baseImageUrl: String = "https://image.tmdb.org/t/p/w500"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onItemClick, baseImageUrl)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onItemClick: ((Movie) -> Unit)?, // Click listener for movie item
        private val baseImageUrl: String // Base URL for images
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movieTitleText.text = movie.title
            binding.moviePosterImage.load(baseImageUrl + movie.posterUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_error)
            }


            // Invoke click listener when the item view is clicked
            itemView.setOnClickListener {
                onItemClick?.invoke(movie)
            }
        }
    }
}

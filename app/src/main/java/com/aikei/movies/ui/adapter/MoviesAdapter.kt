package com.aikei.movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aikei.movies.databinding.ItemMovieBinding
import com.aikei.movies.api.model.Movie

class MoviesAdapter(
    private val movies: List<Movie>,
    //private val onItemClick: (Movie) -> Unit // Click listener for movie item
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitleText.text = movie.title
            binding.moviePosterImage.load(movie.posterUrl) // Load movie poster using Coil
            // Set click listener for movie item
//            binding.root.setOnClickListener {
//                onItemClick.invoke(movie)
//            }
        }
    }
}

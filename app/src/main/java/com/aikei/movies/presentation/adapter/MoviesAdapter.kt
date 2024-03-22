package com.aikei.movies.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aikei.movies.R
import com.aikei.movies.presentation.model.PresentationMovie
import com.aikei.movies.databinding.ItemMovieBinding

class MoviesAdapter(
    private val onItemClick: ((PresentationMovie) -> Unit)? // Updated to use PresentationMovie
) : ListAdapter<PresentationMovie, MoviesAdapter.MovieViewHolder>(MovieDiffCallback()) { // Use PresentationMovie

    private val baseImageUrl: String = "https://image.tmdb.org/t/p/w500"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onItemClick, baseImageUrl)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onItemClick: ((PresentationMovie) -> Unit)?, // Updated to use PresentationMovie
        private val baseImageUrl: String
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: PresentationMovie) { // Binding method now expects PresentationMovie
            binding.movieTitleText.text = movie.title
            binding.moviePosterImage.load(baseImageUrl + movie.posterUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_error)
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(movie)
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<PresentationMovie>() {
        override fun areItemsTheSame(oldItem: PresentationMovie, newItem: PresentationMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PresentationMovie, newItem: PresentationMovie): Boolean {
            return oldItem == newItem
        }
    }
}

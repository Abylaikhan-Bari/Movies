package com.aikei.movies.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aikei.movies.R
import com.aikei.movies.data.db.entities.FavoriteMovie
import com.bumptech.glide.Glide

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    private var movies: List<FavoriteMovie> = emptyList()

    fun submitList(movies: List<FavoriteMovie>) {
        this.movies = movies
        notifyDataSetChanged() // For simplicity; consider using ListAdapter for diffing
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_movie_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val movieImageView: ImageView = view.findViewById(R.id.movieImageView)
        private val movieNameTextView: TextView = view.findViewById(R.id.movieNameTextView)

        fun bind(movie: FavoriteMovie) {
            Glide.with(itemView.context)
                .load(movie.posterPath) // Use posterPath for the image URL
                .into(movieImageView)
            movieNameTextView.text = movie.title // Use title for the movie's name
        }
    }

}

package com.aikei.movies.presentation.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aikei.movies.R
import com.aikei.movies.data.db.entities.FavoriteMovie
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
            val baseUrl = "https://image.tmdb.org/t/p/w500" // Example base URL
            val fullPath = baseUrl + movie.posterUrl

            Glide.with(itemView.context)
                .load(fullPath)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("GlideLoadFailed", "Error loading image", e)
                        return false // Important to return false so the error placeholder can be placed
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(movieImageView)


            movieNameTextView.text = movie.title
        }

    }

}

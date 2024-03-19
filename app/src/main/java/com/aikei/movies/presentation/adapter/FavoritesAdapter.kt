package com.aikei.movies.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aikei.movies.R
import com.aikei.movies.data.db.entities.FavoriteMovie

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    private var movies: List<FavoriteMovie> = emptyList()

    fun submitList(movies: List<FavoriteMovie>) {
        this.movies = movies
        notifyDataSetChanged() // For simplicity; consider using ListAdapter for diffing
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie = movies[position]
        // Bind movie data to the ViewHolder
    }

    override fun getItemCount(): Int = movies.size

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize views here
    }
}

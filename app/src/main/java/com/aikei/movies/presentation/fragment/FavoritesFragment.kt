package com.aikei.movies.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aikei.movies.R

class FavoritesFragment : Fragment() {

    // If you're using View Binding, declare your binding variable here

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // If using View Binding, initialize it here

        // Setup RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.favoritesRecyclerView)
        // Assuming you have a FavoritesAdapter for your RecyclerView, initialize and set it here
    }

    // If using View Binding, remember to nullify the binding variable to avoid memory leaks
}

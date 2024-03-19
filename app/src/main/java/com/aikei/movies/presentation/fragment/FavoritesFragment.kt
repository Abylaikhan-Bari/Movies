package com.aikei.movies.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.aikei.movies.MyApp
import com.aikei.movies.R
import com.aikei.movies.presentation.adapter.FavoritesAdapter
import com.aikei.movies.presentation.viewmodel.FavoritesViewModel
import com.aikei.movies.presentation.viewmodel.ViewModelFactory

class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).repository)
    }
    private lateinit var favoritesAdapter: FavoritesAdapter // Assuming this is your adapter class

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView and adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.favoritesRecyclerView)
        favoritesAdapter = FavoritesAdapter() // Initialize your adapter
        recyclerView.adapter = favoritesAdapter

        // Observe favorite movies
        viewModel.favoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
            // Ensure your adapter can handle the list of movies
            favoritesAdapter.submitList(favoriteMovies)
        }
    }
}

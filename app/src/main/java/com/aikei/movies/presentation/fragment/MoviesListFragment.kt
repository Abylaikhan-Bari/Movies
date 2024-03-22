package com.aikei.movies.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aikei.movies.MyApp
import com.aikei.movies.databinding.FragmentMoviesListBinding
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.presentation.adapter.MoviesAdapter
import com.aikei.movies.presentation.viewmodel.MoviesViewModel
import com.aikei.movies.util.NetworkHelper

class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoviesViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).repository, NetworkHelper(requireContext()))
    }

    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        // Initializing the adapter with an empty list and set up click listener to navigate
        moviesAdapter = MoviesAdapter { movie ->
            val action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(movie.id)
            findNavController().navigate(action)
        }

        setupRecyclerView()

        val apiKey =  "16d4b76831709bc650217ad5df094731"
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshMovies(true, apiKey) // Trigger data refresh
        }

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            moviesAdapter.submitList(movies) // Update adapter's dataset
            binding.swipeRefreshLayout.isRefreshing = false // Stop the refreshing animation
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.moviesRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.moviesRecyclerView.adapter = moviesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ViewModelFactory(private val repository: MoviesRepository, private val networkHelper: NetworkHelper) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MoviesViewModel(repository, networkHelper) as T // Pass networkHelper here
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

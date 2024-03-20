package com.aikei.movies.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aikei.movies.MyApp
import com.aikei.movies.data.api.model.Movie
import com.aikei.movies.databinding.FragmentMoviesListBinding
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.presentation.adapter.MoviesAdapter
import com.aikei.movies.presentation.viewmodel.MoviesViewModel

class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and adapter
        moviesAdapter = MoviesAdapter { movie ->
            val action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(movie.id)
            findNavController().navigate(action)
        }
        setupRecyclerView()

        val repository = (requireActivity().application as MyApp).repository
        viewModel = ViewModelProvider(this, MoviesListViewModelFactory(repository)).get(MoviesViewModel::class.java)

        // Observe popular movies
        viewModel.getPopularMovies(needToRefresh = false, apiKey = "16d4b76831709bc650217ad5df094731").observe(viewLifecycleOwner) { movies ->
            movies?.let {
                if (it.isNotEmpty()) {
                    showMovies(it)
                } else {
                    showEmptyState()
                }
            } ?: run {
                showEmptyState()
            }
        }

        // Set up SwipeRefreshLayout
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getPopularMovies(needToRefresh = true, apiKey = "16d4b76831709bc650217ad5df094731")
                .observe(viewLifecycleOwner) { movies ->
                    movies?.let {
                        if (it.isNotEmpty()) {
                            showMovies(it)
                        } else {
                            showEmptyState()
                        }
                    } ?: run {
                        showEmptyState()
                    }
                    swipeRefreshLayout.isRefreshing = false // Stop the refreshing animation
                }
        }
    }

    private fun setupRecyclerView() {
        binding.moviesRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.moviesRecyclerView.adapter = moviesAdapter
    }

    private fun showMovies(movies: List<Movie>) {
        if (movies.isNotEmpty()) {
            binding.moviesRecyclerView.visibility = View.VISIBLE
            binding.emptyStateTextView.visibility = View.GONE
            moviesAdapter.submitList(movies)
            Log.d("MoviesListFragment", "Number of movies: ${movies.size}")
        } else {
            showEmptyState()
        }
    }

    private fun showEmptyState() {
        binding.moviesRecyclerView.visibility = View.GONE
        binding.emptyStateTextView.visibility = View.VISIBLE
        Log.d("MoviesListFragment", "Movie list is empty")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class MoviesListViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MoviesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

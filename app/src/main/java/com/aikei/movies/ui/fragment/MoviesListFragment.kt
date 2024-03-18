package com.aikei.movies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aikei.movies.MyApp
import com.aikei.movies.databinding.FragmentMoviesListBinding
import com.aikei.movies.repository.MoviesRepository
import com.aikei.movies.ui.adapter.MoviesAdapter
import com.aikei.movies.viewmodel.MoviesViewModel

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

        // Initialize the adapter with an empty list and set up click listener to navigate
        moviesAdapter = MoviesAdapter(emptyList()) { movie ->
            val action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(movie.id)
            findNavController().navigate(action)


        }
        setupRecyclerView()

        val factory = ViewModelFactory((activity?.application as MyApp).repository)
        viewModel = ViewModelProvider(this, factory)[MoviesViewModel::class.java]

        viewModel.getPopularMovies("16d4b76831709bc650217ad5df094731").observe(viewLifecycleOwner) { movies ->
            movies?.let {
                moviesAdapter.updateMovies(it) // Update adapter's dataset
            }
        }
    }

    private fun setupRecyclerView() {
        binding.moviesRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.moviesRecyclerView.adapter = moviesAdapter
    }

    companion object {
        fun newInstance(): MoviesListFragment = MoviesListFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MoviesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

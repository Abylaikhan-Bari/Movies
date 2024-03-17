package com.aikei.movies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        setupRecyclerView()

        val factory = ViewModelFactory((activity?.application as MyApp).repository)
        viewModel = ViewModelProvider(this, factory).get(MoviesViewModel::class.java)

        viewModel.getPopularMovies("16d4b76831709bc650217ad5df094731").observe(viewLifecycleOwner) { movies ->
            movies?.let {
                moviesAdapter = MoviesAdapter(it)
                binding.moviesRecyclerView.adapter = moviesAdapter
            }
        }
    }

    private fun setupRecyclerView() {
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(context)
        // Initial empty adapter; data will be set upon receiving from ViewModel
        moviesAdapter = MoviesAdapter(emptyList())
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

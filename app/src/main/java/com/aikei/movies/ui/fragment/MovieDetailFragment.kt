package com.aikei.movies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aikei.movies.MyApp
import com.aikei.movies.databinding.FragmentMovieDetailBinding
import com.aikei.movies.viewmodel.MovieDetailViewModel
import com.aikei.movies.viewmodel.MoviesViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.aikei.movies.repository.MoviesRepository

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels {
        ViewModelFactory((activity?.application as MyApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = requireArguments().getInt(ARG_MOVIE_ID)

        viewModel.getMovieDetails(movieId, "16d4b76831709bc650217ad5df094731").observe(viewLifecycleOwner) { movieDetails ->
            // Assuming 'movieDetails' is not null and has 'title' and 'overview' properties
            binding.movieTitleText.text = movieDetails?.title
            binding.movieOverviewText.text = movieDetails?.overview
            // If you have an ImageView for poster, you can load the image using a library like Coil
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_MOVIE_ID = "movie_id"

        fun newInstance(movieId: Int): MovieDetailFragment = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MOVIE_ID, movieId)
            }
        }
    }

    class ViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

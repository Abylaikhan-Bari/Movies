package com.aikei.movies.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.aikei.movies.MyApp
import com.aikei.movies.R
import com.aikei.movies.data.api.model.MovieDetails
import com.aikei.movies.databinding.FragmentMovieDetailBinding
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.presentation.viewmodel.MovieDetailViewModel

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).repository)
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
        val movieId = requireArguments().getInt("movieId")

        if (movieId <= 0) {
            Log.e("MovieDetailFragment", "Invalid movie ID")
            binding.movieTitleText.text = getString(R.string.error_invalid_movie_id)
            // Update the UI to indicate the error or hide the detail view
            return
        }
        viewModel.getMovieDetails(movieId, "16d4b76831709bc650217ad5df094731").observe(viewLifecycleOwner) { movieDetails ->
            if (movieDetails != null) {
                binding.movieTitleText.text = movieDetails.title
                binding.movieOverviewText.text = movieDetails.overview
                // Load poster image, etc.
                val baseImageUrl: String = "https://image.tmdb.org/t/p/w500"
                binding.moviePosterImage.load(baseImageUrl + movieDetails.posterUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_error)
                }
            } else {
                binding.movieTitleText.text = getString(R.string.error_loading_movie_details)
                binding.movieOverviewText.text = getString(R.string.try_again_later)
            }
        }
    }


    private fun displayMovieDetails(movieDetails: MovieDetails) {
        // Display movie details when available
        binding.movieTitleText.text = movieDetails.title
        binding.movieOverviewText.text = movieDetails.overview
        // You can set other views with relevant data here
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

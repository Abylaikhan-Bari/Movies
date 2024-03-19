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
import com.aikei.movies.presentation.model.PresentationMovie
import com.aikei.movies.presentation.viewmodel.MovieDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        GlobalScope.launch(Dispatchers.Main) {
            val presentationMovie = viewModel.getMovieDetails(movieId, "16d4b76831709bc650217ad5df094731")
            if (presentationMovie != null) {
                val movieDetails = convertToMovieDetails(presentationMovie)
                displayMovieDetails(movieDetails)
            } else {
                binding.movieTitleText.text = getString(R.string.error_loading_movie_details)
            }
        }

    }
    fun convertToMovieDetails(presentationMovie: PresentationMovie): MovieDetails {
        return MovieDetails(
            id = presentationMovie.id,
            title = presentationMovie.title,
            overview = presentationMovie.overview,
            posterUrl = presentationMovie.posterUrl,
            release_date = presentationMovie.releaseDate,
            vote_average = presentationMovie.voteAverage,
            genres = presentationMovie.genres,
            runtime = presentationMovie.runtime
        )
    }

    private fun displayMovieDetails(movieDetails: MovieDetails) {
        binding.movieTitleText.text = movieDetails.title
        binding.movieOverviewText.text = movieDetails.overview
        val genresText = movieDetails.genres.joinToString(separator = ", ") { it.name }
        binding.movieGenresText.text = genresText
        binding.movieRuntimeText.text = getString(R.string.runtime_format, movieDetails.runtime)
        val baseImageUrl: String = "https://image.tmdb.org/t/p/w500"
        binding.moviePosterImage.load(baseImageUrl + movieDetails.posterUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_error)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(movieId: Int): MovieDetailFragment = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("movieId", movieId)
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

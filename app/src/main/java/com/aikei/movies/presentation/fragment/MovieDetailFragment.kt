package com.aikei.movies.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.aikei.movies.BuildConfig
import com.aikei.movies.MyApp
import com.aikei.movies.R
import com.aikei.movies.data.api.model.MovieDetails
import com.aikei.movies.databinding.FragmentMovieDetailBinding
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.presentation.model.PresentationMovie
import com.aikei.movies.presentation.viewmodel.FavoritesViewModel
import com.aikei.movies.presentation.viewmodel.MovieDetailViewModel
import com.aikei.movies.util.MovieMapper.mapToPresentation
import kotlinx.coroutines.launch

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private var movieId: Int = 0
    private var movie: PresentationMovie? = null // Make it nullable to handle uninitialized state safely

    private val viewModel: MovieDetailViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).repository)
    }

    private val favoritesViewModel: FavoritesViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).repository)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieId = requireArguments().getInt("movieId")
        setHasOptionsMenu(true)
        fetchMovieDetails(movieId)
    }

    private fun fetchMovieDetails(movieId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                viewModel.getMovieDetails(movieId, BuildConfig.API_KEY).let { presentationMovie ->
                    movie = presentationMovie // Now correctly handles null
                    presentationMovie?.let {
                        displayMovieDetails(convertToMovieDetails(it))
                    } ?: run {
                        binding.movieTitleText.text = getString(R.string.error_loading_movie_details)
                    }
                }
            } catch (e: Exception) {
                Log.e("MovieDetailFragment", "Error fetching movie details", e)
                binding.movieTitleText.text = getString(R.string.error_loading_movie_details)
            }
        }
    }

    // Assumes that convertToMovieDetails correctly handles a non-nullable PresentationMovie
    private fun convertToMovieDetails(presentationMovie: PresentationMovie): MovieDetails = with(presentationMovie) {
        MovieDetails(
            id = id,
            title = title,
            overview = overview,
            posterUrl = posterUrl,
            releaseDate = this.releaseDate ?: "Not Available",
            rating = rating,
            genres = genres,
            runtime = runtime
        )
    }

    private fun displayMovieDetails(movieDetails: MovieDetails) {
        with(binding) {
            movieTitleText.text = movieDetails.title
            movieOverviewText.text = movieDetails.overview
            val genresText = movieDetails.genres.joinToString(separator = ", ") { it.name }
            movieGenresText.text = genresText
            movieRuntimeText.text = getString(R.string.runtime_format, movieDetails.runtime)
            val baseImageUrl = "https://image.tmdb.org/t/p/w500"
            moviePosterImage.load(baseImageUrl + movieDetails.posterUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_error)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_movie_detail, menu)
        updateFavoriteStatus(menu)
    }

    private fun updateFavoriteStatus(menu: Menu) {
        movieId.let { id ->
            favoritesViewModel.isMovieFavorite(id).observe(viewLifecycleOwner) { isFavorite ->
                val favoriteIcon = if (isFavorite) {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_filled)
                } else {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_border)
                }
                menu.findItem(R.id.action_favorite).icon = favoriteIcon
            }
        }
    }


    private fun handleFavoriteClicked() {
        movie?.let { currentMovie ->
            // Observe the LiveData within the ViewModel to check if the movie is a favorite
            favoritesViewModel.isMovieFavorite(currentMovie.id).observe(viewLifecycleOwner) { isFavorite ->
                if (!isFavorite) {
                    currentMovie.releaseDate?.let {
                        favoritesViewModel.addFavorite(currentMovie.id, currentMovie.title, currentMovie.posterUrl,
                            it, currentMovie.rating)
                    }
                } else {
                    favoritesViewModel.removeFavorite(currentMovie.id)
                }
                // You may need to remove this observer to prevent multiple triggers
                // Or, alternatively, handle this logic differently to avoid directly observing here
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_favorite -> {
            handleFavoriteClicked()
            true
        }
        else -> super.onOptionsItemSelected(item)
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
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> {
                    MovieDetailViewModel(repository) as T
                }
                modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                    FavoritesViewModel(repository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}
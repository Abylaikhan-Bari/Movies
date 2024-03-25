package com.aikei.movies.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aikei.movies.MyApp
import com.aikei.movies.data.repository.MoviesRepository
import com.aikei.movies.presentation.model.PresentationMovie
import com.aikei.movies.presentation.viewmodel.MoviesViewModel
import com.aikei.movies.util.NetworkHelper
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.aikei.movies.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class MoviesListFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).repository, NetworkHelper(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MoviesListScreen(viewModel = viewModel) { movieId ->
                    val action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(movieId)
                    findNavController().navigate(action)
                }
            }
        }
    }


    class ViewModelFactory(private val repository: MoviesRepository, private val networkHelper: NetworkHelper) : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MoviesViewModel(repository, networkHelper) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

@Composable
fun MoviesListScreen(viewModel: MoviesViewModel, navigateToMovieDetail: (Int) -> Unit) {
    val movies by viewModel.movies.observeAsState(initial = emptyList())
    val isRefreshing by viewModel.isLoading.observeAsState(initial = false)

    Scaffold(

    ) { padding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.refreshMovies(true) }
        ) {
            // Using LazyVerticalGrid for a grid layout
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Fixed count of 3 columns
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(movies) { movie ->
                    MovieItem(movie, navigateToMovieDetail)
                }
            }
        }
    }
}


@Composable
fun MovieItem(movie: PresentationMovie, navigateToMovieDetail: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .size(120.dp, 200.dp), // Fixed size for the Card
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .clickable { navigateToMovieDetail(movie.id) }
                .padding(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
                    contentDescription = stringResource(R.string.movie_poster_cd, movie.title),
                    modifier = Modifier
                        .fillMaxSize() // Fill the available space
                        .aspectRatio(0.75f),
                    // Providing a placeholder for the loading state
                    placeholder = painterResource(R.drawable.ic_placeholder),
                    // Providing an image for the error state
                    error = painterResource(R.drawable.ic_error)
                )
            }
            Text(
                text = movie.title,
                style = MaterialTheme.typography.caption,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
                    .wrapContentWidth() // Ensure the title doesn't exceed the card width
            )
        }
    }
}

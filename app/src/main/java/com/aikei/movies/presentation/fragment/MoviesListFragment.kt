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
import androidx.compose.runtime.getValue
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.aikei.movies.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.graphics.Color

class MoviesListFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).repository, NetworkHelper(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyAppTheme {
                    MoviesListScreen(viewModel = viewModel) { movieId ->
                        val action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(movieId)
                        findNavController().navigate(action)
                    }
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
fun MyAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        darkColors(
            primary = Color(0xFFBB86FC),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5),
            background = Color(0xFF121212),
            surface = Color(0xFF121212),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White,
        )
    } else {
        lightColors(
            primary = Color(0xFF6200EE),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5),
            background = Color.White,
            surface = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black,
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}

@Composable
fun MoviesListScreen(viewModel: MoviesViewModel, navigateToMovieDetail: (Int) -> Unit) {
    val movies by viewModel.movies.observeAsState(initial = emptyList())
    val isRefreshing by viewModel.isLoading.observeAsState(initial = false)

    Scaffold {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.refreshMovies(true) }
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.padding(it)
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
            .size(120.dp, 200.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
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
                        .fillMaxSize()
                        .aspectRatio(0.75f),
                    placeholder = painterResource(R.drawable.ic_placeholder),
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
                    .wrapContentWidth()
            )
        }
    }
}


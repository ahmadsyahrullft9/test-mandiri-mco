package test.mandiri.moviedb.presentation.screen.movie_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.koinViewModel
import test.mandiri.moviedb.core.theme.MovieDbTheme
import test.mandiri.moviedb.domain.model.Movie
import test.mandiri.moviedb.presentation.component.ErrorView
import test.mandiri.moviedb.presentation.component.LoadingView
import test.mandiri.moviedb.presentation.component.MovieItemView
import test.mandiri.moviedb.presentation.mockMovieList

@Composable
fun MovieListScreen(
    genreID: Int,
    onMovieClick: (Int, String) -> Unit
) {
    val movieListViewModel = koinViewModel<MovieListViewModel>()
    movieListViewModel.changeGenreID(genreID)

    val movieList = movieListViewModel.movieList.collectAsLazyPagingItems()
    if (movieList.loadState.refresh is LoadState.Loading) {
        LoadingView()
    } else if (movieList.loadState.refresh is LoadState.Error) {
        val error = movieList.loadState.refresh as LoadState.Error
        ErrorView(errorMessage = error.error.message ?: "data tidak dapat dimuat") {
            movieListViewModel.refresh()
        }
    } else if (movieList.loadState.refresh is LoadState.NotLoading && movieList.itemCount == 0) {
        ErrorView(errorMessage = "data tidak ditemukan") {
            movieListViewModel.refresh()
        }
    } else {
        MovieListScreenView(movieList) { id, title ->
            onMovieClick(id, title)
        }
    }
}

@Composable
fun MovieListScreenView(movieList: LazyPagingItems<Movie>, onMovieClick: (Int, String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movieList.itemCount) { i ->
            movieList[i]?.let {
                MovieItemView(it, Modifier.clickable(true, onClick = {
                    onMovieClick(
                        it.id ?: 0,
                        it.title ?: "-"
                    )
                }))
            }
        }
    }
}

@Composable
@PreviewLightDark
fun MovieListScreenPreview() {
    val mockData = mockMovieList
    val flow = MutableStateFlow(PagingData.from(mockData))
    val movieList = flow.collectAsLazyPagingItems()

    MovieDbTheme {
        Scaffold { _ ->
            MovieListScreenView(movieList) { id, title ->

            }
        }
    }
}
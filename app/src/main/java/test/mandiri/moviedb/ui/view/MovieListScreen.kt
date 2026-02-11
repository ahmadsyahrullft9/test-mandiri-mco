package test.mandiri.moviedb.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel
import test.mandiri.moviedb.ui.component.ErrorView
import test.mandiri.moviedb.ui.component.LoadingView
import test.mandiri.moviedb.ui.component.MovieItem
import test.mandiri.moviedb.ui.viewmodel.MovieListViewModel

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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 156.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movieList.itemCount) { i ->
                movieList[i]?.let {
                    MovieItem(it, Modifier.clickable(true, onClick = {
                        onMovieClick(
                            it.id ?: 0,
                            it.title ?: "-"
                        )
                    }))
                }
            }
        }
    }
}
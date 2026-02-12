package test.mandiri.moviedb.presentation.screen.movie_trailer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.koinViewModel
import test.mandiri.moviedb.core.base.UIState
import test.mandiri.moviedb.core.theme.MovieDbTheme
import test.mandiri.moviedb.domain.model.Trailer
import test.mandiri.moviedb.presentation.component.ErrorView
import test.mandiri.moviedb.presentation.component.LoadingView
import test.mandiri.moviedb.presentation.component.YouTubeThumbnailView
import test.mandiri.moviedb.presentation.mockMovieList
import test.mandiri.moviedb.presentation.mockTrailerList
import test.mandiri.moviedb.presentation.screen.movie_list.MovieListScreenView

@Composable
fun MovieTrailerScreen(
    movieID: Int,
    onTrailerClick: (String) -> Unit,
) {
    val movieTrailerViewModel = koinViewModel<MovieTrailerViewModel>()
    movieTrailerViewModel.changeMovieID(movieID)

    when (val trailers = movieTrailerViewModel.trailerList.collectAsState().value) {
        is UIState.Success -> {
            val trailerList = trailers.result ?: emptyList()
            if (trailerList.isNotEmpty()) {
                MovieTrailerScreenView(trailerList) { videoID ->
                    onTrailerClick(videoID)
                }
            } else {
                ErrorView(errorMessage = "data tidak ditemukan") {
                    movieTrailerViewModel.refresh()
                }
            }
        }

        is UIState.Error -> ErrorView(errorMessage = trailers.message ?: "data tidak ditemukan") {
            movieTrailerViewModel.refresh()
        }

        is UIState.Loading -> LoadingView()
    }
}

@Composable
fun MovieTrailerScreenView(
    trailerList: List<Trailer>,
    onTrailerClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(trailerList.size) { index ->
            YouTubeThumbnailView(
                videoId = trailerList[index].key ?: "",
                videoName = trailerList[index].name ?: "",
            ) {
                trailerList[index].key?.let { videoID ->
                    onTrailerClick(videoID)
                }
            }
        }
    }
}


@Composable
@PreviewLightDark
fun MovieTrailerScreenPreview() {
    val trailerList = mockTrailerList.toList()
    MovieDbTheme {
        Scaffold { _ ->
            MovieTrailerScreenView(trailerList) { videoID ->

            }
        }
    }
}

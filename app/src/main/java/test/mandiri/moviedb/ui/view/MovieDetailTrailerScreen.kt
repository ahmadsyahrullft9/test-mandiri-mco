package test.mandiri.moviedb.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import test.mandiri.moviedb.data.model.TrailerResponse
import test.mandiri.moviedb.ui.component.ErrorView
import test.mandiri.moviedb.ui.component.LoadingView
import test.mandiri.moviedb.ui.component.YouTubeThumbnail
import test.mandiri.moviedb.ui.state.UIState
import kotlin.collections.isNotEmpty


@Composable
fun MovieDetailTrailerScreen(
    uiState: UIState<TrailerResponse>,
    onRefresh: () -> Unit,
    onTrailerClick: (String) -> Unit,
) {
    when (uiState) {
        is UIState.Success -> {
            val trailers = uiState.result?.results
            if (trailers != null && trailers.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 156.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(trailers.size) { index ->
                        YouTubeThumbnail(
                            videoId = trailers[index].key ?: "",
                            videoName = trailers[index].name ?: "",
                        ) {
                            trailers[index].key?.let { videoID ->
                                onTrailerClick(videoID)
                            }
                        }
                    }
                }
            } else {
                ErrorView(errorMessage = "data tidak ditemukan") {
                    onRefresh()
                }
            }
        }

        is UIState.Error -> ErrorView(errorMessage = uiState.message ?: "data tidak ditemukan") {
            onRefresh()
        }

        is UIState.Loading -> LoadingView()
    }
}


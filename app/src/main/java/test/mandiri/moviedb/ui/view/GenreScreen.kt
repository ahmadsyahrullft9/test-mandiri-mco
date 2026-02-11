package test.mandiri.moviedb.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import test.mandiri.moviedb.ui.component.ErrorView
import test.mandiri.moviedb.ui.component.GenreItem
import test.mandiri.moviedb.ui.component.LoadingView
import test.mandiri.moviedb.ui.state.UIState
import test.mandiri.moviedb.ui.viewmodel.GenreViewModel

@Composable
fun GenreScreen(
    onGenreClick: (Int, String) -> Unit
) {
    val genreViewModel = koinViewModel<GenreViewModel>()
    when (val uiState = genreViewModel.genre.collectAsState().value) {
        is UIState.Success -> {
            val result = uiState.result ?: emptyList()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 156.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(result.size) { i ->
                    GenreItem(
                        result[i],
                        Modifier.clickable(true, onClick = {
                            onGenreClick(
                                result[i].id ?: 0,
                                result[i].name ?: "-"
                            )
                        })
                    )
                }
            }
        }

        is UIState.Error -> ErrorView(uiState.message ?: "data genre tidak ditemukan") {
            genreViewModel.refresh()
        }

        is UIState.Loading -> LoadingView()
    }
}
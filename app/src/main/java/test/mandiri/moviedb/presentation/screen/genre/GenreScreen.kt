package test.mandiri.moviedb.presentation.screen.genre

import androidx.compose.foundation.clickable
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
import org.koin.androidx.compose.koinViewModel
import test.mandiri.moviedb.core.base.UIState
import test.mandiri.moviedb.core.theme.MovieDbTheme
import test.mandiri.moviedb.domain.model.Genre
import test.mandiri.moviedb.presentation.component.ErrorView
import test.mandiri.moviedb.presentation.component.GenreItemView
import test.mandiri.moviedb.presentation.component.LoadingView
import test.mandiri.moviedb.presentation.mockGenreList

@Composable
fun GenreScreen(onGenreClick: (Int, String) -> Unit) {
    val genreViewModel = koinViewModel<GenreViewModel>()
    when (val uiState = genreViewModel.genre.collectAsState().value) {
        is UIState.Success -> {
            val genreList = uiState.result ?: emptyList()
            if (genreList.isNotEmpty())
                GenreScreenView(genreList = genreList) { id, name ->
                    onGenreClick(id, name)
                }
            else ErrorView("data genre tidak ditemukan") {
                genreViewModel.refresh()
            }
        }

        is UIState.Error -> ErrorView(uiState.message ?: "data genre tidak ditemukan") {
            genreViewModel.refresh()
        }

        is UIState.Loading -> LoadingView()
    }
}

@Composable
fun GenreScreenView(genreList: List<Genre>, onGenreClick: (Int, String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genreList.size) { i ->
            GenreItemView(
                genreList[i],
                Modifier.clickable(true, onClick = {
                    onGenreClick(
                        genreList[i].id ?: 0,
                        genreList[i].name ?: "-"
                    )
                })
            )
        }
    }
}

@Composable
@PreviewLightDark
fun GenreScreenPreview() {
    val genreList = mockGenreList.toList()
    MovieDbTheme {
        Scaffold { _ ->
            GenreScreenView(genreList) { genreID, genreName ->

            }
        }
    }
}
package test.mandiri.moviedb.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import test.mandiri.moviedb.data.model.Review
import test.mandiri.moviedb.ui.component.ErrorView
import test.mandiri.moviedb.ui.component.LoadingView
import test.mandiri.moviedb.ui.component.ReviewItem


@Composable
fun MovieDetailReviewScreen(
    uiState: LazyPagingItems<Review>,
    onRefresh: () -> Unit,
) {
    if (uiState.loadState.refresh is LoadState.Loading) {
        LoadingView()
    } else if (uiState.loadState.refresh is LoadState.Error) {
        val error = uiState.loadState.refresh as LoadState.Error
        ErrorView(errorMessage = error.error.message ?: "data tidak dapat dimuat") {
            onRefresh()
        }
    } else if (uiState.loadState.refresh is LoadState.NotLoading && uiState.itemCount == 0) {
        ErrorView(errorMessage = "data tidak ditemukan") {
            onRefresh()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(uiState.itemCount) { i ->
                uiState[i]?.let {
                    ReviewItem(it)
                }
            }
        }
    }
}
package test.mandiri.moviedb.presentation.screen.movie_review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import org.koin.compose.viewmodel.koinActivityViewModel
import test.mandiri.moviedb.core.theme.MovieDbTheme
import test.mandiri.moviedb.domain.model.Review
import test.mandiri.moviedb.presentation.component.ErrorView
import test.mandiri.moviedb.presentation.component.LoadingView
import test.mandiri.moviedb.presentation.component.ReviewItemView
import test.mandiri.moviedb.presentation.mockReviewList

@Composable
fun MovieReviewScreen(movieID: Int) {
    val movieReviewViewModel = koinActivityViewModel<MovieReviewViewModel>()
    movieReviewViewModel.changeMovieID(movieID)

    val reviewList = movieReviewViewModel.reviewList.collectAsLazyPagingItems()

    if (reviewList.loadState.refresh is LoadState.Loading) {
        LoadingView()
    } else if (reviewList.loadState.refresh is LoadState.Error) {
        val error = reviewList.loadState.refresh as LoadState.Error
        ErrorView(errorMessage = error.error.message ?: "data tidak dapat dimuat") {
            movieReviewViewModel.refresh()
        }
    } else if (reviewList.loadState.refresh is LoadState.NotLoading && reviewList.itemCount == 0) {
        ErrorView(errorMessage = "data tidak ditemukan") {
            movieReviewViewModel.refresh()
        }
    } else {
        MovieReviewScreenView(reviewList = reviewList)
    }
}

@Composable
fun MovieReviewScreenView(reviewList: LazyPagingItems<Review>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(reviewList.itemCount) { i ->
            reviewList[i]?.let {
                ReviewItemView(it)
            }
        }
    }
}

@Composable
@PreviewLightDark
fun MovieReviewScreenPreview() {
    val mockData = mockReviewList
    val flow = MutableStateFlow(PagingData.from(mockData))
    val reviewList = flow.collectAsLazyPagingItems()

    MovieDbTheme {
        Scaffold { _ ->
            MovieReviewScreenView(reviewList)
        }
    }
}
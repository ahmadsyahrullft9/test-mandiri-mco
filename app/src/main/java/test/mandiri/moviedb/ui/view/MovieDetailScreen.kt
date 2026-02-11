package test.mandiri.moviedb.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel
import test.mandiri.moviedb.ui.viewmodel.MovieDetailViewModel

sealed class MovieDetailTab(val title: String) {
    object Overview : MovieDetailTab("Overview")
    object Trailer : MovieDetailTab("Trailer")
    object Review : MovieDetailTab("Review")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieID: Int,
    onTrailerClick: (String) -> Unit
) {
    val movieDetailViewModel = koinViewModel<MovieDetailViewModel>()
    movieDetailViewModel.changeMovieID(movieID)

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val allTabs = listOf(MovieDetailTab.Overview, MovieDetailTab.Trailer, MovieDetailTab.Review)
    Column {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            divider = {},
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    Modifier
                        .tabIndicatorOffset(selectedTabIndex)
                        .height(2.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                )
            }) {
            allTabs.mapIndexed { index, tab ->
                Tab(
                    text = { Text(tab.title) },
                    selected = selectedTabIndex == index,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {
                        selectedTabIndex = index
                    }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> MovieDetailInfoScreen(
                uiState = movieDetailViewModel.movieDetail.collectAsState().value,
                onRefresh = { movieDetailViewModel.refreshMovieDetail() }
            )

            1 -> MovieDetailTrailerScreen(
                uiState = movieDetailViewModel.trailerList.collectAsState().value,
                onTrailerClick = { id -> onTrailerClick(id) },
                onRefresh = { movieDetailViewModel.refreshMovieTrailer() }
            )

            2 -> MovieDetailReviewScreen(
                uiState = movieDetailViewModel.reviewList.collectAsLazyPagingItems(),
                onRefresh = { movieDetailViewModel.refreshMovieReview() }
            )
        }
    }
}

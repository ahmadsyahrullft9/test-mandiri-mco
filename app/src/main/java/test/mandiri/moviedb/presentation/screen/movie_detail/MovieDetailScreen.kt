package test.mandiri.moviedb.presentation.screen.movie_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import test.mandiri.moviedb.presentation.screen.movie_overview.MovieOverviewScreen
import test.mandiri.moviedb.presentation.screen.movie_review.MovieReviewScreen
import test.mandiri.moviedb.presentation.screen.movie_trailer.MovieTrailerScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieID: Int,
    onTrailerClick: (String) -> Unit
) {
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
            0 -> MovieOverviewScreen(movieID)

            1 -> MovieTrailerScreen(movieID) { onTrailerClick(it) }

            2 -> MovieReviewScreen(movieID)
        }
    }
}

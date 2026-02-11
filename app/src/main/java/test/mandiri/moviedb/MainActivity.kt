package test.mandiri.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import test.mandiri.moviedb.ui.theme.MovieDbTheme
import test.mandiri.moviedb.ui.view.GenreScreen
import test.mandiri.moviedb.ui.view.MovieDetailScreen
import test.mandiri.moviedb.ui.view.MovieListScreen
import test.mandiri.moviedb.ui.view.YouTubePlayerScreen

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = retain { mutableStateListOf<AppRoute>(AppRoute.GenreRoute) }
            MovieDbTheme {
                Scaffold(
                    topBar = {
                        if (backStack.lastOrNull() !is AppRoute.YoutubePlayerRoute) {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = backStack.lastOrNull()?.title ?: "The Movie DB",
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                                ),
                                navigationIcon = {
                                    AnimatedVisibility(
                                        visible = backStack.size > 1,
                                    ) {
                                        IconButton(onClick = {
                                            backStack.removeLastOrNull()
                                        }) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                                contentDescription = "back button"
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    NavDisplay(
                        modifier = Modifier.padding(
                            paddingValues
                        ),
                        backStack = backStack,
                        onBack = {
                            backStack.removeLastOrNull()
                        },
                        entryProvider = { key ->
                            when (key) {
                                is AppRoute.GenreRoute -> NavEntry(key) {
                                    GenreScreen { genreID, genreName ->
                                        backStack.add(
                                            AppRoute.MovieListRoute(
                                                genreID,
                                                genreName
                                            )
                                        )
                                    }
                                }

                                is AppRoute.MovieListRoute -> NavEntry(key) {
                                    MovieListScreen(key.genreID) { movieID, movieTitle ->
                                        backStack.add(
                                            AppRoute.MovieDetailRoute(
                                                movieID,
                                                movieTitle
                                            )
                                        )
                                    }
                                }

                                is AppRoute.MovieDetailRoute -> NavEntry(key) {
                                    MovieDetailScreen(key.movieID) { videoID ->
                                        backStack.add(AppRoute.YoutubePlayerRoute(videoID))
                                    }
                                }

                                is AppRoute.YoutubePlayerRoute -> NavEntry(key) {
                                    YouTubePlayerScreen(key.videoID) {
                                        backStack.removeLastOrNull()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

sealed class AppRoute(val title: String) {
    object GenreRoute : AppRoute(title = "List Of Movie Genre")

    data class MovieListRoute(val genreID: Int, val genreName: String) :
        AppRoute(title = "${genreName.replaceFirstChar { it.uppercase() }} Movie List")

    data class MovieDetailRoute(val movieID: Int, val movieTitle: String) :
        AppRoute(title = movieTitle)

    data class YoutubePlayerRoute(val videoID: String) : AppRoute(title = videoID)
}
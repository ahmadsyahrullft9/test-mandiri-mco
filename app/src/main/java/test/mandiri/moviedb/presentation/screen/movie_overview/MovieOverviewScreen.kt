package test.mandiri.moviedb.presentation.screen.movie_overview

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.koin.androidx.compose.koinViewModel
import test.mandiri.moviedb.core.base.UIState
import test.mandiri.moviedb.core.theme.MovieDbTheme
import test.mandiri.moviedb.core.util.tryParseAndFormatDate
import test.mandiri.moviedb.domain.model.MovieDetail
import test.mandiri.moviedb.presentation.component.ErrorView
import test.mandiri.moviedb.presentation.component.LoadingView
import test.mandiri.moviedb.presentation.mockMovieDetail
import java.util.Locale

@Composable
fun MovieOverviewScreen(movieID: Int) {
    val movieOverviewViewModel = koinViewModel<MovieOverviewViewModel>()
    movieOverviewViewModel.changeMovieID(movieID)

    when (val movieDetail = movieOverviewViewModel.movieDetail.collectAsState().value) {
        is UIState.Success -> {
            movieDetail.result?.let { MovieOverviewScreenView(it) }
                ?: ErrorView("data tidak ditemukan") {
                    movieOverviewViewModel.refresh()
                }
        }

        is UIState.Error -> ErrorView(movieDetail.message ?: "data tidak ditemukan") {
            movieOverviewViewModel.refresh()
        }

        is UIState.Loading -> LoadingView()
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun MovieOverviewScreenView(movieDetail: MovieDetail) {
    val context = LocalContext.current
    val movieTitle = if (movieDetail.original_title == movieDetail.title) {
        movieDetail.title ?: movieDetail.original_title ?: ""
    } else {
        "${movieDetail.title} - ${movieDetail.original_title}"
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(corner = CornerSize(8.dp))),
            model = "https://image.tmdb.org/t/p/w500/${movieDetail.poster_path}",
            contentDescription = "movie detail poster ${movieDetail.id}",
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            movieTitle,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            movieDetail.genres.joinToString(", ") { (_, name) -> name ?: "" },
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
        )
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Language",
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    movieDetail.spoken_languages.firstOrNull()?.name ?: "-",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Rating",
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${
                        String.format(
                            Locale.getDefault(),
                            "%.1f",
                            movieDetail.vote_average
                        )
                    } / 10",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Status",
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    movieDetail.status ?: "-",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Synopsis",
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            movieDetail.overview ?: "-",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Release Date",
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            movieDetail.release_date.tryParseAndFormatDate(),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(
                corner = CornerSize(8.dp)
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(),
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, movieDetail.homepage?.toUri())
                context.startActivity(intent)
            },
        ) {
            Text("Visit Official Page")
        }
    }
}

@Composable
@PreviewLightDark
fun MovieOverviewScreenPreview(){
    MovieDbTheme {
        Scaffold { _ ->
            MovieOverviewScreenView(mockMovieDetail)
        }
    }
}
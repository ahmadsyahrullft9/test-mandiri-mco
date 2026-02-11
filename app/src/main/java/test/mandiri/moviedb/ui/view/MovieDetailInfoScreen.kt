package test.mandiri.moviedb.ui.view

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import test.mandiri.moviedb.data.model.MovieDetail
import test.mandiri.moviedb.ui.component.ErrorView
import test.mandiri.moviedb.ui.component.LoadingView
import test.mandiri.moviedb.ui.state.UIState
import test.mandiri.moviedb.utils.tryParseAndFormatDate
import java.util.Locale


@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun MovieDetailInfoScreen(
    uiState: UIState<MovieDetail>,
    onRefresh: () -> Unit,
) {
    val context = LocalContext.current
    when (uiState) {
        is UIState.Success -> {
            val result = uiState.result
            result?.let {
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
                        model = "https://image.tmdb.org/t/p/w500/${it.poster_path}",
                        contentDescription = "movie detail poster ${it.id}",
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(Modifier.height(24.dp))
                    if (it.original_title == it.title) {
                        it.title
                    } else {
                        "${it.title} - ${it.original_title}"
                    }?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        it.genres.joinToString(", ") { (_, name) -> name ?: "" },
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
                                it.spoken_languages.firstOrNull()?.name ?: "-",
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
                                        it.vote_average
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
                                it.status ?: "-",
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
                        it.overview ?: "-",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "Release Date",
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        it.release_date.tryParseAndFormatDate(),
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
                            val intent = Intent(Intent.ACTION_VIEW, it.homepage?.toUri())
                            context.startActivity(intent)
                        },
                    ) {
                        Text("Visit Official Page")
                    }
                }
            } ?: ErrorView("data tidak ditemukan") {
                onRefresh()
            }
        }

        is UIState.Error -> ErrorView(uiState.message ?: "data tidak ditemukan") {
            onRefresh()
        }

        is UIState.Loading -> LoadingView()
    }
}
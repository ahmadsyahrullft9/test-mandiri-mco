package test.mandiri.moviedb.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import test.mandiri.moviedb.R
import test.mandiri.moviedb.domain.model.Movie
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItemView(movie: Movie, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(236.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(
                    corner = CornerSize(8.dp),
                ),
            ),
    ) {
        Column {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(5F)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                        )
                    ),
                model = "https://image.tmdb.org/t/p/w500/${movie.poster_path}",
                contentDescription = "movie poster ${movie.id}",
                contentScale = ContentScale.Crop,
                loading = placeholder(
                    resourceId = R.drawable.ic_launcher_background
                ),
                failure = placeholder(
                    resourceId = R.drawable.ic_launcher_background
                )
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(6F)
                    .padding(8.dp)
            ) {
                Text(
                    movie.title ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    movie.overview ?: "-",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${
                        String.format(
                            Locale.getDefault(),
                            "%.1f",
                            movie.vote_average
                        )
                    } / 10",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
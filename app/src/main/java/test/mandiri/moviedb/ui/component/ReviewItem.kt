package test.mandiri.moviedb.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import test.mandiri.moviedb.data.model.Review
import test.mandiri.moviedb.ui.theme.MovieDbTheme

@Composable
fun ReviewItem(review: Review) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(corner = CornerSize(8.dp))
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    review.author ?: "-",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Icon(
                        Icons.Default.Star, contentDescription = "rating",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "${review.author_details?.rating ?: "-"} / 10",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                review.content ?: "-",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
@PreviewLightDark
fun previewReviewItem() {
    val json = """
        {
            "author": "MovieGuys",
            "author_details": {
                "name": "",
                "username": "MovieGuys",
                "avatar_path": null,
                "rating": 7.0
            },
            "content": "\"The Tank\" starts out well but leads to a rather uninspiring outcome. Viewed as an anti war film, with realistic scenes of combat and war crimes, this film is compellingly, horribly, honest. The spirtual component, that the script try's to inject towards the films end, however, falls flat. Its not especially well done, lacking a narrational consistency, that would tie it back, convincingly, into the majority of the story. \r\n\r\nIn summary, excellent acting, realistic scenes of combat and the mistreatment of civilian populations, that sit's somewhat ackwardly, alongside a tale of death and damnation. A reasonable, if not exceptional watch.",
            "created_at": "2026-01-04T00:40:39.236Z",
            "id": "6959b707bbf78189c8cbf053",
            "updated_at": "2026-01-04T00:42:37.731Z",
            "url": "https://www.themoviedb.org/review/6959b707bbf78189c8cbf053"
        }
    """.trimIndent()
    val gson = Gson()
    val review = gson.fromJson(json, Review::class.java)
    MovieDbTheme {
        Scaffold { _ ->
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 411.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(10) {
                    ReviewItem(review)
                }
            }
        }
    }
}
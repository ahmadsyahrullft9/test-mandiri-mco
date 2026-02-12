package test.mandiri.moviedb.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import test.mandiri.moviedb.R


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun YouTubeThumbnailView(
    videoId: String,
    videoName: String,
    onVideoClick: (String) -> Unit
) {
    val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

    Box(
        modifier = Modifier
            .width(200.dp)
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .clickable { onVideoClick(videoId) }
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(corner = CornerSize(8.dp))),
            model = thumbnailUrl,
            contentDescription = "thumnail movie of $videoId",
            contentScale = ContentScale.Crop,
            loading = placeholder(
                painter = painterResource(R.drawable.ic_launcher_background)
            ),
            failure = placeholder(
                painter = painterResource(R.drawable.ic_launcher_background)
            )
        )

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play $videoName",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                .padding(8.dp)
        )
    }
}
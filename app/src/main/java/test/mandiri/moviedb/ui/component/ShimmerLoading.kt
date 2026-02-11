package test.mandiri.moviedb.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

//https://medium.com/@hzolfagharipour/shimmer-animation-in-jetpack-compose-without-recomposition-04d1317634a7
@Composable
fun Modifier.shimmerLoading(
    durationMillis: Int = 1000,
): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer_loading")

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "translate_animation",
    )

    val mainColor = MaterialTheme.colorScheme.surfaceContainerHigh

    return drawBehind {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    mainColor.copy(alpha = 0.7f),
                    mainColor.copy(alpha = 1.0f),
                    mainColor.copy(alpha = 0.7f),
                ),
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + 180f, y = translateAnimation + 180f),
            )
        )
    }
}
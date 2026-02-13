package test.mandiri.moviedb.presentation.screen.movie_review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

    val seeMoreOnReview = rememberSaveable {
        mutableStateOf<Review?>(null)
    }

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
        MovieReviewScreenView(reviewList = reviewList) {
            seeMoreOnReview.value = it
        }
        if (seeMoreOnReview.value != null) {
            Dialog(
                onDismissRequest = {
                    seeMoreOnReview.value = null
                },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnClickOutside = true,
                )
            ) {
                Box(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth()
                                .padding(top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                seeMoreOnReview.value!!.author ?: "-",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.headlineMedium,
                                maxLines = 2,
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
                                    "${seeMoreOnReview.value!!.author_details?.rating ?: "-"} / 10",
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Box(
                            Modifier
                                .weight(1f)
                        ) {
                            Text(
                                seeMoreOnReview.value!!.content ?: "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(state = rememberScrollState()),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(
                                corner = CornerSize(8.dp)
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(),
                            onClick = {
                                seeMoreOnReview.value = null
                            },
                        ) {
                            Text(
                                "Tutup",
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun MovieReviewScreenView(reviewList: LazyPagingItems<Review>, onSeeMore: (Review) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(reviewList.itemCount) { i ->
            reviewList[i]?.let {
                ReviewItemView(it) { review ->
                    onSeeMore(review)
                }
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
            MovieReviewScreenView(reviewList) {

            }
        }
    }
}
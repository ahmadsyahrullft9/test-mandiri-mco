package test.mandiri.moviedb.presentation.screen.movie_detail

sealed class MovieDetailTab(val title: String) {
    object Overview : MovieDetailTab("Overview")
    object Trailer : MovieDetailTab("Trailer")
    object Review : MovieDetailTab("Review")
}
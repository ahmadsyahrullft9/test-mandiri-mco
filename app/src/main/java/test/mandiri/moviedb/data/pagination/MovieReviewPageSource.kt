package test.mandiri.moviedb.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import test.mandiri.moviedb.domain.model.Review
import test.mandiri.moviedb.domain.model.ReviewRequest
import test.mandiri.moviedb.domain.repository.MovieRepository
import test.mandiri.moviedb.domain.usecase.FetchMovieReviewsUsecase

class MovieReviewPageSource(
    private val movieRepository: MovieRepository,
    private val movieID: Int,
) : PagingSource<Int, Review>() {

    private val fetchMovieReviewsUsecase by lazy {
        FetchMovieReviewsUsecase(movieRepository)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return try {
            val nextPage = params.key ?: 1
            val input = ReviewRequest(
                movie_id = movieID,
                page = nextPage
            )
            val result = fetchMovieReviewsUsecase.call(input)
            LoadResult.Page(
                data = result,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (result.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Review>): Int {
        return state.anchorPosition ?: 1
    }
}
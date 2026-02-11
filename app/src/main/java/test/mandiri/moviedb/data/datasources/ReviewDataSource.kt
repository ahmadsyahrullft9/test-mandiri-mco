package test.mandiri.moviedb.data.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import test.mandiri.moviedb.data.model.Review
import test.mandiri.moviedb.data.repository.MovieRepository

class ReviewDataSource(
    private val movieRepository: MovieRepository,
    private val movieID: Int,
) : PagingSource<Int, Review>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return try {
            val nextPage = params.key ?: 1
            val result = movieRepository.fetchMovieReview(
                movieID = movieID,
                page = nextPage,
            )
            LoadResult.Page(
                data = result.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (result.results.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Review>): Int {
        return state.anchorPosition ?: 1
    }
}
package test.mandiri.moviedb.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import test.mandiri.moviedb.domain.model.Movie
import test.mandiri.moviedb.domain.model.MovieRequest
import test.mandiri.moviedb.domain.repository.MovieRepository
import test.mandiri.moviedb.domain.usecase.FetchMoviesByGenreUsecase

class MoviePageSource(
    private val movieRepository: MovieRepository,
    private val genreID: Int,
) : PagingSource<Int, Movie>() {

    private val fetchMoviesByGenreUsecase by lazy {
        FetchMoviesByGenreUsecase(movieRepository)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val input = MovieRequest(
                genre_id = genreID,
                page = nextPage
            )
            val result = fetchMoviesByGenreUsecase.call(input)
            LoadResult.Page(
                data = result,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (result.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int {
        return state.anchorPosition ?: 1
    }
}
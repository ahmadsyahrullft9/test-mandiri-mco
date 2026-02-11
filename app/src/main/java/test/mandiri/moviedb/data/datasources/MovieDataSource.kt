package test.mandiri.moviedb.data.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import test.mandiri.moviedb.data.model.Movie
import test.mandiri.moviedb.data.repository.MovieRepository

class MovieDataSource(
    private val movieRepository: MovieRepository,
    private val genreID: Int,
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val result = movieRepository.fetchMovieByGenre(
                genreIDs = listOf(genreID),
                page = nextPage,
            )
            result.results?.let {
                LoadResult.Page(
                    data = it,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (it.isEmpty()) null else nextPage + 1
                )
            } ?: throw Exception("data tidak ditemukan")
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int {
        return state.anchorPosition ?: 1
    }
}
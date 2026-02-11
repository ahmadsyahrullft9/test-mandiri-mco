package test.mandiri.moviedb.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import test.mandiri.moviedb.data.datasources.MovieDataSource
import test.mandiri.moviedb.data.repository.MovieRepository
import java.util.Date

/**
 * ViewModel responsible for managing and providing a paginated list of movies.
 *
 * This ViewModel handles business logic for filtering movies by genre and triggering
 * manual data refreshes. It utilizes the Paging 3 library to provide a reactive [kotlinx.coroutines.flow.Flow]
 * of movie data that is cached within the [viewModelScope].
 *
 * @property movieRepository The repository instance used to fetch movie data from the data source.
 */
class MovieListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _genreID = MutableStateFlow(0)
    private val _refreshUnit = MutableSharedFlow<Date>(extraBufferCapacity = 1)

    /**
     * A reactive stream of paginated movie data, filtered by the selected genre ID.
     *
     * This flow automatically triggers a refresh whenever [changeGenreID] or [refresh] is called.
     * It uses the Paging 3 library to provide a [androidx.paging.PagingData] stream and is
     * cached within the [viewModelScope] to maintain the state across configuration changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val movieList = combine(
        _genreID,
        _refreshUnit.onStart { emit(Date()) },
    ) { id, refreshDate ->
        Log.d("MovieListViewModel", "movieList : $refreshDate")
        id
    }.flatMapLatest { id ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                MovieDataSource(movieRepository, id)
            },
        ).flow
    }.cachedIn(viewModelScope)

    fun changeGenreID(genreID: Int) {
        _genreID.value = genreID
    }

    fun refresh() {
        _refreshUnit.tryEmit(Date())
    }
}
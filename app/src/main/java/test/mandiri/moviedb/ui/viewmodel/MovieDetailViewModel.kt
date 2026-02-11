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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import test.mandiri.moviedb.data.datasources.ReviewDataSource
import test.mandiri.moviedb.data.repository.MovieRepository
import test.mandiri.moviedb.ui.state.UIState
import java.util.Date

/**
 * ViewModel responsible for managing and providing data for the Movie Detail screen.
 *
 * This ViewModel handles the retrieval of movie details, trailer information, and
 * paginated user reviews based on a provided movie ID. It utilizes [MovieRepository]
 * to fetch data and exposes it via [kotlinx.coroutines.flow.StateFlow] and
 * [androidx.paging.PagingData] to the UI.
 *
 * Key functionalities include:
 * - Loading and refreshing movie metadata.
 * - Loading and refreshing movie trailers/videos.
 * - Providing a paginated stream of movie reviews using [ReviewDataSource].
 * - Reacting to movie ID changes to update all associated data.
 *
 * @property movieRepository The repository used to interact with movie data sources.
 */
class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movieID = MutableStateFlow(0)

    private val _refreshMovieDetail = MutableSharedFlow<Date>(extraBufferCapacity = 1)
    private val _refreshMovieTrailer = MutableSharedFlow<Date>(extraBufferCapacity = 1)
    private val _refreshMovieReview = MutableSharedFlow<Date>(extraBufferCapacity = 1)

    /**
     * A [kotlinx.coroutines.flow.StateFlow] that provides the current state of the movie details.
     *
     * This flow combines the selected movie ID and refresh triggers to fetch detailed information
     * from the repository. It emits [UIState.Loading] during data retrieval, [UIState.Success]
     * when data is successfully fetched, and [UIState.Error] if the operation fails.
     *
     * The flow is lazily started and remains active for 5 seconds after the last subscriber disconnects.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val movieDetail = combine(
        _movieID,
        _refreshMovieDetail.onStart { //onStart agar data dimuat otomatis diawal
            emit(Date())
        },
    ) { id, refreshDate ->
        Log.d("MovieDetailViewModel", "movieDetail : $refreshDate")
        id
    }.flatMapLatest { id ->
        flow {
            if (id == 0) return@flow
            emit(UIState.Loading)
            try {
                val data = movieRepository.fetchMovieDetail(id)
                emit(UIState.Success(data))
            } catch (e: Exception) {
                emit(UIState.Error(e.message ?: "Unknown Error"))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UIState.Loading
    )

    /**
     * A [kotlinx.coroutines.flow.StateFlow] that provides the [UIState] of the movie's trailer list.
     *
     * This flow combines the current movie ID and refresh triggers to fetch trailer data
     * from the repository. It automatically handles loading states and error capturing,
     * maintaining the latest state for the UI to observe.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val trailerList = combine(
        _movieID,
        _refreshMovieTrailer.onStart { emit(Date()) },
    ) { id, refreshDate ->
        Log.d("MovieDetailViewModel", "trailerList : $refreshDate")
        id
    }.flatMapLatest { id ->
        flow {
            if (id == 0) return@flow
            emit(UIState.Loading)
            try {
                val data = movieRepository.fetchMovieTrailer(movieID = id)
                emit(UIState.Success(data))
            } catch (e: Exception) {
                emit(UIState.Error(e.message ?: "Unknown Error"))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UIState.Loading
    )

    /**
     * A [kotlinx.coroutines.flow.Flow] of paginated movie reviews.
     *
     * This property combines the current [movieID] and a refresh trigger to fetch
     * reviews using the [ReviewDataSource]. The results are wrapped in a [androidx.paging.PagingData]
     * and cached within the [viewModelScope] to maintain the pagination state during
     * configuration changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val reviewList = combine(
        _movieID,
        _refreshMovieReview.onStart { emit(Date()) },
    ) { id, refreshDate ->
        Log.d("MovieDetailViewModel", "reviewList : $refreshDate")
        id
    }.flatMapLatest { id ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ReviewDataSource(movieRepository, id)
            },
        ).flow
    }.cachedIn(viewModelScope)

    fun changeMovieID(movieID: Int) {
        _movieID.value = movieID
    }

    fun refreshMovieDetail() {
        _refreshMovieDetail.tryEmit(Date())
    }

    fun refreshMovieTrailer() {
        _refreshMovieTrailer.tryEmit(Date())
    }

    fun refreshMovieReview() {
        _refreshMovieReview.tryEmit(Date())
    }
}
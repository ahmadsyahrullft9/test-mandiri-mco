package test.mandiri.moviedb.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import test.mandiri.moviedb.data.model.Genre
import test.mandiri.moviedb.data.repository.MovieRepository
import test.mandiri.moviedb.ui.state.UIState
import java.util.Date

/**
 * ViewModel responsible for managing and providing the list of movie genres.
 *
 * This class interacts with the [MovieRepository] to fetch genre data and exposes it
 * as a [StateFlow] of [UIState]. It includes a refresh mechanism to reload data
 * via the [refresh] function.
 *
 * @property movieRepository The repository used to fetch movie genre data.
 */
class GenreViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _refreshUnit = MutableSharedFlow<Date>(extraBufferCapacity = 1)

    /**
     * A [StateFlow] providing the [UIState] of movie genres.
     *
     * This property automatically fetches the genres upon initialization and
     * re-fetches them whenever a refresh is triggered. It emits:
     * - [UIState.Loading] when the data is being fetched.
     * - [UIState.Success] containing the list of [Genre] objects upon a successful fetch.
     * - [UIState.Error] containing an error message if the fetch fails.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val genre = _refreshUnit.onStart { emit(Date()) }.flatMapLatest { refreshDate ->
        Log.d("GenreViewModel", "genre : $refreshDate")
        flow {
            emit(UIState.Loading)
            try {
                val data = movieRepository.fetchGenre()
                emit(UIState.Success(data.genres))
            } catch (e: Exception) {
                emit(UIState.Error(e.message ?: "Unknown Error"))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UIState.Loading
    )

    fun refresh() {
        _refreshUnit.tryEmit(Date())
    }
}
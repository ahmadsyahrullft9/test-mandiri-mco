package test.mandiri.moviedb.presentation.screen.movie_trailer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import test.mandiri.moviedb.core.base.UIState
import test.mandiri.moviedb.domain.repository.MovieRepository
import test.mandiri.moviedb.domain.usecase.FetchMovieTrailersUsecase
import java.util.Date

class MovieTrailerViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val movieID = MutableStateFlow(0)
    private val refreshUnit = MutableSharedFlow<Date>()

    private val fetchMovieTrailersUsecase by lazy {
        FetchMovieTrailersUsecase(movieRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val trailerList = combine(
        movieID,
        refreshUnit.onStart { emit(Date()) },
    ) { id, refreshDate ->
        Log.d("MovieTrailerViewModel", "trailerList : $refreshDate")
        id
    }.flatMapLatest { id ->
        flow {
            if (id == 0) return@flow
            emit(UIState.Loading)
            try {
                val result = fetchMovieTrailersUsecase.call(id)
                emit(UIState.Success(result))
            } catch (e: Exception) {
                emit(UIState.Error(e.message ?: "Unknown Error"))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = UIState.Loading
    )

    fun changeMovieID(newMovieID: Int){
        movieID.value = newMovieID
    }

    fun refresh() {
        refreshUnit.tryEmit(Date())
    }
}
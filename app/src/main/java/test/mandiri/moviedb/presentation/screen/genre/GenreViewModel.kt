package test.mandiri.moviedb.presentation.screen.genre

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import test.mandiri.moviedb.core.base.UIState
import test.mandiri.moviedb.domain.repository.GenreRepository
import test.mandiri.moviedb.domain.usecase.FetchGenresUsecase
import java.util.Date

class GenreViewModel(private val genreRepository: GenreRepository) : ViewModel() {

    private val fetchGenresUsecase by lazy { FetchGenresUsecase(genreRepository) }

    private val refreshUnit = MutableSharedFlow<Date>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val genre = refreshUnit.onStart { emit(Date()) }
        .flatMapLatest { refreshDate ->
            Log.d("GenreViewModel", "genre : $refreshDate")
            flow {
                emit(UIState.Loading)
                try {
                    val result = fetchGenresUsecase.call()
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


    fun refresh() {
        refreshUnit.tryEmit(Date())
    }
}
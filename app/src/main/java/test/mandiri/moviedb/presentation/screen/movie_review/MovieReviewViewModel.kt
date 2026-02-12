package test.mandiri.moviedb.presentation.screen.movie_review

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
import test.mandiri.moviedb.data.pagination.MovieReviewPageSource
import test.mandiri.moviedb.domain.repository.MovieRepository
import java.util.Date

class MovieReviewViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val movieID = MutableStateFlow(0)
    private val refreshUnit = MutableSharedFlow<Date>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val reviewList = combine(
        movieID,
        refreshUnit.onStart { emit(Date()) },
    ) { id, refreshDate ->
        Log.d("MovieReviewViewModel", "reviewList : $refreshDate")
        id
    }.flatMapLatest { id ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                MovieReviewPageSource(
                    movieRepository = movieRepository,
                    movieID = id
                )
            },
        ).flow
    }.cachedIn(viewModelScope)


    fun changeMovieID(newMovieID: Int){
        movieID.value = newMovieID
    }

    fun refresh() {
        refreshUnit.tryEmit(Date())
    }
}
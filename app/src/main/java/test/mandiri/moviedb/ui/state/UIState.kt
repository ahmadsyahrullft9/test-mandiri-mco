package test.mandiri.moviedb.ui.state

sealed class UIState<out T>(
    val result: T? = null,
    val message: String? = null
) {
    object Loading : UIState<Nothing>()
    data class Success<T>(private val _result: T) : UIState<T>(result = _result)
    data class Error(private val _msg: String) : UIState<Nothing>(message = _msg)
}
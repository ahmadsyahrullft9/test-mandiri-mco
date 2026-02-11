package test.mandiri.moviedb.data.repository

import org.json.JSONObject
import retrofit2.Retrofit
import test.mandiri.moviedb.BuildConfig
import test.mandiri.moviedb.data.api.MovieApi
import test.mandiri.moviedb.data.model.GenreResponse
import test.mandiri.moviedb.data.model.MovieDetail
import test.mandiri.moviedb.data.model.MovieResponse
import test.mandiri.moviedb.data.model.ReviewResponse
import test.mandiri.moviedb.data.model.TrailerResponse

class MovieRepository(
    private val apiClient: Retrofit
) {

    private val movieService by lazy {
        apiClient.create(MovieApi::class.java)
    }

    suspend fun fetchGenre(): GenreResponse {
        var errorMessage = "data genre tidak ditemukan"
        val result = movieService.genreList()
        if (result.isSuccessful) {
            val body = result.body()
            return body ?: throw Exception(errorMessage)
        }
        result.errorBody()?.let {
            val errorObj = JSONObject(it.string())
            if (errorObj.has("status_message")) {
                if (BuildConfig.DEBUG) {
                    errorMessage = errorObj.getString("status_message")
                }
            }
        }
        throw Exception(errorMessage)
    }

    suspend fun fetchMovieByGenre(
        genreIDs: List<Int>,
        page: Int
    ): MovieResponse {
        var errorMessage = "data movie tidak ditemukan"
        val result = movieService.movieListByGenre(
            withGenres = genreIDs.joinToString(separator = ","),
            page = page,
        )
        if (result.isSuccessful) {
            val body = result.body()
            return body ?: throw Exception(errorMessage)
        }
        result.errorBody()?.let {
            val errorObj = JSONObject(it.string())
            if (errorObj.has("status_message")) {
                if (BuildConfig.DEBUG) {
                    errorMessage = errorObj.getString("status_message")
                }
            }
        }
        throw Exception(errorMessage)
    }

    suspend fun fetchMovieDetail(movieID: Int): MovieDetail {
        var errorMessage = "data movie tidak ditemukan"
        val result = movieService.movieDetail(movieID = movieID)
        if (result.isSuccessful) {
            val body = result.body()
            return body ?: throw Exception(errorMessage)
        }
        result.errorBody()?.let {
            val errorObj = JSONObject(it.string())
            if (errorObj.has("status_message")) {
                if (BuildConfig.DEBUG) {
                    errorMessage = errorObj.getString("status_message")
                }
            }
        }
        throw Exception(errorMessage)
    }

    suspend fun fetchMovieReview(movieID: Int, page: Int): ReviewResponse {
        var errorMessage = "data review tidak ditemukan"
        val result = movieService.reviews(movieID = movieID, page = page)
        if (result.isSuccessful) {
            val body = result.body()
            return body ?: throw Exception(errorMessage)
        }
        result.errorBody()?.let {
            val errorObj = JSONObject(it.string())
            if (errorObj.has("status_message")) {
                if (BuildConfig.DEBUG) {
                    errorMessage = errorObj.getString("status_message")
                }
            }
        }
        throw Exception(errorMessage)
    }

    suspend fun fetchMovieTrailer(movieID: Int): TrailerResponse{
        var errorMessage = "data trailer tidak ditemukan"
        val result = movieService.trailers(movieID = movieID)
        if (result.isSuccessful) {
            val body = result.body()
            return body ?: throw Exception(errorMessage)
        }
        result.errorBody()?.let {
            val errorObj = JSONObject(it.string())
            if (errorObj.has("status_message")) {
                if (BuildConfig.DEBUG) {
                    errorMessage = errorObj.getString("status_message")
                }
            }
        }
        throw Exception(errorMessage)
    }
}
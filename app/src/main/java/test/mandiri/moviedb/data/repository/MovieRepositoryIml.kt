package test.mandiri.moviedb.data.repository

import org.json.JSONObject
import test.mandiri.moviedb.BuildConfig
import test.mandiri.moviedb.data.endpoint.MovieEndpoint
import test.mandiri.moviedb.domain.model.MovieDetail
import test.mandiri.moviedb.domain.model.MovieResponse
import test.mandiri.moviedb.domain.model.ReviewResponse
import test.mandiri.moviedb.domain.model.TrailerResponse
import test.mandiri.moviedb.domain.repository.MovieRepository

class MovieRepositoryIml(
    private val apiService: MovieEndpoint
) : MovieRepository {

    override suspend fun fetchMoviesByGenre(
        genreID: Int,
        page: Int
    ): MovieResponse {
        val genreIDs = listOf(genreID)
        var errorMessage = "movie not found"
        val result = apiService.fetchMoviesByGenre(
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

    override suspend fun getMovieDetail(movieID: Int): MovieDetail {
        var errorMessage = "movie detail not found"
        val result = apiService.getMovieDetail(movieID)
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

    override suspend fun fetchMovieReviews(
        movieID: Int,
        page: Int
    ): ReviewResponse {
        var errorMessage = "movie review not found"
        val result = apiService.fetchMovieReviews(movieID = movieID, page = page)
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

    override suspend fun fetchMovieTrailers(movieID: Int): TrailerResponse {
        var errorMessage = "movie trailer not found"
        val result = apiService.fetchMovieTrailers(movieID = movieID)
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
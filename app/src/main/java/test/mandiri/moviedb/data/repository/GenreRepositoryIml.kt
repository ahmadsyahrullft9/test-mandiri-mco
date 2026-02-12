package test.mandiri.moviedb.data.repository

import org.json.JSONObject
import retrofit2.Retrofit
import test.mandiri.moviedb.BuildConfig
import test.mandiri.moviedb.data.endpoint.GenreEndpoint
import test.mandiri.moviedb.domain.model.GenreResponse
import test.mandiri.moviedb.domain.repository.GenreRepository

class GenreRepositoryIml(
    private val apiService:GenreEndpoint
) : GenreRepository {

    override suspend fun fetchGenres(): GenreResponse {
        var errorMessage = "movie genre not found"
        val result = apiService.fetchGenres()
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
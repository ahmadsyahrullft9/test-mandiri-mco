package test.mandiri.moviedb.data.endpoint

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import test.mandiri.moviedb.domain.model.GenreResponse


interface GenreEndpoint {
    @GET("genre/movie/list")
    suspend fun fetchGenres(
        @Query("language") language: String = "en"
    ): Response<GenreResponse>
}
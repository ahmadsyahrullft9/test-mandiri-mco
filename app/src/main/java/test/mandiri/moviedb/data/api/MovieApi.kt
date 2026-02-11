package test.mandiri.moviedb.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import test.mandiri.moviedb.data.model.GenreResponse
import test.mandiri.moviedb.data.model.MovieDetail
import test.mandiri.moviedb.data.model.MovieResponse
import test.mandiri.moviedb.data.model.ReviewResponse
import test.mandiri.moviedb.data.model.TrailerResponse

interface MovieApi {

    @GET("genre/movie/list")
    suspend fun genreList(
        @Query("language") language: String = "en"
    ): Response<GenreResponse>

    @GET("discover/movie")
    suspend fun movieListByGenre(
        @Query("language") language: String = "en",
        @Query("with_genres") withGenres: String,
        @Query("page") page: Int = 1,
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun movieDetail(
        @Path("movie_id") movieID: Int,
        @Query("language") language: String = "en",
    ): Response<MovieDetail>

    @GET("movie/{movie_id}/reviews")
    suspend fun reviews(
        @Path("movie_id") movieID: Int,
        @Query("language") language: String = "en",
        @Query("page") page: Int = 1,
    ): Response<ReviewResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun trailers(@Path("movie_id") movieID: Int): Response<TrailerResponse>

}
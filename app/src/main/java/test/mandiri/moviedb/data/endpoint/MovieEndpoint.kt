package test.mandiri.moviedb.data.endpoint

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import test.mandiri.moviedb.domain.model.MovieDetail
import test.mandiri.moviedb.domain.model.MovieResponse
import test.mandiri.moviedb.domain.model.ReviewResponse
import test.mandiri.moviedb.domain.model.TrailerResponse

interface MovieEndpoint {

    @GET("discover/movie")
    suspend fun fetchMoviesByGenre(
        @Query("language") language: String = "en",
        @Query("with_genres") withGenres: String,
        @Query("page") page: Int = 1,
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieID: Int,
        @Query("language") language: String = "en",
    ): Response<MovieDetail>

    @GET("movie/{movie_id}/reviews")
    suspend fun fetchMovieReviews(
        @Path("movie_id") movieID: Int,
        @Query("language") language: String = "en",
        @Query("page") page: Int = 1,
    ): Response<ReviewResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun fetchMovieTrailers(@Path("movie_id") movieID: Int):
            Response<TrailerResponse>
}
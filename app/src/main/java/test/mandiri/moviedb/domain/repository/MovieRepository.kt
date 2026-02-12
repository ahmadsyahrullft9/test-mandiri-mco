package test.mandiri.moviedb.domain.repository

import test.mandiri.moviedb.domain.model.MovieDetail
import test.mandiri.moviedb.domain.model.MovieResponse
import test.mandiri.moviedb.domain.model.ReviewResponse
import test.mandiri.moviedb.domain.model.TrailerResponse

interface MovieRepository {
    suspend fun fetchMoviesByGenre(genreID: Int, page: Int): MovieResponse

    suspend fun getMovieDetail(movieID: Int): MovieDetail

    suspend fun fetchMovieReviews(movieID: Int, page: Int): ReviewResponse

    suspend fun fetchMovieTrailers(movieID: Int): TrailerResponse
}
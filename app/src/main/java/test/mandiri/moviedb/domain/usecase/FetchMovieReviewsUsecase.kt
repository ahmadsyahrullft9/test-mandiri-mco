package test.mandiri.moviedb.domain.usecase

import test.mandiri.moviedb.core.base.IOUsecase
import test.mandiri.moviedb.domain.model.Review
import test.mandiri.moviedb.domain.model.ReviewRequest
import test.mandiri.moviedb.domain.repository.MovieRepository

class FetchMovieReviewsUsecase(private val movieRepository: MovieRepository) :
    IOUsecase<List<Review>, ReviewRequest>() {
    override suspend fun call(input: ReviewRequest): List<Review> {
        val movieID = input.movie_id ?: throw Exception("invalid movie id")
        val page = input.page ?: 1

        val response = movieRepository.fetchMovieReviews(movieID = movieID, page = page)
        return response.results.ifEmpty { throw Exception("movie review not found") }
    }

}
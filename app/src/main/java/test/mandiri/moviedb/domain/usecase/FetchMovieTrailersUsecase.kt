package test.mandiri.moviedb.domain.usecase

import test.mandiri.moviedb.core.base.IOUsecase
import test.mandiri.moviedb.domain.model.Trailer
import test.mandiri.moviedb.domain.repository.MovieRepository

class FetchMovieTrailersUsecase(private val movieRepository: MovieRepository) :
    IOUsecase<List<Trailer>, Int>() {

    override suspend fun call(input: Int): List<Trailer> {
        val response = movieRepository.fetchMovieTrailers(input)
        return response.results.ifEmpty { throw Exception("movie trailer not found") }
    }

}
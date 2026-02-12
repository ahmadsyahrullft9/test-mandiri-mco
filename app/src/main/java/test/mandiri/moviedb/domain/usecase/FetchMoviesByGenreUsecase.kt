package test.mandiri.moviedb.domain.usecase

import test.mandiri.moviedb.core.base.IOUsecase
import test.mandiri.moviedb.domain.model.Movie
import test.mandiri.moviedb.domain.model.MovieRequest
import test.mandiri.moviedb.domain.repository.MovieRepository

class FetchMoviesByGenreUsecase(private val movieRepository: MovieRepository) :
    IOUsecase<List<Movie>, MovieRequest>() {

    override suspend fun call(input: MovieRequest): List<Movie> {
        val genreID = input.genre_id ?: throw Exception("invalid movie genre id")
        val page = input.page ?: 1

        val response = movieRepository.fetchMoviesByGenre(genreID = genreID, page = page)
        return if (response.results != null && response.results.isNotEmpty()) {
            response.results
        } else
            throw Exception("movie not found")
    }
}

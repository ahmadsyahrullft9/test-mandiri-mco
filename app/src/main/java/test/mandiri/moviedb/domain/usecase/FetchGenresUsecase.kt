package test.mandiri.moviedb.domain.usecase

import test.mandiri.moviedb.core.base.BaseUsecase
import test.mandiri.moviedb.domain.model.Genre
import test.mandiri.moviedb.domain.repository.GenreRepository

class FetchGenresUsecase(private val genreRepository: GenreRepository) :
    BaseUsecase<List<Genre>>() {
    override suspend fun call(): List<Genre> {
        val response = genreRepository.fetchGenres()
        return if (response.genres != null && response.genres.isNotEmpty()) {
            response.genres
        } else
            throw Exception("movie genre not found")
    }
}
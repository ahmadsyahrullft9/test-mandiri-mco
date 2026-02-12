package test.mandiri.moviedb.domain.repository

import test.mandiri.moviedb.domain.model.GenreResponse

interface GenreRepository {
    suspend fun fetchGenres(): GenreResponse
}
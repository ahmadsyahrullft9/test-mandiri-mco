package test.mandiri.moviedb.domain.usecase

import test.mandiri.moviedb.core.base.IOUsecase
import test.mandiri.moviedb.domain.model.MovieDetail
import test.mandiri.moviedb.domain.repository.MovieRepository


class GetMovieDetailUsecase(private val movieRepository: MovieRepository) :
    IOUsecase<MovieDetail, Int>() {

    override suspend fun call(input: Int): MovieDetail {
        return movieRepository.getMovieDetail(input)
    }
}
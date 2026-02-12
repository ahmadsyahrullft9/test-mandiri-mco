package test.mandiri.moviedb.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import test.mandiri.moviedb.core.util.ApiClient
import test.mandiri.moviedb.data.endpoint.GenreEndpoint
import test.mandiri.moviedb.data.endpoint.MovieEndpoint
import test.mandiri.moviedb.data.repository.GenreRepositoryIml
import test.mandiri.moviedb.data.repository.MovieRepositoryIml
import test.mandiri.moviedb.domain.repository.GenreRepository
import test.mandiri.moviedb.domain.repository.MovieRepository
import test.mandiri.moviedb.presentation.screen.genre.GenreViewModel
import test.mandiri.moviedb.presentation.screen.movie_list.MovieListViewModel
import test.mandiri.moviedb.presentation.screen.movie_overview.MovieOverviewViewModel
import test.mandiri.moviedb.presentation.screen.movie_review.MovieReviewViewModel
import test.mandiri.moviedb.presentation.screen.movie_trailer.MovieTrailerViewModel

val mainModule = module {
    single<Retrofit> { ApiClient.getClient() }

    single<GenreEndpoint> { get<Retrofit>().create(GenreEndpoint::class.java) }
    single<MovieEndpoint> { get<Retrofit>().create(MovieEndpoint::class.java) }

    single<GenreRepository> { GenreRepositoryIml(apiService = get()) }
    single<MovieRepository> { MovieRepositoryIml(apiService = get()) }

    viewModelOf(::GenreViewModel)
    viewModelOf(::MovieListViewModel)
    viewModelOf(::MovieOverviewViewModel)
    viewModelOf(::MovieTrailerViewModel)
    viewModelOf(::MovieReviewViewModel)
}
package test.mandiri.moviedb.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import test.mandiri.moviedb.data.repository.MovieRepository
import test.mandiri.moviedb.utils.ApiClient
import test.mandiri.moviedb.ui.viewmodel.MovieListViewModel
import test.mandiri.moviedb.ui.viewmodel.GenreViewModel
import test.mandiri.moviedb.ui.viewmodel.MovieDetailViewModel

val mainModule = module {
    single<Retrofit> { ApiClient.getClient() }
    single<MovieRepository> { MovieRepository(get()) }
    viewModel<GenreViewModel> { GenreViewModel(get()) }
    viewModel<MovieListViewModel> { MovieListViewModel(get()) }
    viewModel<MovieDetailViewModel> { MovieDetailViewModel(get()) }
}
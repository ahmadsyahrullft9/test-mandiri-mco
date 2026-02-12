package test.mandiri.moviedb.core.base

abstract class BaseUsecase<Output> {
    abstract suspend fun call(): Output
}

abstract class IOUsecase<Output, Input> {
    abstract suspend fun call(input: Input): Output
}
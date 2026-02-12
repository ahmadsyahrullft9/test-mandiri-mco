package test.mandiri.moviedb.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MovieRequest(
    @SerialName("genre_id")
    val genre_id: Int? = null,
    @SerialName("page")
    val page: Int? = null
)

@Serializable
data class MovieResponse(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<Movie>? = null,
    @SerialName("total_pages")
    val total_pages: Int? = null,
    @SerialName("total_results")
    val total_results: Int? = null,
)

@Serializable
data class Movie(
    @SerialName("adult")
    val adult: Boolean? = null,
    @SerialName("backdrop_path")
    val backdrop_path: String? = null,
    @SerialName("genre_ids")
    val genre_ids: ArrayList<Int> = arrayListOf(),
    @SerialName("id")
    val id: Int? = null,
    @SerialName("original_language")
    val original_language: String? = null,
    @SerialName("original_title")
    val original_title: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("poster_path")
    val poster_path: String? = null,
    @SerialName("release_date")
    val release_date: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("video")
    val video: Boolean? = null,
    @SerialName("vote_average")
    val vote_average: Double? = null,
    @SerialName("vote_count")
    val vote_count: Int? = null
)
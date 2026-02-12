package test.mandiri.moviedb.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MovieDetail(
    @SerialName("adult") var adult: Boolean? = null,
    @SerialName("backdrop_path") var backdrop_path: String? = null,
    @SerialName("belongs_to_collection") var belongs_to_collection: BelongToCollection? = null,
    @SerialName("budget") var budget: Int? = null,
    @SerialName("genres") var genres: ArrayList<Genre> = arrayListOf(),
    @SerialName("homepage") var homepage: String? = null,
    @SerialName("id") var id: Int? = null,
    @SerialName("imdb_id") var imdb_id: String? = null,
    @SerialName("origin_country") var origin_country: ArrayList<String> = arrayListOf(),
    @SerialName("original_language") var original_language: String? = null,
    @SerialName("original_title") var original_title: String? = null,
    @SerialName("overview") var overview: String? = null,
    @SerialName("popularity") var popularity: Double? = null,
    @SerialName("poster_path") var poster_path: String? = null,
    @SerialName("production_companies") var production_companies: ArrayList<ProductionCompany> = arrayListOf(),
    @SerialName("production_countries") var production_countries: ArrayList<ProductionCountry> = arrayListOf(),
    @SerialName("release_date") var release_date: String? = null,
    @SerialName("revenue") var revenue: Float? = null,
    @SerialName("runtime") var runtime: Int? = null,
    @SerialName("spoken_languages") var spoken_languages: ArrayList<SpokenLanguage> = arrayListOf(),
    @SerialName("status") var status: String? = null,
    @SerialName("tagline") var tagline: String? = null,
    @SerialName("title") var title: String? = null,
    @SerialName("video") var video: Boolean? = null,
    @SerialName("vote_average") var vote_average: Double? = null,
    @SerialName("vote_count") var vote_count: Int? = null
)

@Serializable
data class BelongToCollection(
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("poster_path") var poster_path: String? = null,
    @SerialName("backdrop_path") var backdrop_path: String? = null

)

@Serializable
data class ProductionCompany(
    @SerialName("id") var id: Int? = null,
    @SerialName("logo_path") var logo_path: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("origin_country") var origin_country: String? = null
)

@Serializable
data class ProductionCountry(
    @SerialName("iso_3166_1") var iso_3166_1: String? = null,
    @SerialName("name") var name: String? = null
)


@Serializable
data class SpokenLanguage(
    @SerialName("english_name") var english_name: String? = null,
    @SerialName("iso_639_1") var iso_639_1: String? = null,
    @SerialName("name") var name: String? = null
)
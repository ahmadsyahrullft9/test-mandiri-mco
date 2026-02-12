package test.mandiri.moviedb.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TrailerResponse(
    @SerialName("id") var id: Int? = null,
    @SerialName("results") var results: ArrayList<Trailer> = arrayListOf()
)

@Serializable
data class Trailer(
    @SerialName("iso_639_1") var iso_639_1: String? = null,
    @SerialName("iso_3166_1") var iso_3166_1: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("key") var key: String? = null,
    @SerialName("site") var site: String? = null,
    @SerialName("size") var size: Int? = null,
    @SerialName("type") var type: String? = null,
    @SerialName("official") var official: Boolean? = null,
    @SerialName("published_at") var published_at: String? = null,
    @SerialName("id") var id: String? = null
)
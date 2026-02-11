package test.mandiri.moviedb.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>? = null
)

@Serializable
data class Genre(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
)
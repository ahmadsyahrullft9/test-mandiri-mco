package test.mandiri.moviedb.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    @SerialName("movie_id") var movie_id: Int? = null,
    @SerialName("page") var page: Int? = null,
)

@Serializable
data class ReviewResponse(
    @SerialName("id") var id: Int? = null,
    @SerialName("page") var page: Int? = null,
    @SerialName("results") var results: ArrayList<Review> = arrayListOf(),
    @SerialName("total_pages") var total_pages: Int? = null,
    @SerialName("total_results") var total_results: Int? = null
)

@Serializable
data class Review(
    @SerialName("author") var author: String? = null,
    @SerialName("author_details") var author_details: AuthorDetail? = AuthorDetail(),
    @SerialName("content") var content: String? = null,
    @SerialName("created_at") var created_at: String? = null,
    @SerialName("id") var id: String? = null,
    @SerialName("updated_at") var updated_at: String? = null,
    @SerialName("url") var url: String? = null
)


@Serializable
data class AuthorDetail(
    @SerialName("name") var name: String? = null,
    @SerialName("username") var username: String? = null,
    @SerialName("avatar_path") var avatar_path: String? = null,
    @SerialName("rating") var rating: String? = null
)
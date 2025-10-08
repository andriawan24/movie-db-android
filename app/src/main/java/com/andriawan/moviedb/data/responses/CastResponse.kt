package com.andriawan.moviedb.data.responses


import com.andriawan.moviedb.domain.models.Cast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastResponse(
    @SerialName("cast_id")
    val castId: Int? = null,
    @SerialName("character")
    val character: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("order")
    val order: Int? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("profile_path")
    val profilePath: String? = null
) {
    companion object {
        fun CastResponse.toDomain(): Cast {
            return Cast(
                castId = this.castId ?: 0,
                character = this.character.orEmpty(),
                id = this.id ?: 0,
                name = this.name.orEmpty(),
                order = this.order ?: 0,
                originalName = this.originalName.orEmpty(),
                popularity = this.popularity ?: 0.0,
                profilePath = this.profilePath.orEmpty()
            )
        }
    }
}
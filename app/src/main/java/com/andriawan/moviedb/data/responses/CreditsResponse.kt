package com.andriawan.moviedb.data.responses


import com.andriawan.moviedb.data.responses.CastResponse.Companion.toDomain
import com.andriawan.moviedb.domain.models.Credits
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditsResponse(
    @SerialName("cast")
    val cast: List<CastResponse>? = null,
    @SerialName("id")
    val id: Int? = null
) {
    companion object {
        fun CreditsResponse.toDomain(): Credits {
            return Credits(
                id = this.id ?: 0,
                cast = this.cast?.map { it.toDomain() }.orEmpty()
            )
        }
    }
}
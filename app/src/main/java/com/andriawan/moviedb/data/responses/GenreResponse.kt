package com.andriawan.moviedb.data.responses


import com.andriawan.moviedb.domain.models.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null
) {
    companion object {
        fun GenreResponse.toDomain(): Genre {
            return Genre(
                id = id ?: 0,
                name = name.orEmpty()
            )
        }
    }
}
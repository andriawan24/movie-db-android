package com.andriawan.moviedb.data.responses


import com.andriawan.moviedb.domain.models.SpokenLanguage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpokenLanguageResponse(
    @SerialName("english_name")
    val englishName: String? = null,
    @SerialName("iso_639_1")
    val iso6391: String? = null,
    @SerialName("name")
    val name: String? = null
) {
    companion object {
        fun SpokenLanguageResponse.toDomain(): SpokenLanguage {
            return SpokenLanguage(
                englishName = englishName.orEmpty(),
                iso6391 = iso6391.orEmpty(),
                name = name.orEmpty()
            )
        }
    }
}
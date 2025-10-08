package com.andriawan.moviedb.data.responses


import com.andriawan.moviedb.domain.models.ProductionCountry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountryResponse(
    @SerialName("iso_3166_1")
    val iso31661: String? = null,
    @SerialName("name")
    val name: String? = null
) {
    companion object {
        fun ProductionCountryResponse.toDomain(): ProductionCountry {
            return ProductionCountry(
                iso31661 = iso31661.orEmpty(),
                name = name.orEmpty()
            )
        }
    }
}
package com.andriawan.moviedb.data.responses


import com.andriawan.moviedb.domain.models.ProductionCompany
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompanyResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("logo_path")
    val logoPath: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("origin_country")
    val originCountry: String? = null
) {
    companion object {
        fun ProductionCompanyResponse.toDomain(): ProductionCompany {
            return ProductionCompany(
                id = id ?: 0,
                logoPath = logoPath.orEmpty(),
                name = name.orEmpty(),
                originCountry = originCountry.orEmpty()
            )
        }
    }
}
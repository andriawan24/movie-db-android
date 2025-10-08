package com.andriawan.moviedb.data.responses


import com.andriawan.moviedb.data.responses.GenreResponse.Companion.toDomain
import com.andriawan.moviedb.data.responses.ProductionCompanyResponse.Companion.toDomain
import com.andriawan.moviedb.data.responses.ProductionCountryResponse.Companion.toDomain
import com.andriawan.moviedb.data.responses.SpokenLanguageResponse.Companion.toDomain
import com.andriawan.moviedb.domain.models.MovieDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
    @SerialName("adult")
    val adult: Boolean? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("budget")
    val budget: Int? = null,
    @SerialName("genres")
    val genres: List<GenreResponse?>? = null,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyResponse>? = null,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountryResponse>? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("revenue")
    val revenue: Int? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguageResponse>? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("tagline")
    val tagline: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("video")
    val video: Boolean? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null
) {
    companion object {
        fun MovieDetailResponse.toDomain(): MovieDetail {
            return MovieDetail(
                adult = adult ?: false,
                backdropPath = backdropPath.orEmpty(),
                budget = budget ?: 0,
                genres = genres?.filterNotNull()?.map { it.toDomain() }.orEmpty(),
                homepage = homepage.orEmpty(),
                id = id ?: 0,
                imdbId = imdbId.orEmpty(),
                originalLanguage = originalLanguage.orEmpty(),
                originalTitle = originalTitle.orEmpty(),
                overview = overview.orEmpty(),
                popularity = popularity ?: 0.0,
                posterPath = posterPath.orEmpty(),
                productionCompanies = productionCompanies?.map { it.toDomain() }.orEmpty(),
                productionCountries = productionCountries?.map { it.toDomain() }.orEmpty(),
                releaseDate = releaseDate.orEmpty(),
                revenue = revenue ?: 0,
                runtime = runtime ?: 0,
                spokenLanguages = spokenLanguages?.map { it.toDomain() }.orEmpty(),
                status = status.orEmpty(),
                tagline = tagline.orEmpty(),
                title = title.orEmpty(),
                video = video ?: false,
                voteAverage = voteAverage ?: 0.0,
                voteCount = voteCount ?: 0
            )
        }
    }
}
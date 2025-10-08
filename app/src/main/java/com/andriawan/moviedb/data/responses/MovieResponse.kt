package com.andriawan.moviedb.data.responses

import com.andriawan.moviedb.domain.models.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("adult")
    val adult: Boolean? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("id")
    val id: Int? = null,
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
    @SerialName("release_date")
    val releaseDate: String? = null,
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
        fun MovieResponse.toDomain(): Movie {
            return Movie(
                id = this.id ?: 0,
                title = this.title.orEmpty(),
                adult = this.adult ?: false,
                backdropPath = this.backdropPath ?: "",
                genreIds = this.genreIds ?: emptyList(),
                originalLanguage = this.originalLanguage.orEmpty(),
                originalTitle = this.originalTitle.orEmpty(),
                overview = this.overview.orEmpty(),
                popularity = this.popularity ?: 0.0,
                posterPath = this.posterPath ?: "",
                releaseDate = this.releaseDate ?: "",
                video = this.video ?: false,
                voteAverage = this.voteAverage ?: 0.0,
                voteCount = this.voteCount ?: 0
            )
        }
    }
}
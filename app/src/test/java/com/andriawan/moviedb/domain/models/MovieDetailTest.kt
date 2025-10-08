package com.andriawan.moviedb.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieDetailTest {

    @Test
    fun `MovieDetail creation with all parameters`() {
        val movieDetail = MovieDetail(
            id = 123,
            title = "Avengers: Endgame",
            adult = false,
            backdropPath = "/backdrop.jpg",
            budget = 200000000,
            genres = listOf(
                Genre(id = 28, name = "Action"),
                Genre(id = 12, name = "Adventure")
            ),
            homepage = "https://example.com",
            imdbId = "tt4154796",
            originalLanguage = "en",
            originalTitle = "Avengers: Endgame",
            overview = "The epic conclusion",
            popularity = 150.5,
            posterPath = "/poster.jpg",
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            releaseDate = "2019-04-26",
            revenue = 500000000,
            runtime = 181,
            spokenLanguages = emptyList(),
            status = "Released",
            tagline = "Avenge the fallen",
            video = false,
            voteAverage = 8.4,
            voteCount = 5000
        )

        assertEquals(123, movieDetail.id)
        assertEquals("Avengers: Endgame", movieDetail.title)
        assertFalse(movieDetail.adult)
        assertEquals("/backdrop.jpg", movieDetail.backdropPath)
        assertEquals(200000000, movieDetail.budget)
        assertEquals(2, movieDetail.genres.size)
        assertEquals("https://example.com", movieDetail.homepage)
        assertEquals("tt4154796", movieDetail.imdbId)
        assertEquals("en", movieDetail.originalLanguage)
        assertEquals("Avengers: Endgame", movieDetail.originalTitle)
        assertEquals("The epic conclusion", movieDetail.overview)
        assertEquals(150.5, movieDetail.popularity, 0.01)
        assertEquals("/poster.jpg", movieDetail.posterPath)
        assertEquals("2019-04-26", movieDetail.releaseDate)
        assertEquals(500000000, movieDetail.revenue)
        assertEquals(181, movieDetail.runtime)
        assertEquals("Released", movieDetail.status)
        assertEquals("Avenge the fallen", movieDetail.tagline)
        assertFalse(movieDetail.video)
        assertEquals(8.4, movieDetail.voteAverage, 0.01)
        assertEquals(5000, movieDetail.voteCount)
    }

    @Test
    fun `MovieDetail with default values`() {
        val movieDetail = MovieDetail()

        assertEquals(0, movieDetail.id)
        assertEquals("", movieDetail.title)
        assertFalse(movieDetail.adult)
        assertEquals("", movieDetail.backdropPath)
        assertEquals(0, movieDetail.budget)
        assertEquals(emptyList<Genre>(), movieDetail.genres)
        assertEquals("", movieDetail.homepage)
        assertEquals("", movieDetail.imdbId)
        assertEquals("", movieDetail.originalLanguage)
        assertEquals("", movieDetail.originalTitle)
        assertEquals("", movieDetail.overview)
        assertEquals(0.0, movieDetail.popularity, 0.01)
        assertEquals("", movieDetail.posterPath)
        assertEquals(emptyList<ProductionCompany>(), movieDetail.productionCompanies)
        assertEquals(emptyList<ProductionCountry>(), movieDetail.productionCountries)
        assertEquals("", movieDetail.releaseDate)
        assertEquals(0, movieDetail.revenue)
        assertEquals(0, movieDetail.runtime)
        assertEquals(emptyList<SpokenLanguage>(), movieDetail.spokenLanguages)
        assertEquals("", movieDetail.status)
        assertEquals("", movieDetail.tagline)
        assertFalse(movieDetail.video)
        assertEquals(0.0, movieDetail.voteAverage, 0.01)
        assertEquals(0, movieDetail.voteCount)
    }

    @Test
    fun `MovieDetail equality with same values`() {
        val genres = listOf(Genre(id = 28, name = "Action"))
        val movieDetail1 = MovieDetail(
            id = 1,
            title = "Test Movie",
            genres = genres
        )
        val movieDetail2 = MovieDetail(
            id = 1,
            title = "Test Movie",
            genres = genres
        )

        assertEquals(movieDetail1, movieDetail2)
        assertEquals(movieDetail1.hashCode(), movieDetail2.hashCode())
    }

    @Test
    fun `MovieDetail inequality with different values`() {
        val movieDetail1 = MovieDetail(id = 1, title = "Movie 1")
        val movieDetail2 = MovieDetail(id = 2, title = "Movie 2")

        assertNotEquals(movieDetail1, movieDetail2)
    }

    @Test
    fun `MovieDetail copy function works correctly`() {
        val original = MovieDetail(id = 1, title = "Original")
        val copied = original.copy(title = "Modified")

        assertEquals(1, copied.id)
        assertEquals("Modified", copied.title)
        assertNotEquals(original, copied)
    }

    @Test
    fun `MovieDetail handles multiple genres`() {
        val movieDetail = MovieDetail(
            genres = listOf(
                Genre(id = 28, name = "Action"),
                Genre(id = 12, name = "Adventure"),
                Genre(id = 16, name = "Animation")
            )
        )

        assertEquals(3, movieDetail.genres.size)
        assertEquals("Action", movieDetail.genres[0].name)
        assertEquals("Adventure", movieDetail.genres[1].name)
        assertEquals("Animation", movieDetail.genres[2].name)
    }

    @Test
    fun `MovieDetail handles budget and revenue`() {
        val movieDetail = MovieDetail(
            budget = 150000000,
            revenue = 500000000
        )

        assertEquals(150000000, movieDetail.budget)
        assertEquals(500000000, movieDetail.revenue)
        assertTrue(movieDetail.revenue > movieDetail.budget)
    }

    @Test
    fun `MovieDetail handles runtime in minutes`() {
        val movieDetail = MovieDetail(
            runtime = 142
        )

        assertEquals(142, movieDetail.runtime)
    }

    @Test
    fun `MovieDetail handles status values`() {
        val releasedMovie = MovieDetail(status = "Released")
        val upcomingMovie = MovieDetail(status = "Post Production")

        assertEquals("Released", releasedMovie.status)
        assertEquals("Post Production", upcomingMovie.status)
    }

    @Test
    fun `MovieDetail handles IMDb ID format`() {
        val movieDetail = MovieDetail(imdbId = "tt4154796")

        assertEquals("tt4154796", movieDetail.imdbId)
        assertTrue(movieDetail.imdbId.startsWith("tt"))
    }

    @Test
    fun `MovieDetail handles empty collections`() {
        val movieDetail = MovieDetail(
            genres = emptyList(),
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            spokenLanguages = emptyList()
        )

        assertTrue(movieDetail.genres.isEmpty())
        assertTrue(movieDetail.productionCompanies.isEmpty())
        assertTrue(movieDetail.productionCountries.isEmpty())
        assertTrue(movieDetail.spokenLanguages.isEmpty())
    }

    @Test
    fun `MovieDetail handles adult content flag`() {
        val adultMovie = MovieDetail(adult = true)
        val familyMovie = MovieDetail(adult = false)

        assertTrue(adultMovie.adult)
        assertFalse(familyMovie.adult)
    }

    @Test
    fun `MovieDetail handles video flag`() {
        val videoContent = MovieDetail(video = true)
        val movieContent = MovieDetail(video = false)

        assertTrue(videoContent.video)
        assertFalse(movieContent.video)
    }

    @Test
    fun `MovieDetail handles tagline`() {
        val movieDetail = MovieDetail(tagline = "An epic adventure awaits")

        assertEquals("An epic adventure awaits", movieDetail.tagline)
    }

    @Test
    fun `MovieDetail handles homepage URL`() {
        val movieDetail = MovieDetail(homepage = "https://www.marvel.com/movies/avengers-endgame")

        assertEquals("https://www.marvel.com/movies/avengers-endgame", movieDetail.homepage)
        assertTrue(movieDetail.homepage.startsWith("https://"))
    }

    @Test
    fun `MovieDetail handles zero budget`() {
        val movieDetail = MovieDetail(budget = 0)

        assertEquals(0, movieDetail.budget)
    }

    @Test
    fun `MovieDetail handles zero revenue`() {
        val movieDetail = MovieDetail(revenue = 0)

        assertEquals(0, movieDetail.revenue)
    }
}
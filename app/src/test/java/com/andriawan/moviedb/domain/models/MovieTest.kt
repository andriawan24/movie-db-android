package com.andriawan.moviedb.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieTest {

    @Test
    fun `Movie creation with all parameters`() {
        val movie = Movie(
            id = 123,
            title = "Avengers: Endgame",
            adult = false,
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(28, 12),
            originalLanguage = "en",
            originalTitle = "Avengers: Endgame",
            overview = "The epic conclusion",
            popularity = 150.5,
            posterPath = "/poster.jpg",
            releaseDate = "2019-04-26",
            video = false,
            voteAverage = 8.4,
            voteCount = 5000
        )

        assertEquals(123, movie.id)
        assertEquals("Avengers: Endgame", movie.title)
        assertFalse(movie.adult)
        assertEquals("/backdrop.jpg", movie.backdropPath)
        assertEquals(listOf(28, 12), movie.genreIds)
        assertEquals("en", movie.originalLanguage)
        assertEquals("Avengers: Endgame", movie.originalTitle)
        assertEquals("The epic conclusion", movie.overview)
        assertEquals(150.5, movie.popularity, 0.01)
        assertEquals("/poster.jpg", movie.posterPath)
        assertEquals("2019-04-26", movie.releaseDate)
        assertFalse(movie.video)
        assertEquals(8.4, movie.voteAverage, 0.01)
        assertEquals(5000, movie.voteCount)
    }

    @Test
    fun `Movie with default values`() {
        val movie = Movie(
            adult = false,
            genreIds = emptyList(),
            popularity = 0.0,
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )

        assertEquals(0, movie.id)
        assertEquals("", movie.title)
        assertEquals("", movie.backdropPath)
        assertEquals(emptyList<Int>(), movie.genreIds)
        assertEquals("", movie.originalLanguage)
        assertEquals("", movie.originalTitle)
        assertEquals("", movie.overview)
        assertEquals("", movie.posterPath)
        assertEquals("", movie.releaseDate)
    }

    @Test
    fun `Movie equality with same values`() {
        val movie1 = Movie(
            id = 1,
            title = "Test",
            adult = false,
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(1, 2),
            originalLanguage = "en",
            originalTitle = "Test",
            overview = "Overview",
            popularity = 10.0,
            posterPath = "/poster.jpg",
            releaseDate = "2024-01-01",
            video = false,
            voteAverage = 7.5,
            voteCount = 100
        )
        val movie2 = Movie(
            id = 1,
            title = "Test",
            adult = false,
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(1, 2),
            originalLanguage = "en",
            originalTitle = "Test",
            overview = "Overview",
            popularity = 10.0,
            posterPath = "/poster.jpg",
            releaseDate = "2024-01-01",
            video = false,
            voteAverage = 7.5,
            voteCount = 100
        )

        assertEquals(movie1, movie2)
        assertEquals(movie1.hashCode(), movie2.hashCode())
    }

    @Test
    fun `Movie inequality with different values`() {
        val movie1 = Movie(
            id = 1,
            title = "Movie 1",
            adult = false,
            genreIds = emptyList(),
            popularity = 0.0,
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )
        val movie2 = Movie(
            id = 2,
            title = "Movie 2",
            adult = false,
            genreIds = emptyList(),
            popularity = 0.0,
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )

        assertNotEquals(movie1, movie2)
    }

    @Test
    fun `Movie copy function works correctly`() {
        val original = Movie(
            id = 1,
            title = "Original",
            adult = false,
            genreIds = emptyList(),
            popularity = 0.0,
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )

        val copied = original.copy(title = "Modified")

        assertEquals(1, copied.id)
        assertEquals("Modified", copied.title)
        assertNotEquals(original, copied)
    }

    @Test
    fun `Movie handles adult content flag`() {
        val adultMovie = Movie(
            id = 1,
            adult = true,
            genreIds = emptyList(),
            popularity = 0.0,
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )

        assertTrue(adultMovie.adult)
    }

    @Test
    fun `Movie handles video flag`() {
        val videoMovie = Movie(
            id = 1,
            adult = false,
            genreIds = emptyList(),
            popularity = 0.0,
            video = true,
            voteAverage = 0.0,
            voteCount = 0
        )

        assertTrue(videoMovie.video)
    }

    @Test
    fun `Movie handles multiple genre IDs`() {
        val movie = Movie(
            id = 1,
            adult = false,
            genreIds = listOf(28, 12, 16, 35),
            popularity = 0.0,
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )

        assertEquals(4, movie.genreIds.size)
        assertTrue(movie.genreIds.contains(28))
        assertTrue(movie.genreIds.contains(12))
        assertTrue(movie.genreIds.contains(16))
        assertTrue(movie.genreIds.contains(35))
    }

    @Test
    fun `Movie handles empty genre list`() {
        val movie = Movie(
            id = 1,
            adult = false,
            genreIds = emptyList(),
            popularity = 0.0,
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )

        assertTrue(movie.genreIds.isEmpty())
    }

    @Test
    fun `Movie handles high vote average`() {
        val movie = Movie(
            id = 1,
            adult = false,
            genreIds = emptyList(),
            popularity = 0.0,
            video = false,
            voteAverage = 10.0,
            voteCount = 10000
        )

        assertEquals(10.0, movie.voteAverage, 0.01)
        assertEquals(10000, movie.voteCount)
    }

    @Test
    fun `Movie handles low vote average`() {
        val movie = Movie(
            id = 1,
            adult = false,
            genreIds = emptyList(),
            popularity = 0.0,
            video = false,
            voteAverage = 1.0,
            voteCount = 5
        )

        assertEquals(1.0, movie.voteAverage, 0.01)
        assertEquals(5, movie.voteCount)
    }
}
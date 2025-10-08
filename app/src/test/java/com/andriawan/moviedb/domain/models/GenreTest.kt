package com.andriawan.moviedb.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GenreTest {

    @Test
    fun `Genre creation with all parameters`() {
        val genre = Genre(id = 28, name = "Action")

        assertEquals(28, genre.id)
        assertEquals("Action", genre.name)
    }

    @Test
    fun `Genre with default values`() {
        val genre = Genre()

        assertEquals(0, genre.id)
        assertEquals("", genre.name)
    }

    @Test
    fun `Genre equality with same values`() {
        val genre1 = Genre(id = 28, name = "Action")
        val genre2 = Genre(id = 28, name = "Action")

        assertEquals(genre1, genre2)
        assertEquals(genre1.hashCode(), genre2.hashCode())
    }

    @Test
    fun `Genre inequality with different values`() {
        val genre1 = Genre(id = 28, name = "Action")
        val genre2 = Genre(id = 12, name = "Adventure")

        assertNotEquals(genre1, genre2)
    }

    @Test
    fun `Genre copy function works correctly`() {
        val original = Genre(id = 28, name = "Action")
        val copied = original.copy(name = "Adventure")

        assertEquals(28, copied.id)
        assertEquals("Adventure", copied.name)
        assertNotEquals(original, copied)
    }

    @Test
    fun `Genre handles different genre types`() {
        val action = Genre(id = 28, name = "Action")
        val adventure = Genre(id = 12, name = "Adventure")
        val animation = Genre(id = 16, name = "Animation")
        val comedy = Genre(id = 35, name = "Comedy")
        val crime = Genre(id = 80, name = "Crime")

        assertEquals("Action", action.name)
        assertEquals("Adventure", adventure.name)
        assertEquals("Animation", animation.name)
        assertEquals("Comedy", comedy.name)
        assertEquals("Crime", crime.name)
    }

    @Test
    fun `Genre ID uniquely identifies genre`() {
        val genre1 = Genre(id = 28, name = "Action")
        val genre2 = Genre(id = 28, name = "Different Name")

        assertNotEquals(genre1, genre2) // Names are different
        assertEquals(genre1.id, genre2.id) // IDs are same
    }
}
package com.andriawan.moviedb.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CreditsTest {

    @Test
    fun `Credits creation with all parameters`() {
        val cast = listOf(
            Cast(
                castId = 1,
                character = "Iron Man",
                id = 100,
                name = "Robert Downey Jr.",
                order = 0,
                originalName = "Robert Downey Jr.",
                popularity = 50.5,
                profilePath = "/profile.jpg"
            )
        )
        val credits = Credits(
            cast = cast,
            id = 123
        )

        assertEquals(cast, credits.cast)
        assertEquals(123, credits.id)
        assertEquals(1, credits.cast.size)
    }

    @Test
    fun `Credits with default values`() {
        val credits = Credits()

        assertEquals(emptyList<Cast>(), credits.cast)
        assertEquals(0, credits.id)
    }

    @Test
    fun `Credits equality with same values`() {
        val cast = listOf(
            Cast(castId = 1, character = "Hero", id = 100, name = "John Doe")
        )
        val credits1 = Credits(cast = cast, id = 1)
        val credits2 = Credits(cast = cast, id = 1)

        assertEquals(credits1, credits2)
        assertEquals(credits1.hashCode(), credits2.hashCode())
    }

    @Test
    fun `Credits inequality with different values`() {
        val credits1 = Credits(id = 1)
        val credits2 = Credits(id = 2)

        assertNotEquals(credits1, credits2)
    }

    @Test
    fun `Credits copy function works correctly`() {
        val original = Credits(id = 1, cast = emptyList())
        val newCast = listOf(Cast(id = 100, name = "John Doe"))
        val copied = original.copy(cast = newCast)

        assertEquals(1, copied.id)
        assertEquals(newCast, copied.cast)
        assertNotEquals(original, copied)
    }

    @Test
    fun `Credits handles multiple cast members`() {
        val credits = Credits(
            cast = listOf(
                Cast(castId = 1, character = "Hero", id = 100, name = "John Doe"),
                Cast(castId = 2, character = "Villain", id = 101, name = "Jane Smith"),
                Cast(castId = 3, character = "Sidekick", id = 102, name = "Bob Johnson")
            ),
            id = 123
        )

        assertEquals(3, credits.cast.size)
        assertEquals("Hero", credits.cast[0].character)
        assertEquals("Villain", credits.cast[1].character)
        assertEquals("Sidekick", credits.cast[2].character)
    }

    @Test
    fun `Credits handles empty cast list`() {
        val credits = Credits(cast = emptyList(), id = 123)

        assertTrue(credits.cast.isEmpty())
        assertEquals(123, credits.id)
    }

    @Test
    fun `Credits with large cast list`() {
        val largeCast = List(50) { index ->
            Cast(
                castId = index,
                character = "Character $index",
                id = 100 + index,
                name = "Actor $index",
                order = index
            )
        }
        val credits = Credits(cast = largeCast, id = 456)

        assertEquals(50, credits.cast.size)
        assertEquals(456, credits.id)
        assertEquals("Character 0", credits.cast[0].character)
        assertEquals("Character 49", credits.cast[49].character)
    }
}
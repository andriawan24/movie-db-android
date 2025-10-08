package com.andriawan.moviedb.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CastTest {

    @Test
    fun `Cast creation with all parameters`() {
        val cast = Cast(
            castId = 14,
            character = "Tony Stark / Iron Man",
            id = 3223,
            name = "Robert Downey Jr.",
            order = 0,
            originalName = "Robert Downey Jr.",
            popularity = 50.5,
            profilePath = "/im9SAqJPZKEbVZGmjXuLI4O7RvM.jpg"
        )

        assertEquals(14, cast.castId)
        assertEquals("Tony Stark / Iron Man", cast.character)
        assertEquals(3223, cast.id)
        assertEquals("Robert Downey Jr.", cast.name)
        assertEquals(0, cast.order)
        assertEquals("Robert Downey Jr.", cast.originalName)
        assertEquals(50.5, cast.popularity, 0.01)
        assertEquals("/im9SAqJPZKEbVZGmjXuLI4O7RvM.jpg", cast.profilePath)
    }

    @Test
    fun `Cast with default values`() {
        val cast = Cast()

        assertEquals(0, cast.castId)
        assertEquals("", cast.character)
        assertEquals(0, cast.id)
        assertEquals("", cast.name)
        assertEquals(0, cast.order)
        assertEquals("", cast.originalName)
        assertEquals(0.0, cast.popularity, 0.01)
        assertEquals("", cast.profilePath)
    }

    @Test
    fun `Cast equality with same values`() {
        val cast1 = Cast(
            castId = 1,
            character = "Hero",
            id = 100,
            name = "John Doe",
            order = 0,
            originalName = "John Doe",
            popularity = 10.0,
            profilePath = "/profile.jpg"
        )
        val cast2 = Cast(
            castId = 1,
            character = "Hero",
            id = 100,
            name = "John Doe",
            order = 0,
            originalName = "John Doe",
            popularity = 10.0,
            profilePath = "/profile.jpg"
        )

        assertEquals(cast1, cast2)
        assertEquals(cast1.hashCode(), cast2.hashCode())
    }

    @Test
    fun `Cast inequality with different values`() {
        val cast1 = Cast(id = 1, name = "Actor 1")
        val cast2 = Cast(id = 2, name = "Actor 2")

        assertNotEquals(cast1, cast2)
    }

    @Test
    fun `Cast copy function works correctly`() {
        val original = Cast(id = 1, name = "Original Name", character = "Hero")
        val copied = original.copy(character = "Villain")

        assertEquals(1, copied.id)
        assertEquals("Original Name", copied.name)
        assertEquals("Villain", copied.character)
        assertNotEquals(original, copied)
    }

    @Test
    fun `Cast handles order value`() {
        val leadActor = Cast(order = 0, name = "Lead Actor")
        val supportingActor = Cast(order = 5, name = "Supporting Actor")

        assertEquals(0, leadActor.order)
        assertEquals(5, supportingActor.order)
    }

    @Test
    fun `Cast handles popularity score`() {
        val popularActor = Cast(
            name = "Popular Actor",
            popularity = 98.5
        )
        val unknownActor = Cast(
            name = "Unknown Actor",
            popularity = 2.3
        )

        assertEquals(98.5, popularActor.popularity, 0.01)
        assertEquals(2.3, unknownActor.popularity, 0.01)
    }

    @Test
    fun `Cast handles profile path`() {
        val cast = Cast(
            name = "Actor",
            profilePath = "/profiles/actor123.jpg"
        )

        assertEquals("/profiles/actor123.jpg", cast.profilePath)
    }

    @Test
    fun `Cast handles character with multiple roles`() {
        val cast = Cast(
            name = "Actor Name",
            character = "Character A / Character B"
        )

        assertEquals("Character A / Character B", cast.character)
    }

    @Test
    fun `Cast handles original name different from display name`() {
        val cast = Cast(
            name = "Jackie Chan",
            originalName = "陳港生"
        )

        assertEquals("Jackie Chan", cast.name)
        assertEquals("陳港生", cast.originalName)
    }

    @Test
    fun `Cast handles zero popularity`() {
        val cast = Cast(
            name = "New Actor",
            popularity = 0.0
        )

        assertEquals(0.0, cast.popularity, 0.01)
    }

    @Test
    fun `Cast handles high cast ID`() {
        val cast = Cast(
            castId = 999999,
            id = 888888,
            name = "Actor"
        )

        assertEquals(999999, cast.castId)
        assertEquals(888888, cast.id)
    }
}
package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimpleSanityTest {

    @Test
    fun addition_works() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun string_concatenation_works() {
        val result = "Spot" + "Seeker"
        assertEquals("SpotSeeker", result)
    }

    @Test
    fun list_contains_expected_item() {
        val items = listOf("home", "vote", "upload")
        assertTrue("vote" in items)
    }

    @Test
    fun multiplication_works() {
        val product = 3 * 7
        assertEquals(21, product)
    }

    @Test
    fun map_lookup_works() {
        val scores = mapOf("Alice" to 10, "Bob" to 20)
        assertEquals(20, scores["Bob"])
    }

    @Test
    fun boolean_condition_works() {
        val players = 4
        val isValidRoomSize = players in 2..10
        assertTrue(isValidRoomSize)
    }

    @Test
    fun list_sorting_works() {
        val unsorted = listOf(5, 1, 3)
        val sorted = unsorted.sorted()
        assertEquals(listOf(1, 3, 5), sorted)
    }
}

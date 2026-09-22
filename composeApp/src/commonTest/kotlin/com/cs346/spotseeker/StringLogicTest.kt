package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StringLogicTest {

    @Test
    fun trimming_and_lowercase_works() {
        val raw = "  SpotSeeker  "
        val processed = raw.trim().lowercase()
        assertEquals("spotseeker", processed)
    }

    @Test
    fun splitting_on_spaces_works() {
        val text = "vote upload home"
        val parts = text.split(" ")
        assertEquals(listOf("vote", "upload", "home"), parts)
    }

    @Test
    fun contains_substring_works() {
        val text = "Welcome to SpotSeeker"
        assertTrue(text.contains("Spot"))
    }
}

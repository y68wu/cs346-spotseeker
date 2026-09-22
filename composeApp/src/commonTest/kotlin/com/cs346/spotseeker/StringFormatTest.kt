package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StringFormatTest {

    private fun formatScore(name: String, score: Int): String {
        return "$name: $score"
    }

    @Test
    fun score_format_is_correct() {
        val formatted = formatScore("Chris", 10)
        assertEquals("Chris: 10", formatted)
    }

    @Test
    fun uppercase_and_replace_works() {
        val raw = "spotseeker vote"
        val processed = raw.uppercase().replace("VOTE", "UPLOAD")
        assertEquals("SPOTSEEKER UPLOAD", processed)
    }

    @Test
    fun drop_and_take_works() {
        val text = "SpotSeeker"
        val middle = text.drop(4).take(3)
        assertEquals("See", middle)
    }
}

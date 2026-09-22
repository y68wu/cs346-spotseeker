package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoomCodeGenerationTest {

    private fun generateRoomCode(seed: Int): String {
        // Super simple deterministic "generator" for testing
        val base = listOf('A', 'B', 'C', 'D', 'E', 'F')
        val c1 = base[seed % base.size]
        val c2 = base[(seed + 1) % base.size]
        val c3 = base[(seed + 2) % base.size]
        val c4 = base[(seed + 3) % base.size]
        return "$c1$c2$c3$c4"
    }

    @Test
    fun generated_code_has_length_four() {
        val code = generateRoomCode(0)
        assertEquals(4, code.length)
    }

    @Test
    fun generator_is_deterministic() {
        val code1 = generateRoomCode(3)
        val code2 = generateRoomCode(3)
        assertEquals(code1, code2)
    }

    @Test
    fun generator_uses_expected_letters_for_seed_zero() {
        val code = generateRoomCode(0)
        assertEquals("ABCD", code)
    }
}

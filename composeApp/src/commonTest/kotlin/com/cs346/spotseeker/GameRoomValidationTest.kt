package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameRoomValidationTest {

    private fun isValidRoomCode(code: String): Boolean {
        return code.length in 4..8 &&
            code.all { it.isLetterOrDigit() }
    }

    private fun isValidPlayerCount(count: Int): Boolean {
        return count in 1..10
    }

    @Test
    fun room_code_with_special_chars_is_invalid() {
        assertFalse(isValidRoomCode("AB#1"))
    }

    @Test
    fun room_code_with_right_length_is_valid() {
        assertTrue(isValidRoomCode("ROOM1"))
    }

    @Test
    fun too_many_players_is_invalid() {
        assertFalse(isValidPlayerCount(20))
    }

    @Test
    fun typical_player_count_is_valid() {
        assertTrue(isValidPlayerCount(4))
    }
}

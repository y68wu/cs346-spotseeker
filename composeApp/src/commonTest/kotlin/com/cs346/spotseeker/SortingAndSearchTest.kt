package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class SortingAndSearchTest {

    @Test
    fun sorted_by_length_works() {
        val names = listOf("AA", "BBBB", "C")
        val sorted = names.sortedBy { it.length }
        assertEquals(listOf("C", "AA", "BBBB"), sorted)
    }

    @Test
    fun binary_search_like_logic_works() {
        val sorted = listOf(1, 3, 5, 7, 9)
        val contains5 = 5 in sorted
        val contains2 = 2 in sorted
        assertTrue(contains5)
        assertFalse(contains2)
    }
}

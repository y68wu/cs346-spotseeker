package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListLogicTest {

    @Test
    fun filtering_even_numbers_works() {
        val nums = listOf(1, 2, 3, 4, 5, 6)
        val evens = nums.filter { it % 2 == 0 }
        assertEquals(listOf(2, 4, 6), evens)
    }

    @Test
    fun grouping_by_modulo_works() {
        val nums = listOf(1, 2, 3, 4, 5)
        val grouped = nums.groupBy { it % 2 }
        assertEquals(listOf(2, 4), grouped[0])
        assertEquals(listOf(1, 3, 5), grouped[1])
    }

    @Test
    fun distinct_preserves_order() {
        val nums = listOf(1, 2, 2, 3, 3, 3)
        val distinct = nums.distinct()
        assertEquals(listOf(1, 2, 3), distinct)
    }
}

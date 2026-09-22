package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CollectionOpsTest {

    @Test
    fun zip_and_sum_pairs_work() {
        val a = listOf(1, 2, 3)
        val b = listOf(4, 5, 6)
        val sums = a.zip(b) { x, y -> x + y }
        assertEquals(listOf(5, 7, 9), sums)
    }

    @Test
    fun chunked_operation_works() {
        val nums = (1..7).toList()
        val chunks = nums.chunked(3)
        assertEquals(listOf(1, 2, 3), chunks[0])
        assertEquals(listOf(4, 5, 6), chunks[1])
        assertEquals(listOf(7), chunks[2])
    }

    @Test
    fun flatten_nested_lists_works() {
        val nested = listOf(listOf(1, 2), listOf(3), listOf(4, 5))
        val flat = nested.flatten()
        assertEquals(listOf(1, 2, 3, 4, 5), flat)
    }
}

package com.cs346.spotseeker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MathUtilsTest {

    private fun average(nums: List<Int>): Double {
        return nums.sum().toDouble() / nums.size
    }

    @Test
    fun average_of_simple_list_is_correct() {
        val nums = listOf(2, 4, 6, 8)
        val avg = average(nums)
        assertEquals(5.0, avg)
    }

    @Test
    fun absolute_value_logic_works() {
        val x = -10
        val y = 10
        assertEquals(10, kotlin.math.abs(x))
        assertEquals(10, kotlin.math.abs(y))
    }
}

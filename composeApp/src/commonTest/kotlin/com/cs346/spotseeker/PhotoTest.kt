package com.cs346.spotseeker

import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PhotoTest {

    @Test
    fun goodBadCountersUpdate() {
        val p = Photo("p", id="id", address = Address(0.0, 0.0))
        assertEquals(0, p.good)
        assertEquals(0, p.bad)

        p.goodPhoto()
        p.goodPhoto()
        p.badPhoto()

        assertEquals(2, p.good)
        assertEquals(1, p.bad)
    }

    @Test
    fun questionFlagBecomesTrueWhenGoodEnough() {
        val p = Photo("p",  id="id", address = Address(0.0, 0.0))
        // simulate many goods
        repeat(12) { p.goodPhoto() }
        // give some bads
        repeat(3) { p.badPhoto() }

        // backicon = 12, bad = 3 => backicon>10 and (backicon-bad)>5 -> true
        assertTrue(p.question)
    }

    @Test
    fun questionFlagFalseWhenNotEnoughGood() {
        val p = Photo("p", id="id", address = Address(0.0, 0.0))
        repeat(9) { p.goodPhoto() }
        repeat(2) { p.badPhoto() }

        assertFalse(p.question)
    }
}

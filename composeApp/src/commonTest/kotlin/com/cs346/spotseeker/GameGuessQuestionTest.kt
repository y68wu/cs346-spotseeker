package com.cs346.spotseeker

import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.GameGuessQuestion
import com.cs346.spotseeker.domain.Photo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameGuessQuestionTest {

    @Test
    fun perfectGuess() {
        val addr = Address(5.0, 5.0)
        val photo = Photo("p1", addr)
        val q = GameGuessQuestion(photo)

        val score = q.calculateScore(addr)
        assertEquals(5000, score)
    }

    @Test
    fun closerGuessHasHigherScoreThanFarther() {
        val addr = Address(0.0, 0.0)
        val photo = Photo("p2", addr)
        val q = GameGuessQuestion(photo)

        val near = q.calculateScore(Address(0.0, 0.0))
        val far = q.calculateScore(Address(10.0, 0.0))

        assertTrue(near > far)
    }

    @Test
    fun equalDistanceGivesEqualScore() {
        val addr = Address(2.0, 3.0)
        val photo = Photo("p3", addr)
        val q = GameGuessQuestion(photo)

        val s1 = q.calculateScore(Address(3.0, 3.0)) // dx=1,dy=0
        val s2 = q.calculateScore(Address(2.0, 4.0)) // dx=0,dy=1

        assertEquals(s1, s2)
    }
}
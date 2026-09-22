package com.cs346.spotseeker

import com.cs346.spotseeker.data.PhotoRepository
import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Game
import com.cs346.spotseeker.domain.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameTest {

    @Test
    fun startGameTest() {
        val questionNum = 3
        // create users
    val user1 = User(1, "user1")
    val user2 = User(2, "user2")
        val players = listOf(user1, user2)
        // init photo repository that stores the photos
        val photoRepo = PhotoRepository()

        // create game object
        val game = Game(players, questionNum, photoRepo.getNPhotos(questionNum))

        // test questions are loaded
        assertNotNull(game.questions[questionNum-1])

        // test player scores are initialized to 0
        for (p in game.players) {
            assertEquals(0, game.playerScores[p])
        }

        game.endGame()
    }

    @Test
    fun submitAnswerTest() {
        val questionNum = 3
        // create users
    val user1 = User(1, "user1")
    val user2 = User(2, "user2")
        val players = listOf(user1, user2)
        // init photo repository that stores the photos
        val photoRepo = PhotoRepository()

        // create game object
        val game = Game(players, questionNum, photoRepo.getNPhotos(questionNum))

        // check player get scores for their guess
        val perfectGuess = game.questions[game.currentQuestion].photo.address
        game.submitAnswer(user1, perfectGuess)
        assertTrue(game.playerScores[user1]!! > 0.0)
        val user1Score = game.playerScores[user1]

        // check player can't guess multiple times
        try {
            game.submitAnswer(user1, perfectGuess)
        } catch (exception: Exception) {
            assertEquals(user1Score, game.playerScores[user1])
        }

        // check other player can still guess
        game.submitAnswer(user2, perfectGuess)
        assertTrue(game.playerScores[user2]!! > 0.0)

        // check players get same score for same guesses
        assertEquals(game.playerScores[user1], game.playerScores[user2])
    }

    // TODO: write tet cases for Game.loadNxtQuestion()
    @Test
    fun loadNextQuestionTest() {

        val questionNum = 2
        // create users
    val user1 = User(1, "user1")
    val user2 = User(2, "user2")
        val players = listOf(user1, user2)
        // init photo repository that stores the photos
        val photoRepo = PhotoRepository()

        // create game object
        val game = Game(players, questionNum, photoRepo.getNPhotos(questionNum))
        
        // start game which loads first question and prints
        assertEquals(false, game.gameEnded)
        // ensure currentQuestion was advanced to a valid index
        assertTrue(game.currentQuestion == 0)

        // try loading next question and check output again
        game.loadNextQuestion()
        assertEquals(false, game.gameEnded)

        // ensure currentQuestion was advanced to a valid index
        assertTrue(game.currentQuestion == 1)

        // load next question, which should end the game since there's only 2 questions
        game.loadNextQuestion()
        assertEquals(true, game.gameEnded)
        assertTrue(game.currentQuestion == game.questions.size) // currentQuestion should be equal to questions.size when game ends
    }

    // TODO: write more test cases (if there's any case not covered)
}
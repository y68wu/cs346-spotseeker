package com.cs346.spotseeker.domain

class Game(
    val players: List<User>,
    val questionNum: Int,
    val questionPhotos: List<Photo>
) {
    var currentQuestion: Int = -1
    val playerScores: MutableMap<User, Int> =
        players.associateWith { 0 }.toMutableMap()
    val playerAnswered: MutableMap<User, Boolean> =
        players.associateWith { false }.toMutableMap()

    var questions: List<GameGuessQuestion> = questionPhotos.map { photo ->
        GameGuessQuestion(photo)
    }

    var gameEnded: Boolean = false

    init {
        startGame()
    }

    private fun startGame() {
        gameEnded = false
        loadNextQuestion()
    }

    fun submitAnswer(player: User, answer: Address): Int {
        if (playerAnswered[player] == true) {
            throw Exception("player already answered")
        }

        val score = questions[currentQuestion].calculateScore(answer)
        playerScores[player] = (playerScores[player] ?: 0) + score
        playerAnswered[player] = true

        return score
    }

    fun loadNextQuestion() {
        currentQuestion++
        playerAnswered.replaceAll { _, _ -> false }

        if (currentQuestion >= questionNum ||
            currentQuestion >= questions.size
        ) {
            endGame()
            return
        }
    }

    fun endGame() {
        gameEnded = true
    }
}

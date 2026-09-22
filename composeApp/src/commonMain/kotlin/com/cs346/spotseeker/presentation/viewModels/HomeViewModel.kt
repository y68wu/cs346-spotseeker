package com.cs346.spotseeker.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

/**
 * ViewModel used by HomeScreen for entering/exiting a game.
 * Must implement ScreenModel so it can be created via rememberScreenModel.
 */
class HomeViewModel : ScreenModel {

    var inGame by mutableStateOf(false)
        private set

    fun startSinglePlayerGame() { inGame = true }
    fun start1V1Game()          { inGame = true }
    fun createGameRoom()        { inGame = true }

    /** Reset game-enter flag so returning to Home doesn't re-push GameScreen. */
    fun endGame() { inGame = false }
}

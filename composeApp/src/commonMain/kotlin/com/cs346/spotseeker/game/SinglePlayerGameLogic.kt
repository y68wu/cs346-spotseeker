package com.cs346.spotseeker.game

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

data class SinglePlayerRound(
    val roundIndex: Int,
    val totalRounds: Int,
    val photo: GamePhoto,
    val guessLat: Double? = null,
    val guessLng: Double? = null,
    val distanceMeters: Double? = null,
    val score: Int? = null,
    val revealed: Boolean = false
)

data class SinglePlayerGameState(
    val currentRound: SinglePlayerRound? = null,
    val totalScore: Int = 0,
    val isGameOver: Boolean = false,
    val usedPhotoIndices: List<Int> = emptyList()
)

fun haversineDistanceMeters(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double
): Double {
    val R = 6_371_000.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lng2 - lng1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLng / 2) * sin(dLng / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c
}

fun distanceToScore(
    distanceMeters: Double,
    maxDistanceMeters: Double = 5_000.0,
    maxScore: Int = 5_000
): Int {
    val clamped = distanceMeters.coerceIn(0.0, maxDistanceMeters)
    val ratio = 1.0 - clamped / maxDistanceMeters
    return (maxScore * ratio).roundToInt()
}

fun createInitialGameState(
    photos: List<GamePhoto>,
    totalRounds: Int = 5
): SinglePlayerGameState {
    require(photos.isNotEmpty()) { "Need at least one GamePhoto." }

    val rounds = min(totalRounds, photos.size)
    val firstIndex = 0

    return SinglePlayerGameState(
        currentRound = SinglePlayerRound(
            roundIndex = 1,
            totalRounds = rounds,
            photo = photos[firstIndex]
        ),
        totalScore = 0,
        isGameOver = false,
        usedPhotoIndices = listOf(firstIndex)
    )
}

fun applyGuess(
    state: SinglePlayerGameState,
    guessLat: Double,
    guessLng: Double
): SinglePlayerGameState {
    val current = state.currentRound ?: return state
    if (current.revealed) return state

    val realLat = current.photo.latitude
    val realLng = current.photo.longitude
    val distance = haversineDistanceMeters(realLat, realLng, guessLat, guessLng)
    val score = distanceToScore(distance)

    val updatedRound = current.copy(
        guessLat = guessLat,
        guessLng = guessLng,
        distanceMeters = distance,
        score = score,
        revealed = true
    )

    return state.copy(
        currentRound = updatedRound,
        totalScore = state.totalScore + score
    )
}

fun goToNextRound(
    state: SinglePlayerGameState,
    allPhotos: List<GamePhoto>
): SinglePlayerGameState {
    val current = state.currentRound ?: return state

    if (current.roundIndex >= current.totalRounds) {
        return state.copy(isGameOver = true)
    }

    val nextIndex = state.usedPhotoIndices.size
    if (nextIndex >= allPhotos.size) {
        return state.copy(isGameOver = true)
    }

    val nextRound = SinglePlayerRound(
        roundIndex = current.roundIndex + 1,
        totalRounds = current.totalRounds,
        photo = allPhotos[nextIndex]
    )

    return state.copy(
        currentRound = nextRound,
        usedPhotoIndices = state.usedPhotoIndices + nextIndex
    )
}

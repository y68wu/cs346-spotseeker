package com.cs346.spotseeker.domain

import kotlin.math.*

class GameGuessQuestion(val photo: Photo) {

    // 球面距离（米）
    private fun haversineMeters(
        lat1: Double, lng1: Double,
        lat2: Double, lng2: Double
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

    // 0–5000 分，超过 10km 才是 0 分
    fun calculateScore(answer: Address): Int {
        val realLat = photo.address.x
        val realLng = photo.address.y
        val guessLat = answer.x
        val guessLng = answer.y

        val distanceMeters = haversineMeters(realLat, realLng, guessLat, guessLng)

        val maxDistance = 10_000.0      // 10 km
        if (distanceMeters >= maxDistance) return 0

        val ratio = 1.0 - distanceMeters / maxDistance
        return (ratio * 5000).roundToInt()
    }
}

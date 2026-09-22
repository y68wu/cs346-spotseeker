package com.cs346.spotseeker.domain

// Photo that will be displayed to the user for them to guess

data class Photo(
    val photoPath: String,
    val address: Address,
    val id: String = generatePhotoId(),
    var good: Int = 0,
    var bad: Int = 0
) {
    val question: Boolean
        get() = good > 10 && (good - bad) > 5
    fun goodPhoto() {
        good++
    }
    fun badPhoto() {
        bad++
    }
}
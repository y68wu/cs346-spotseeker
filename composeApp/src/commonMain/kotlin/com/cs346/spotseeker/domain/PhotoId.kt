package com.cs346.spotseeker.domain

import kotlin.random.Random

/**
 * Generates a positive numeric identifier that fits into a signed 64-bit value so it can be
 * inserted into Supabase tables that expect a BIGINT primary key.
 */
fun generatePhotoId(): String {
    val candidate = Random.nextLong(1_000_000_000_000L, Long.MAX_VALUE)
    return candidate.toString()
}

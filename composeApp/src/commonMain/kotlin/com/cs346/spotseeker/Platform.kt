package com.cs346.spotseeker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
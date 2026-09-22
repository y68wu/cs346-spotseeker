package com.cs346.spotseeker.data

/**
 * Singleton holder for the PhotoRepository so that multiple parts of the app
 * (Vote screen, upload screen, etc.) all see the same in-memory data.
 */
object SharedPhotoRepository {
    val repo: PhotoRepository = PhotoRepository()
}


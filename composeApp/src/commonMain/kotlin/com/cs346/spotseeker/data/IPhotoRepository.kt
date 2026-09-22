package com.cs346.spotseeker.data

import com.cs346.spotseeker.domain.Photo
import kotlinx.coroutines.flow.Flow

interface IPhotoRepository : IRepository {
    fun getAllPhotos(): Flow<List<Photo>>

    // return a list of n photos that are with question field = true
    fun getNQuestionPhotos(n: Int): Flow<List<Photo>>

    suspend fun addPhoto(photo: Photo)

    suspend fun updatePhoto(photo: Photo)

    suspend fun getPhoto(id: String): Photo?

    suspend fun uploadPhoto(photo: Photo, data: ByteArray)
}



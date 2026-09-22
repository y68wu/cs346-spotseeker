package com.cs346.spotseeker.data

import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockPhotoRepository : IPhotoRepository {
    val photos = mutableMapOf<String, Photo>(
//        "id1" to Photo("photo1", Address(0.0, 0.0), "id1", 0, 0),
//        "id2" to Photo("photo2", Address(0.0, 0.0), "id2", 0, 0),
//        "id3" to Photo("photo3", Address(0.0, 0.0), "id3", 0, 0),
//        "id4" to Photo("photo4", Address(0.0, 0.0), "id4", 0, 0),
//        "id5" to Photo("photo5", Address(0.0, 0.0), "id5", 0, 0),
//        "id6" to Photo("photo6", Address(0.0, 0.0), "id6", 0, 0)
    )

    override fun getAllPhotos(): Flow<List<Photo>> {
        return flowOf(photos.values.toList())
    }

    override fun getNQuestionPhotos(n: Int): Flow<List<Photo>> {
        if (photos.isEmpty() || n <= 0) return flowOf(emptyList<Photo>())

        val photoValues = photos.values.toList()
        return flowOf(List(n) { index ->
            photoValues[index % photoValues.size]
        })
    }

    override suspend fun addPhoto(photo: Photo) {
        if (photo.id in photos.keys) {
            return
        }
        photos[photo.id] = photo
    }

    override suspend fun updatePhoto(photo: Photo) {
        photos[photo.id] = photo
    }

    override suspend fun getPhoto(id: String): Photo? {
        return photos[id]
    }

    override suspend fun uploadPhoto(photo: Photo, data: ByteArray){}
}
package com.cs346.spotseeker.domain

import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.data.IRepository
import com.cs346.spotseeker.data.PhotoRepository
import java.io.Serializable

class User(
    val userId: Int,
    val name: String,
    val history: FloatArray = floatArrayOf()
) : Serializable{
    val accuracy: Float
        get() = if (history.isNotEmpty()) history.average().toFloat() else 1f

    fun addPhoto(photoPath: String, address: Address, repo: PhotoRepository, id: String): Photo {
        val photo = Photo(photoPath, address, id)
        return repo.add(photo)
    }
    fun UgoodPhoto(photo: Photo) {
        photo.goodPhoto();
        return
    }
    fun UbadPhoto(photo: Photo) {
        photo.badPhoto();
        return
    }
}
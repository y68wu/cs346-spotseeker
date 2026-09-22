package com.cs346.spotseeker.domain

import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.data.MySharedPhotoRepository
import kotlinx.coroutines.flow.Flow

// Model handles application-level state for the voting functionality
class VoteModel(val photoRepository: IPhotoRepository) {
    // Return all photo in photoRepository in the voting stage
    fun getAllPhotoForVote(): Flow<List<Photo>> {
        val repository = photoRepository
        return if (repository is MySharedPhotoRepository) {
            repository.getAllPhotos()
        } else {
            repository.getAllPhotos()
        }
    }

    // add photo to photoRepository
    suspend fun addPhoto(photo: Photo) {
        val repository = photoRepository
        if (repository is MySharedPhotoRepository) {
            repository.addPhoto(photo)
        } else {
            repository.addPhoto(photo)
        }
    }

    suspend fun uploadPhoto(photo: Photo, data: ByteArray) {
        val repository = photoRepository
        if (repository is MySharedPhotoRepository) {
            val normalized = ensureNumericId(photo)
            repository.uploadPhoto(normalized, data)
        } else {
            repository.addPhoto(photo)
        }
    }

    suspend fun getPhotoById(id: String): Photo? {
        val repository = photoRepository
        return if (repository is MySharedPhotoRepository) {
            repository.getPhoto(id)
        } else {
            repository.getPhoto(id)
        }
    }

    suspend fun voteGoodById(id: String): Photo? {
        val repository = photoRepository
        if (repository is MySharedPhotoRepository) {
            return repository.voteGoodById(id)
        }

        val photo = repository.getPhoto(id) ?: return null
        val updated = photo.copy(good = photo.good + 1)
        repository.updatePhoto(updated)
        return updated
    }

    suspend fun voteBadById(id: String): Photo? {
        val repository = photoRepository
        if (repository is MySharedPhotoRepository) {
            return repository.voteBadById(id)
        }

        val photo = repository.getPhoto(id) ?: return null
        val updated = photo.copy(bad = photo.bad + 1)
        repository.updatePhoto(updated)
        return updated
    }

    private fun ensureNumericId(photo: Photo): Photo {
        if (photo.id.toLongOrNull() != null) {
            return photo
        }
        val generatedId = System.currentTimeMillis().toString()
        return photo.copy(id = generatedId)
    }
}
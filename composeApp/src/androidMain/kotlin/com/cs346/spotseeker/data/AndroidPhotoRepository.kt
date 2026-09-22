package com.cs346.spotseeker.data

import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.domain.PhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext

class AndroidPhotoRepository(val dao: PhotoDao) : IPhotoRepository {
    override fun getAllPhotos(): Flow<List<Photo>> {
        return dao.getAll()
            .map { entityList ->
                entityList.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override fun getNQuestionPhotos(n: Int): Flow<List<Photo>> {
        return dao.getAll().map { entityList ->
            // Shuffle the list randomly
            val shuffled = entityList.shuffled()
            // Take at most n items
            val selected = shuffled.take(n)
            println(selected.size)
            // Convert to domain objects
            selected.map { it.toDomain() }
        }
    }

    override suspend fun addPhoto(photo: Photo) {
        val id = photo.id
        val entity = PhotoEntity(
            id = id,
            photoPath = photo.photoPath,
            x = photo.address.x,
            y = photo.address.y,
            good = photo.good,
            bad = photo.bad
        )
        dao.insert(entity)
    }

    override suspend fun updatePhoto(photo: Photo) {
        val entity = PhotoEntity(
            id = photo.id,
            photoPath = photo.photoPath,
            x = photo.address.x,
            y = photo.address.y,
            good = photo.good,
            bad = photo.bad
        )
        dao.update(entity)
    }

    override suspend fun getPhoto(id: String): Photo? {
//        val entity = dao.findById(id) ?: return null
//        return entity.toDomain()
        return withContext(Dispatchers.IO) {
            dao.findById(id)?.toDomain()
        }
    }
    override suspend fun uploadPhoto(photo: Photo, data: ByteArray) {
        // TODO: Implement the logic to upload the photo data.
        // For example, you might upload it to Supabase Storage,
        // then get a URL and update the photo's photoPath.
        //
        // Since the logic is not yet defined, a placeholder is used.
        println("Uploading photo with id: ${photo.id}")
        // Awaiting implementation...
    }
}

private fun PhotoEntity.toDomain(): Photo {
    return Photo(photoPath, Address(x, y), id = id, good = good, bad = bad)
}
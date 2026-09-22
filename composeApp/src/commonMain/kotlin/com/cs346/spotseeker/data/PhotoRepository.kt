package com.cs346.spotseeker.data

import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.domain.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.DrawableResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.vote1
import spotseeker.composeapp.generated.resources.vote2
import spotseeker.composeapp.generated.resources.vote3
import spotseeker.composeapp.generated.resources.vote4
import spotseeker.composeapp.generated.resources.vote5
import spotseeker.composeapp.generated.resources.vote6

class PhotoRepository : IPhotoRepository {

     val store = mutableListOf(
        Photo("photo1", Address(43.4700427140495, -80.54305762733442), "id1", 0, 0),
        Photo("photo2", Address(43.4695866968057, -80.54170871082509), "id2", 0, 0),
        Photo("photo3", Address(43.46818562695775, -80.5435181121722), "id3", 0, 0),
        Photo("photo4", Address(43.46730745346701, -80.54645429302312), "id4", 0, 0),
        Photo("photo5", Address(43.46960293068699, -80.54669976567386), "id5", 0, 0),
        Photo("photo6", Address(43.4695167871849, -80.54767433437735), "id6", 0, 0)
    )

    private val photosDrawable: List<DrawableResource> = listOf(
        Res.drawable.vote1,
        Res.drawable.vote2,
        Res.drawable.vote3,
        Res.drawable.vote4,
        Res.drawable.vote5,
        Res.drawable.vote6
    )

    fun seed(n: Int) {
        store.clear()
        repeat(n) { i ->
            val lat = 43.47 + (i * 0.0005)
            val lng = -80.545 + (i * 0.0005)
            val p = Photo("photo_seed_$i", Address(lat, lng), id = "id$i", good = 0, bad = 0)
            store += p
        }
    }

    fun getAll(): List<Photo> = store.toList()

    fun getAllDrawable(): List<DrawableResource> = photosDrawable

    fun getNPhotos(n: Int): List<Photo> {
        val count = n.coerceAtMost(store.size)
        return store.take(count)
    }

    fun add(photo: Photo): Photo {
        store += photo
        return photo
    }

    fun voteGoodByIndex(index: Int): Photo? {
        if (index < 0 || index >= store.size) return null
        val p = store[index]
        p.goodPhoto()
        return p
    }

    fun voteBadByIndex(index: Int): Photo? {
        if (index < 0 || index >= store.size) return null
        val p = store[index]
        p.badPhoto()
        return p
    }

    fun voteGoodById(id: String): Photo? {
        val idx = store.indexOfFirst { it.id == id }
        if (idx < 0) return null
        val p = store[idx]
        p.goodPhoto()
        return p
    }

    fun voteBadById(id: String): Photo? {
        val idx = store.indexOfFirst { it.id == id }
        if (idx < 0) return null
        val p = store[idx]
        p.badPhoto()
        return p
    }

    override fun getAllPhotos(): Flow<List<Photo>> {
        return flowOf(store.toList())
    }

    override fun getNQuestionPhotos(n: Int): Flow<List<Photo>> {
        val count = n.coerceAtMost(store.size)
        val randomList = store.shuffled().take(count)
        return flowOf(randomList)
    }

    override suspend fun addPhoto(photo: Photo) {
        add(photo)
    }

    override suspend fun updatePhoto(photo: Photo) {
        val idx = store.indexOfFirst { it.id == photo.id }
        if (idx != -1) {
            store[idx] = photo
        }
    }

    override suspend fun getPhoto(id: String): Photo? {
        return store.firstOrNull { it.id == id }
    }

    override suspend fun uploadPhoto(photo: Photo, data: ByteArray){}
}

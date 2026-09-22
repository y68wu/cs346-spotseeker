package com.cs346.spotseeker.data

import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MockPhotoRepositoryTest {

    @Test
    fun testAddPhotoAndGetAllPhotos() = runTest {
        val repo = MockPhotoRepository()

        val photo1 = Photo(
            id = "1",
            address = Address(1.0, 2.0),
            photoPath = "path1",
        )
        val photo2 = Photo(
            id = "2",
            address = Address(1.0, 2.0),
            photoPath = "path2",
        )

        repo.addPhoto(photo1)
        repo.addPhoto(photo2)

        val all: Flow<List<Photo>> = repo.getAllPhotos()
        assertEquals(2, all.first().count())
//        assertEquals(setOf("1", "2"), all  { it.id }.toSet())
    }

    @Test
    fun testAddDuplicatePhotoIgnored() = runTest {
        val repo = MockPhotoRepository()

        val photo1 = Photo(
            id = "1",
            address = Address(1.0, 2.0),
            photoPath = "path1",
        )
        val duplicate = Photo(
            id = "1",
            address = Address(1.0, 2.0),
            photoPath = "path2",
        )

        repo.addPhoto(photo1)
        repo.addPhoto(duplicate)  // should not add

        val all = repo.getAllPhotos()
        assertEquals(1, all.first().count())
//        assertEquals("path1", all.first().photoPath)
    }

    @Test
    fun testUpdatePhoto() = runTest {
        val repo = MockPhotoRepository()

        val photo = Photo(id = "1", address = Address(1.0, 2.0), photoPath = "a.jpg")
        repo.addPhoto(photo)

        val updated = Photo(id = "1", address = Address(1.0, 2.0), photoPath = "updated.jpg")
        repo.updatePhoto(updated)

        val result = repo.getPhoto("1")
        assertNotNull(result)
        assertEquals("updated.jpg", result.photoPath)
    }

    @Test
    fun testGetPhotoById() = runTest {
        val repo = MockPhotoRepository()
        val photo = Photo(id = "5", photoPath = "image.jpg", address = Address(1.0, 2.0))

        repo.addPhoto(photo)

        assertNotNull(repo.getPhoto("5"))
        assertNull(repo.getPhoto("missing"))
    }

    @Test
    fun testGetNQuestionPhotosRepeatsIfNeeded() = runTest {
        val repo = MockPhotoRepository()

        val photo1 = Photo(id = "1", photoPath = "a.jpg", address = Address(1.0, 2.0))
        val photo2 = Photo(id = "2", photoPath = "b.jpg", address = Address(1.0, 2.0))
        repo.addPhoto(photo1)
        repo.addPhoto(photo2)

        val result = repo.getNQuestionPhotos(5)

        assertEquals(5, result.first().count())
    }

    @Test
    fun testGetNQuestionPhotosEmptyWhenNoPhotos() = runTest {
        val repo = MockPhotoRepository()

        val result = repo.getNQuestionPhotos(5)
        assertEquals(0, result.first().count())
    }
}

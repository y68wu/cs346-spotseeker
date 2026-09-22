package com.cs346.spotseeker

import com.cs346.spotseeker.data.PhotoRepository
import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.domain.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertContains

class UserTest {

    @Test
    fun addPhotoStoresInRepository() {
        val repo = PhotoRepository()
        val user = User(1, "alice")

        val returned = user.addPhoto("p1", Address(1.0, 2.0), repo, "id1")

        // returned should be the same object stored in repo
        assertContains(repo.store, returned)
        assertEquals("p1", returned.photoPath)
        assertEquals("id1", returned.id)
    }

    @Test
    fun UgoodPhotoIncrementsGood() {
        val user = User(2, "bob")
        val photo = Photo("p2",  id="id", address = Address(0.0, 0.0))

        assertEquals(0, photo.good)
        user.UgoodPhoto(photo)
        assertEquals(1, photo.good)
    }

    @Test
    fun UbadPhotoIncrementsBad() {
        val user = User(3, "carol")
        val photo = Photo("p3", id="id", address = Address(0.0, 0.0))

        assertEquals(0, photo.bad)
        user.UbadPhoto(photo)
        assertEquals(1, photo.bad)
    }

    @Test
    fun questionFlagFromUserActions() {
        val user = User(4, "dave")
        val photo = Photo(photoPath = "p4", id="id", address = Address(0.0, 0.0))

        // upvote 12 times, downvote 3 times
        repeat(12) { user.UgoodPhoto(photo) }
        repeat(3) { user.UbadPhoto(photo) }

        assertTrue(photo.question)
    }
}

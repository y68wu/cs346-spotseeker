package com.cs346.spotseeker.data

import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.domain.generatePhotoId
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Helper for turning a *local image file* into a persisted Photo
 * that the rest of the app (including VoteScreen) can use.
 *
 * This lives in jvmMain, so it is meant for the Desktop build.
 * It works with any IRepository implementation:
 *  - On Android: RealPhotoRepository (Room DB)
 *  - On Desktop: PhotoRepository (in-memory)
 */
class LocalPhotoStorage(
    // Kept for future use if repository wants to persist Photos directly.
    private val repository: IRepository
) {

    // Base folder where we copy user-uploaded images on Desktop
    private val photosDir: File = File(
        System.getProperty("user.home"),
        ".spotseeker/photos"
    ).apply {
        if (!exists()) {
            mkdirs()
        }
    }

    /**
     * Copy the user-selected image into the app's folder, create a Photo
     * object, and return it to the caller.
     *
     * @param originalFile image chosen by the user (e.g. from a file chooser)
     * @param address      the location associated with this photo
     * @return the Photo corresponding to the copied file
     *
     * Typical usage (from a screen):
     *   val storage = LocalPhotoStorage(repository)
     *   val newPhoto = storage.addLocalPhoto(selectedFile, address)
     */
    fun addLocalPhoto(
        originalFile: File,
        address: Address
    ): Photo {
        require(originalFile.exists()) {
            "File does not exist: ${originalFile.absolutePath}"
        }

        // Generate a stable ID for database + filename
        val id = generatePhotoId()
        val extension = originalFile.extension.ifBlank { "jpg" }

        // Copy into our app-specific directory
        val targetFile = File(photosDir, "$id.$extension")
        Files.copy(
            originalFile.toPath(),
            targetFile.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )

        // Create domain Photo pointing at the *copied* path
        val photo = Photo(
            photoPath = targetFile.absolutePath,
            address = address,
            id = id,
            good = 0,
            bad = 0
        )

        // For now, just return the Photo; repository persistence
        // happens elsewhere (e.g. in the ViewModel or use case).
        return photo
    }
}

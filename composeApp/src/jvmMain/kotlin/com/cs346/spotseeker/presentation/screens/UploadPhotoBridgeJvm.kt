package com.cs346.spotseeker.presentation.screens

import com.cs346.spotseeker.data.IRepository
import com.cs346.spotseeker.data.LocalPhotoStorage
import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.presentation.viewModels.VoteViewModel
import java.nio.file.Files
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * Desktop implementation of pickAndStoreLocalPhoto:
 *  - opens a Swing file chooser
 *  - copies the selected image into the app's folder
 *  - adds a Photo to the provided repository using LocalPhotoStorage
 */
actual fun pickAndStoreLocalPhoto(address: Address, repository: IRepository): PickedPhoto? {
    val chooser = JFileChooser().apply {
        fileFilter = FileNameExtensionFilter(
            "Image files",
            "jpg", "jpeg", "png", "bmp", "gif"
        )
    }

    val result = chooser.showOpenDialog(null)
    if (result != JFileChooser.APPROVE_OPTION) {
        // user cancelled or closed the dialog
        return null
    }

    val selectedFile: File = chooser.selectedFile ?: return null

    val address = address

    val storage = LocalPhotoStorage(repository)
    val photo = storage.addLocalPhoto(selectedFile, address)
    val copiedBytes = Files.readAllBytes(File(photo.photoPath).toPath())
    return PickedPhoto(photo, copiedBytes)
}

actual fun launchAndroidPhotoPicker(
    address: Address,
    viewModel: VoteViewModel,
    onReturn: () -> Unit
) {
    error("Android photo picker is not available on JVM targets")
}

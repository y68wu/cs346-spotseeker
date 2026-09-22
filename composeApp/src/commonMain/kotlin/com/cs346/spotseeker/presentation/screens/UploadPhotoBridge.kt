package com.cs346.spotseeker.presentation.screens

import com.cs346.spotseeker.data.IRepository
import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.presentation.viewModels.VoteViewModel

data class PickedPhoto(
    val photo: Photo,
    val bytes: ByteArray?
)

expect fun pickAndStoreLocalPhoto(
    address: Address,
    repository: IRepository
): PickedPhoto?

expect fun launchAndroidPhotoPicker(
    address: Address,
    viewModel: VoteViewModel,
    onReturn: () -> Unit
)

package com.cs346.spotseeker.presentation.screens

import com.cs346.spotseeker.MainActivity
import com.cs346.spotseeker.data.IRepository
import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.presentation.viewModels.VoteViewModel

actual fun pickAndStoreLocalPhoto(
    address: Address,
    repository: IRepository
): PickedPhoto? {
    // Desktop-only helper; Android uses launchAndroidPhotoPicker instead.
    return null
}

actual fun launchAndroidPhotoPicker(
    address: Address,
    viewModel: VoteViewModel,
    onReturn: () -> Unit
) {
    MainActivity.current()?.launchPhotoPicker(
        address = address,
        viewModel = viewModel,
        onReturn = onReturn
    )
}

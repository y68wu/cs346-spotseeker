package com.cs346.spotseeker.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.spotseeker.data.IPhotoRepository
// AppViewModel(val photoRepository: IPhotoRepository = PhotoRepository()) : ViewModel()
open class AppViewModel(val photoRepository: IPhotoRepository) : ViewModel() {
    var selectedScreenIndex by mutableStateOf(0)

    fun selectScreen(index: Int) {
        selectedScreenIndex = index
    }
}
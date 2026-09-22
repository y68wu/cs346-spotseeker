package com.cs346.spotseeker.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotId
import androidx.compose.runtime.snapshots.SnapshotStateMap
import cafe.adriel.voyager.core.model.ScreenModel
import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.domain.Vote
import com.cs346.spotseeker.domain.VoteModel
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.DrawableResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.vote1
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.screenModelScope
import com.sun.jndi.toolkit.url.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import spotseeker.composeapp.generated.resources.vote2

/**
 * ViewModel that exposes vote-related operations for the UI.
 *
 * Responsibilities:
 * - Seed a fake in-memory photo repository
 * - Expose an observable map of votes by photo index
 * - Provide voteGood/voteBad operations which update repository and the observable state
 */
open class VoteViewModel(
//    private val repository: PhotoRepository = PhotoRepository() // sync in-memory repository
    private val model: VoteModel,
    // later could be replaced with async repository for real backend
    // if need more logic for CRUD operations, we won't directly expose the repository
) : ScreenModel {
    var enlargedId: String? by mutableStateOf(null)

    val repository: IPhotoRepository
        get() = model.photoRepository


    // Observable map so Composables can read/write and observe changes
    // private _votes make sure votes is read only to other classes (e.g. Views) using it
    private val _votes: SnapshotStateMap<String, Vote> = mutableStateMapOf()
    val votes: SnapshotStateMap<String, Vote> = _votes

    fun enlargeImage(imgId: String) {
        enlargedId = imgId
    }

    fun closeEnlargedImage() {
        enlargedId = null
    }

    // return drawable resource of a given photo
    fun getPhotoDrawable(photo: Photo?): DrawableResource {
        if (photo == null) {
            return Res.drawable.vote2
        }
        return Res.drawable.vote1
    }

    fun getPhoto(id: String): Photo? {
        return runBlocking { model.getPhotoById(id) }
    }

    fun addPhoto(photo: Photo) {
        screenModelScope.launch {
            model.addPhoto(photo)
        }
    }

    fun uploadPhoto(photo: Photo, data: ByteArray) {
        screenModelScope.launch {
            model.uploadPhoto(photo, data)
        }
    }

    fun getAllPhotosForVote(): Flow<List<Photo>> {
        return model.getAllPhotoForVote()
    }

    fun voteGoodById(id: String) {
        _votes[id] = Vote.Good
        screenModelScope.launch {
            model.voteGoodById(id)
        }
    }

    fun voteBadById(id: String) {
        _votes[id] = Vote.Bad
        screenModelScope.launch {
            model.voteBadById(id)
        }
    }
}

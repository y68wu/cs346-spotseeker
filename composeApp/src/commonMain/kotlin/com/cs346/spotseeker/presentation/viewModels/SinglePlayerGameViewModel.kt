package com.cs346.spotseeker.presentation.viewModels

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.data.ServiceLocator
import com.cs346.spotseeker.domain.Game
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.vote1
import spotseeker.composeapp.generated.resources.vote2
import spotseeker.composeapp.generated.resources.vote3
import spotseeker.composeapp.generated.resources.vote4
import spotseeker.composeapp.generated.resources.vote5
import spotseeker.composeapp.generated.resources.vote6

class SinglePlayerGameViewModel(
    user: User,
    questionNum: Int
) : ScreenModel {
    private val photoRepo = ServiceLocator.repository
    private val _game = MutableStateFlow<Game?>(null)
    val game = _game

    private val _user = MutableStateFlow(user)
    val user = _user

    // 本地 6 张测试照片
//    private val localRepo = PhotoRepository()

    init {
        startGame(
            user = user,
            questionNum = questionNum,
        )
    }

    fun startGame(user: User, questionNum: Int) {
        screenModelScope.launch {
            // 从本地 repo 拿 6 张，再随机抽 questionNum 张
            val photos = photoRepo.getNQuestionPhotos(questionNum).first()

            val game = Game(
                listOf(user),
                questionNum = questionNum,
                questionPhotos = photos,
                )
            _game.value = game
        }
    }

    fun getPhotoDrawable(photo: Photo?): DrawableResource {
        if (photo == null) {
            return Res.drawable.vote2
        }
        return Res.drawable.vote1
    }
}

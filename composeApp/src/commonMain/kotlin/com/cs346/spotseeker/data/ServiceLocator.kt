package com.cs346.spotseeker.data

import com.cs346.spotseeker.domain.VoteModel
import com.cs346.spotseeker.presentation.viewModels.VoteViewModel

object ServiceLocator {
    lateinit var repository: IPhotoRepository
    lateinit var voteViewModel: VoteViewModel
}
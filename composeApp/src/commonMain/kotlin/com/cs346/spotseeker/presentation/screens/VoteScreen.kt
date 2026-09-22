package com.cs346.spotseeker.presentation.screens

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.good
import spotseeker.composeapp.generated.resources.bad
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.data.ServiceLocator
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.domain.Vote
import com.cs346.spotseeker.domain.VoteModel
import com.cs346.spotseeker.presentation.viewModels.VoteViewModel
import com.cs346.spotseeker.presentation.components.PhotoImage

class VoteScreen() : Screen {
    @Composable
    override fun Content() {
        val bottomBarPadding = 88.dp

        val voteViewModel = ServiceLocator.voteViewModel

        Box(modifier = Modifier.fillMaxSize()) {
            VoteGrid(
                voteViewModel = voteViewModel,
                goodRes = Res.drawable.good,
                badRes = Res.drawable.bad,
                bottomBarPadding = bottomBarPadding
            )

            val navigator = LocalNavigator.currentOrThrow
            // button to upload photo
            Button(
                onClick = {
                    navigator.push(
                        UploadScreen()
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = bottomBarPadding + 16.dp)
            ) {
                Text("Upload")
            }
        }
    }
}

@Composable
private fun VoteGrid(
    voteViewModel: VoteViewModel,
    goodRes: DrawableResource,
    badRes: DrawableResource,
    bottomBarPadding: androidx.compose.ui.unit.Dp
) {
//    val photosRes = voteViewModel.getAllVotePhotoDrawables()
    val photos by voteViewModel.getAllPhotosForVote().collectAsState(initial = emptyList())

    val votes = voteViewModel.votes

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 12.dp, top = 12.dp, end = 12.dp, bottom = bottomBarPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(items = photos, key = { i, _ -> i }) { index, p ->
            PhotoCard(
                photo = p,
                id = p.id,
                res = voteViewModel.getPhotoDrawable(p),
                current = votes[p.id],
                goodRes = goodRes,
                badRes = badRes,
                enlarged = false,
                onImageClick = { voteViewModel.enlargeImage(p.id) },
                onVote = { v ->
                    if (v == Vote.Good) {
                        voteViewModel.voteGoodById(p.id)
                    }
                    else if (v == Vote.Bad) {
                        voteViewModel.voteBadById(p.id)
                    }
                }
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(8.dp)) }
    }

    if (voteViewModel.enlargedId != null) {
        val photoId: String = voteViewModel.enlargedId!!
        val photo = voteViewModel.getPhoto(photoId)
        Dialog(
            onDismissRequest = { voteViewModel.enlargedId = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(Modifier.fillMaxSize()) {
                PhotoCard(
                    photo = voteViewModel.getPhoto(photoId),
                    id = photoId,
                    res = voteViewModel.getPhotoDrawable(photo),
                    current = votes[photoId],
                    goodRes = goodRes,
                    badRes = badRes,
                    enlarged = true,
                    onImageClick = { voteViewModel.closeEnlargedImage() },
                    onVote = { v ->
                        if (v == Vote.Good) {
                            voteViewModel.voteGoodById(photoId)
                        }
                        else if (v == Vote.Bad) {
                            voteViewModel.voteBadById(photoId)
                        }
                        voteViewModel.closeEnlargedImage()
                    }
                )
            }
        }
    }
}

@Composable
private fun PhotoCard(
    photo: Photo?,
    id: String,
    res: DrawableResource,
    current: Vote?,
    goodRes: DrawableResource,
    badRes: DrawableResource,
    enlarged: Boolean,
    onImageClick: () -> Unit,
    onVote: (Vote) -> Unit
) {
    val iconSize = if (enlarged) 48.dp else 32.dp
    val radius = if (enlarged) 16.dp else 12.dp
    val ratio = if (enlarged) 16f / 9f else 1f
    val outerPad = if (enlarged) 10.dp else 6.dp
    val between = if (enlarged) 12.dp else 8.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(outerPad),
        verticalArrangement = Arrangement.spacedBy(between),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhotoImage(
            photo = photo,
            placeholder = res,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)
                .clip(RoundedCornerShape(radius))
                .clickable { onImageClick() },
            contentScale = ContentScale.Crop
        )
        Text("good ${photo?.good}, bad ${photo?.bad}")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (current) {
                null -> {
                    Image(
                        painter = painterResource(goodRes),
                        contentDescription = "good",
                        modifier = Modifier.size(iconSize).clickable {
                            onVote(Vote.Good)
//                            voteViewModel.voteGoodByIndex(idx)
//                            goodCount = voteViewModel.getPhoto(idx)?.good
                        }
                    )
                    Image(
                        painter = painterResource(badRes),
                        contentDescription = "bad",
                        modifier = Modifier.size(iconSize).clickable {
                            onVote(Vote.Bad)
//                            voteViewModel.voteBadByIndex(idx)
//                            badCount = voteViewModel.getPhoto(idx)?.bad
                        }
                    )
                }
                Vote.Good -> {
                    print("good ${id}\n")
                    Image(
                        painter = painterResource(goodRes),
                        contentDescription = "good",
                        modifier = Modifier.size(iconSize)
                    )
                }
                Vote.Bad -> {
                    print("bad ${id}\n")
                    Image(
                        painter = painterResource(badRes),
                        contentDescription = "bad",
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}

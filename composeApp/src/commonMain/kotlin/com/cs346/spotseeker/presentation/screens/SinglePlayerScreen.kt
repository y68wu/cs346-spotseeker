package com.cs346.spotseeker.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Game
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.domain.User
import com.cs346.spotseeker.presentation.components.GameMap
import com.cs346.spotseeker.presentation.viewModels.SinglePlayerGameViewModel
import com.cs346.spotseeker.presentation.components.GameMap
import com.cs346.spotseeker.utils.openMapAt
import org.jetbrains.compose.resources.DrawableResource
import com.cs346.spotseeker.presentation.components.PhotoImage
import org.jetbrains.compose.resources.painterResource

class SinglePlayerScreen(val user: User,  val questionNum: Int) : Screen {
    @Composable
    override fun Content() {
        val singlePlayerGameViewModel = rememberScreenModel { SinglePlayerGameViewModel(user, questionNum) }
        val game by singlePlayerGameViewModel.game.collectAsState()
        val user by singlePlayerGameViewModel.user.collectAsState()

        game?.let { nonNullGame ->
            SinglePlayerScreenContent(
                game = nonNullGame,
                user = user,
                getPhotoDrawable = { photo ->
                    singlePlayerGameViewModel.getPhotoDrawable(photo = photo)
                }
            )
        } ?: run {
            Text("Loading game...")
        }
    }
}

@Composable
fun SinglePlayerScreenContent(
    game: Game,
    user: User,
    getPhotoDrawable: (Photo?) -> DrawableResource,
) {
    val navigator = LocalNavigator.currentOrThrow
    val questions = game.questions

    var answered by remember { mutableStateOf(false) }
    var previousScore by remember { mutableIntStateOf(0) }
    var pendingGuess by remember { mutableStateOf<Address?>(null) }

    Scaffold(
        topBar = {
            SinglePlayerTopBar(
                onBack = { navigator.pop() }
            )
        }
    ) { paddingValues ->

        if (game.gameEnded) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Game Over")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Final score: ${game.playerScores[user]?.toInt() ?: 0}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navigator.pop() }) {
                        Text("Back to Home")
                    }
                }
            }
            return@Scaffold
        }

        if (game.currentQuestion < 0 || game.currentQuestion >= questions.size) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No round available.")
            }
        } else {
            val currentQuestion = questions[game.currentQuestion]
            val realAddress = currentQuestion.photo.address

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())   // 允许整体滚动
            ) {
                // ===== 文本信息 =====
                Text(
                    text = "Round ${game.currentQuestion + 1} / ${game.questionNum}",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
//                Text(
//                    text = "Question ID: ${currentQuestion.photo.id}",
//                    modifier = Modifier.padding(8.dp)
//                )
                Text(
                    text = "Total score: ${game.playerScores[user]?.toInt() ?: 0}",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ===== 图片：固定高度，防止撑满全屏 =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PhotoImage(
                        photo = currentQuestion.photo,
                        placeholder = getPhotoDrawable(currentQuestion.photo),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ===== 地图 + 文本 + 按钮 =====
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    GameMap(
                        lat = realAddress.x,
                        lng = realAddress.y,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp),      // Desktop/Android 都用一个合理高度
                        guessLat = pendingGuess?.x,
                        guessLng = pendingGuess?.y,
                        revealed = answered,
                        onMapClick = { lat, lng ->
                            if (!answered) {
                                // 这里只记录“待确认”的猜测
                                pendingGuess = Address(lat, lng)
                                answered = false
                                previousScore = 0
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!answered) {
                        Text(
                            text = if (pendingGuess == null) {
                                "Click on the map to choose a location, then confirm your guess."
                            } else {
                                "Press Confirm guess to submit."
                            },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    } else {
                        Text(
                            text = "This round: $previousScore pts",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                val guess = pendingGuess
                                if (guess != null && !answered) {
                                    val scoreDouble = game.submitAnswer(user, guess)
                                    previousScore = scoreDouble.toInt()
                                    answered = true
                                }
                            },
                            enabled = pendingGuess != null && !answered
                        ) {
                            Text("Confirm guess")
                        }

                        Button(
                            onClick = {
                                game.loadNextQuestion()
                                answered = false
                                previousScore = 0
                                pendingGuess = null
                            },
                            enabled = answered
                        ) {
                            val label =
                                if (game.currentQuestion + 1 >= game.questionNum)
                                    "Finish Game"
                                else
                                    "Next"
                            Text(label)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SinglePlayerTopBar(
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Single Player")
    }
}

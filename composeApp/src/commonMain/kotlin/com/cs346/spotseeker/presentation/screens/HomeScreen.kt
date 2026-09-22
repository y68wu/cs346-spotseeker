package com.cs346.spotseeker.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.data.MockPhotoRepository
import com.cs346.spotseeker.data.PhotoRepository
import com.cs346.spotseeker.data.ServiceLocator
import com.cs346.spotseeker.domain.User
import org.jetbrains.compose.resources.painterResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.background
import com.cs346.spotseeker.presentation.viewModels.HomeViewModel
import com.cs346.spotseeker.presentation.viewModels.SinglePlayerGameViewModel

class HomeScreen() : Screen {

    @Composable
    override fun Content() {
        val homeViewModel = rememberScreenModel { HomeViewModel() }
        val navigator = LocalNavigator.currentOrThrow

        val photoRepository = ServiceLocator.repository

        // 保留原有 inGame 逻辑
        LaunchedEffect(homeViewModel.inGame) {
            if (homeViewModel.inGame) {
                homeViewModel.endGame()
                navigator.push(GameScreen(photoRepository))
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Background photo
            Image(
                painter = painterResource(Res.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xAA0B1530),
                                Color(0x660B1530),
                                Color(0x550B2E24)
                            )
                        )
                    )
            )

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val buttonWidth = maxWidth * 0.5f
                val buttonHeight = maxHeight * 0.10f

                ButtonsColumn(
                    buttonWidth = buttonWidth,
                    buttonHeight = buttonHeight,
                    onCreateRoomClick = {
                        navigator.push(CreateRoomScreen())
                    },
                    photoRepository = photoRepository
                )
            }
        }
    }
}

@Composable
private fun ButtonsColumn(
    photoRepository: IPhotoRepository,
    buttonWidth: Dp,
    buttonHeight: Dp,
    onCreateRoomClick: () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 单人模式
        Button(
            onClick = { navigator.push(SinglePlayerScreen(
                user = User(userId = 0, name = "username"),
                questionNum = 5)
            ) },
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF516698))
        ) {
            Text("Singleplayer", fontSize = 20.sp, color = Color.White)
        }

        // Explore Waterloo：直接进入地图页面（跳过 Match Ready 页面）
        Button(
            onClick = { navigator.push(ExploreWaterlooScreen()) },
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF516698))
        ) {
            Text("Explore Waterloo", fontSize = 20.sp, color = Color.White)
        }

        Button(
            onClick = onCreateRoomClick,
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF516698))
        ) {
            Text("Create Room", fontSize = 20.sp, color = Color.White)
        }
    }
}

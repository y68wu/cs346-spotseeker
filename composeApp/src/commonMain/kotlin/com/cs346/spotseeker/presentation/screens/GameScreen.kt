package com.cs346.spotseeker.presentation.screens

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.domain.Photo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.exit_icon
import spotseeker.composeapp.generated.resources.start_button_bg
import spotseeker.composeapp.generated.resources.avatar_blue
import spotseeker.composeapp.generated.resources.avatar_red
import spotseeker.composeapp.generated.resources.timer_badge
import spotseeker.composeapp.generated.resources.game_bg
import spotseeker.composeapp.generated.resources.place_guess
import spotseeker.composeapp.generated.resources.background   // 旧的静态地图图片

// ⭐ 新增：引入我们刚写好的跨平台 GameMap 组件
import com.cs346.spotseeker.presentation.components.GameMap
import com.cs346.spotseeker.presentation.components.PhotoImage

class GameScreen(private val photoRepository: IPhotoRepository) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var started by remember { mutableStateOf(false) }
        var seconds by remember { mutableStateOf(180) } // 3:00

        val currentPhoto by produceState<Photo?>(initialValue = null, photoRepository) {
            photoRepository.getAllPhotos().collect { photos ->
                value = photos.firstOrNull()
            }
        }

        // countdown when started
        LaunchedEffect(started) {
            if (started) {
                while (seconds > 0) {
                    delay(1000)
                    seconds -= 1
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // background
            Image(
                painter = painterResource(Res.drawable.game_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xAA0D1B3A),
                                Color(0x660D1B3A),
                                Color(0x5523453A)
                            )
                        )
                    )
            )

            // top bar with Exit
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { navigator.pop() }
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.exit_icon),
                        contentDescription = "Exit",
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        "Exit",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                if (started) {
                    TimeBadge(seconds = seconds)
                }
            }

            if (!started) {
                Lobby(onExit = { navigator.pop() }, onStart = { started = true })
            } else {
                InGame(
                    seconds = seconds,
                    onExit = { navigator.pop() },
                    photo = currentPhoto
                )
            }
        }
    }
}

@Composable
private fun Lobby(onExit: () -> Unit, onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Match Ready",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        // players panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x33000000))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerCard(name = "You", rating = 1420, avatarRes = Res.drawable.avatar_blue)
            Text("vs", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
            PlayerCard(name = "Opponent", rating = 1450, avatarRes = Res.drawable.avatar_red, status = "waiting…")
        }

        // start button
        Image(
            painter = painterResource(Res.drawable.start_button_bg),
            contentDescription = "Start",
            modifier = Modifier
                .width(320.dp)
                .clickable { onStart() },
            contentScale = ContentScale.FillWidth
        )

        // small hint
        Text(
            text = "Tip: you’ll get more points the closer your pin is to the true spot.",
            color = Color(0xFFE6F6FF),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun PlayerCard(
    name: String,
    rating: Int,
    avatarRes: DrawableResource,
    status: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(avatarRes),
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(28.dp)),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text("ELO $rating", color = Color(0xFFBFD7FF), fontSize = 13.sp)
            if (status != null) {
                Text(status, color = Color(0xFFE6F6FF), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun TimeBadge(seconds: Int) {
    val min = seconds / 60
    val sec = seconds % 60
    val t = "%d:%02d".format(min, sec)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0x66000000))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(t, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InGame(seconds: Int, onExit: () -> Unit, photo: Photo?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 60.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose the correct location",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Opponent: answering…",
                color = Color(0xFFDBEAFE),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(12.dp))

            // Top photo
            PhotoImage(
                photo = photo,
                placeholder = Res.drawable.place_guess,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Bottom: 真正的跨平台 GameMap
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                val targetLat = photo?.address?.x ?: 43.4723
                val targetLng = photo?.address?.y ?: -80.5449
                GameMap(
                    lat = targetLat,
                    lng = targetLng,
                    modifier = Modifier.fillMaxSize(),
                    guessLat = null,
                    guessLng = null,
                    revealed = false,
                    onMapClick = { _, _ -> /* no-op */ }
                )
            }

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = { /* TODO: handle guess */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3E5176),
                    contentColor = Color.White
                )
            ) {
                Text("Place Guess", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

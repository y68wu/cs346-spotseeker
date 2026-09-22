package com.cs346.spotseeker.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.compose.foundation.layout.RowScope
import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.data.MockPhotoRepository
import com.cs346.spotseeker.domain.User
import com.cs346.spotseeker.presentation.viewModels.SinglePlayerGameViewModel
import org.jetbrains.compose.resources.painterResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.room_bg
import spotseeker.composeapp.generated.resources.backicon
import spotseeker.composeapp.generated.resources.code_badge
import spotseeker.composeapp.generated.resources.copy_icon
import spotseeker.composeapp.generated.resources.join_button_bg

class CreateRoomScreen() : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var roomName by remember { mutableStateOf("") }
        var rounds by remember { mutableStateOf(5) }
        var is1v1 by remember { mutableStateOf(true) }
        var joinCode by remember { mutableStateOf("ABCD-123") }
        var showNameError by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {

            // --- Full screen background image ---
            Image(
                painter = painterResource(Res.drawable.room_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF020617),
                                Color(0xFF020617),
                                Color(0xFF020617)
                            )
                        )
                    )
            )

            // --- Back icon: return to main menu (HomeScreen) ---
            Image(
                painter = painterResource(Res.drawable.backicon),
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { navigator.pop() },
                contentScale = ContentScale.Fit
            )

            // --- Main card content ---
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF020617).copy(alpha = 0.96f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Title
                    Text(
                        text = "Create Room",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Text(
                        text = "Set a room name, choose a mode, and share the invite code with your friend.",
                        fontSize = 13.sp,
                        color = Color(0xFF9CA3AF),
                        textAlign = TextAlign.Center
                    )

                    // --- Room name field ---
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Room name",
                            fontSize = 13.sp,
                            color = Color(0xFF9CA3AF)
                        )
                        TextField(
                            value = roomName,
                            onValueChange = {
                                roomName = it
                                if (showNameError && it.isNotBlank()) {
                                    showNameError = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        if (showNameError) {
                            Text(
                                text = "Please enter a room name.",
                                fontSize = 12.sp,
                                color = Color(0xFFF97373)
                            )
                        }
                    }

                    // --- Game mode selector ---
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Game mode",
                            fontSize = 13.sp,
                            color = Color(0xFF9CA3AF)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ModeChip(
                                label = "1V1",
                                selected = is1v1,
                                onClick = { is1v1 = true },
                                modifier = Modifier.weight(1f)
                            )
                            ModeChip(
                                label = "Solo practice",
                                selected = !is1v1,
                                onClick = { is1v1 = false },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // --- Rounds selector ---
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Rounds",
                            fontSize = 13.sp,
                            color = Color(0xFF9CA3AF)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "How many photos per game?",
                                fontSize = 13.sp,
                                color = Color(0xFFCBD5F5)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                SmallCircleButton(onClick = {
                                    if (rounds > 3) rounds--
                                }) {
                                    Text("–", color = Color.White, fontSize = 18.sp)
                                }
                                Text(
                                    text = rounds.toString(),
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                                SmallCircleButton(onClick = {
                                    if (rounds < 10) rounds++
                                }) {
                                    Text("+", color = Color.White, fontSize = 18.sp)
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF111827))

                    // --- Invite code row ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.code_badge),
                            contentDescription = "Invite badge",
                            modifier = Modifier.size(40.dp),
                            contentScale = ContentScale.Fit
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Invite code",
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                            Text(
                                text = joinCode,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFBFDBFE)
                            )
                        }

                        Image(
                            painter = painterResource(Res.drawable.copy_icon),
                            contentDescription = "Copy code",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    // TODO: hook clipboard here (platform-specific)
                                },
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- Create room button ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .clickable {
                                if (roomName.isBlank()) {
                                    showNameError = true
                                } else {
                                    // ✅ 现在创建成功后，直接进入 SinglePlayerScreen（单人模式）
                                    navigator.push(SinglePlayerScreen(
                                        user = User(userId = 0, name = "username"),
                                        questionNum = rounds))
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.join_button_bg),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Create room & share",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// ---------- Reusable components ----------

@Composable
private fun ModeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (selected) Color(0xFF020617) else Color(0xFF020617)
    val border = if (selected) Color(0xFF60A5FA) else Color(0xFF4B5563)

    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = background,
        border = BorderStroke(1.dp, border)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun SmallCircleButton(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .size(32.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF111827)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

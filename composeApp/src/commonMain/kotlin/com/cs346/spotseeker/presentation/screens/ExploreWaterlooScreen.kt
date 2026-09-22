package com.cs346.spotseeker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cs346.spotseeker.presentation.components.GameMap

class ExploreWaterlooScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF020617))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 顶部：返回 HomeScreen 的按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navigator.pop() }) {
                        Text(
                            text = "‹",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Home",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Text(
                    text = "Explore Waterloo",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

                // ✅ 只显示真正的 Google Map（GameMap），没有多余内容
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black, shape = RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    GameMap(
                        lat = 43.4723,   // UW 大致坐标，可以以后换成别的
                        lng = -80.5449,
                        modifier = Modifier.fillMaxSize(),
                        guessLat = null,
                        guessLng = null,
                        revealed = false,
                        onMapClick = { _, _ -> /* no-op */ }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

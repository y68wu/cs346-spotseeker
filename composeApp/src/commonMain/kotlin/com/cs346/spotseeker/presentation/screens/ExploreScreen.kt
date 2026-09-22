package com.cs346.spotseeker.presentation.screens

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.explore_bg
import spotseeker.composeapp.generated.resources.google_maps_badge
import spotseeker.composeapp.generated.resources.icon_info
import spotseeker.composeapp.generated.resources.icon_rules
import spotseeker.composeapp.generated.resources.icon_tips

class ExploreScreen : Screen {
    @Composable
    override fun Content() {
        val bottomBarPadding = 88.dp

        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            Image(
                painter = painterResource(Res.drawable.explore_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient scrim so text pops
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xAA0E1B4D),
                                Color(0x660E1B4D),
                                Color(0x550E4D2E)
                            )
                        )
                    )
            )

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp, bottom = bottomBarPadding + 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Explore SpotSeeker",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 6.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "A location-guessing game across the University of Waterloo.",
                    color = Color(0xFFE6F6FF),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                InfoCard(
                    iconRes = Res.drawable.icon_info,
                    title = "How it Works",
                    bullets = listOf(
                        "You’ll see a campus photo. Guess the spot on the map.",
                        "Place your pin—closer pins score more points.",
                        "Play Solo or 1v1. Create a room to challenge friends!"
                    ),
                    startColor = Color(0xFF7C8FBA),
                    endColor = Color(0xFF5AB7A3)
                )

                Spacer(Modifier.height(12.dp))

                InfoCard(
                    iconRes = Res.drawable.icon_rules,
                    title = "Rules & Guidelines",
                    bullets = listOf(
                        "Photos must be from the University of Waterloo region.",
                        "Keep uploads real and appropriate—no offensive content.",
                        "Don’t reveal exact addresses or personal data in photos."
                    ),
                    startColor = Color(0xFF7DBA7C),
                    endColor = Color(0xFF58A6FF)
                )

                Spacer(Modifier.height(12.dp))

                InfoCard(
                    iconRes = Res.drawable.icon_tips,
                    title = "Tips to Win",
                    bullets = listOf(
                        "Look for landmarks: bridges, sculptures, building shapes.",
                        "Street patterns help—curves, intersections, trail paths.",
                        "Check shadows and signs for subtle location hints."
                    ),
                    startColor = Color(0xFFFFB86B),
                    endColor = Color(0xFFFF6DAA)
                )

                Spacer(Modifier.height(18.dp))

                // Powered by Google Maps
                Surface(
                    color = Color.White.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.google_maps_badge),
                            contentDescription = "Powered by Google Maps",
                            modifier = Modifier
                                .height(22.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Map & place data provided by Google Maps.",
                            color = Color(0xFF1F2937),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    iconRes: DrawableResource,
    title: String,
    bullets: List<String>,
    startColor: Color,
    endColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        // Full-width gradient panel (clip BEFORE background)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            startColor.copy(alpha = 0.92f),
                            endColor.copy(alpha = 0.92f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 2.dp, top = 4.dp, end = 2.dp, bottom = 2.dp)
                ) {
                    bullets.forEach { b ->
                        Row {
                            Text(text = "•  ", color = Color.White, fontSize = 16.sp)
                            Text(text = b, color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

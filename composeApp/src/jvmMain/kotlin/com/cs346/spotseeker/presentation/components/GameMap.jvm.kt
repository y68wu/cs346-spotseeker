package com.cs346.spotseeker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import org.jetbrains.compose.resources.painterResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.desktopmap

// 你给的地图边界：两角的经纬度
// (大致是 NW 和 SE 两个角，我这边整理成 min/max)
private const val MIN_LAT = 43.46407383638987   // 南边
private const val MAX_LAT = 43.47587125310274   // 北边
private const val MIN_LNG = -80.56598295491077  // 西边（更负）
private const val MAX_LNG = -80.53182747207467  // 东边

@Composable
actual fun GameMap(
    lat: Double,              // 正确答案的纬度
    lng: Double,              // 正确答案的经度
    modifier: Modifier,
    guessLat: Double?,
    guessLng: Double?,
    revealed: Boolean,
    onMapClick: (Double, Double) -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    if (w <= 0f || h <= 0f) return@detectTapGestures

                    val xRatio = (offset.x / w).coerceIn(0f, 1f)
                    val yRatio = (offset.y / h).coerceIn(0f, 1f)

                    // 反算出经纬度（和画点时的公式是反过来的）
                    val clickLng =
                        MIN_LNG + (MAX_LNG - MIN_LNG) * xRatio
                    val clickLat =
                        MAX_LAT - (MAX_LAT - MIN_LAT) * yRatio

                    onMapClick(clickLat, clickLng)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // 底图：整张 UW static map
        Image(
            painter = painterResource(Res.drawable.desktopmap),
            contentDescription = "Desktop map",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 在地图上画猜测点和真实点
        Canvas(modifier = Modifier.fillMaxSize()) {
            print(size.width.toFloat())
            print("  ")
            println(size.height.toFloat())
            // 猜测点（小红点）
            if (guessLat != null && guessLng != null) {
                val guessOffset = latLngToOffset(
                    lat = guessLat,
                    lng = guessLng,
                    width = size.width.toFloat(),
                    height = size.height.toFloat()
                )
                drawCircle(
                    color = Color.Red,
                    radius = 6.dp.toPx(),
                    center = guessOffset
                )
            }

            // 正确答案（大红点，只在 revealed = true 时显示）
            if (revealed) {
                println(lat)
                println(lng)
                val answerOffset = latLngToOffset(
                    lat = lat,
                    lng = lng,
                    width = size.width.toFloat(),
                    height = size.height.toFloat()
                )
                println(answerOffset)
                drawCircle(
                    color = Color.Red,
                    radius = 10.dp.toPx(),
                    center = answerOffset,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}

/**
 * 把经纬度映射到当前 Canvas 的像素坐标
 */
private fun DrawScope.latLngToOffset(
    lat: Double,
    lng: Double,
    width: Float,
    height: Float
): Offset {
    // 经度：MIN_LNG(左) → MAX_LNG(右)
    val xRatio = ((lng - MIN_LNG) / (MAX_LNG - MIN_LNG)).toFloat()
    // 纬度：MAX_LAT(上) → MIN_LAT(下)，所以要用 MAX_LAT - lat
    val yRatio = ((MAX_LAT - lat) / (MAX_LAT - MIN_LAT)).toFloat()

    val x = (xRatio.coerceIn(0f, 1f)) * width
    val y = (yRatio.coerceIn(0f, 1f)) * height
    return Offset(x, y)
}

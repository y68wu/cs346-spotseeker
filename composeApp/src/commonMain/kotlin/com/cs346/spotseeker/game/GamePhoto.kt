package com.cs346.spotseeker.game

import org.jetbrains.compose.resources.DrawableResource

/**
 * 单人模式用的照片模型：imageRes 是一个 Compose Multiplatform 的 DrawableResource
 * 例如：Res.drawable.vote1
 */
data class GamePhoto(
    val id: String,
    val imageRes: DrawableResource,
    val latitude: Double,
    val longitude: Double,
    val description: String? = null
)

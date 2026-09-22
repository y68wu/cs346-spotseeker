package com.cs346.spotseeker.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun GameMap(
    lat: Double,
    lng: Double,
    modifier: Modifier = Modifier,
    guessLat: Double? = null,
    guessLng: Double? = null,
    revealed: Boolean = false,
    onMapClick: (Double, Double) -> Unit
)

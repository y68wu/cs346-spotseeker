package com.cs346.spotseeker.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun GameMap(
    lat: Double,
    lng: Double,
    modifier: Modifier,
    guessLat: Double?,
    guessLng: Double?,
    revealed: Boolean,
    onMapClick: (Double, Double) -> Unit
) {
    val uwCenter = LatLng(43.4723, -80.5449)
    val real = LatLng(lat, lng)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uwCenter, 15f)
    }

    LaunchedEffect(lat, lng, revealed) {
        if (!revealed) {
            val update = CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(uwCenter, 15f)
            )
            cameraPositionState.animate(update)
        } else {
            val gLat = guessLat
            val gLng = guessLng
            if (gLat != null && gLng != null) {
                val guess = LatLng(gLat, gLng)
                val bounds = LatLngBounds.builder()
                    .include(real)
                    .include(guess)
                    .build()
                val update = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                cameraPositionState.animate(update)
            } else {
                val update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(real, 16f)
                )
                cameraPositionState.animate(update)
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = { pos ->
            onMapClick(pos.latitude, pos.longitude)
        }
    ) {
        val gLat = guessLat
        val gLng = guessLng
        if (gLat != null && gLng != null) {
            Marker(
                state = MarkerState(position = LatLng(gLat, gLng)),
                title = "Your guess"
            )
        }
        if (revealed) {
            Marker(
                state = MarkerState(position = real),
                title = "Correct location"
            )
        }
    }
}

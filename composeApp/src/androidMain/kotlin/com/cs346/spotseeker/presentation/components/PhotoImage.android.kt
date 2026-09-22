package com.cs346.spotseeker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cs346.spotseeker.domain.Photo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun PhotoImage(
    photo: Photo?,
    placeholder: DrawableResource,
    modifier: Modifier,
    contentScale: ContentScale
) {
    val path = photo?.photoPath
    val placeholderPainter = painterResource(placeholder)

    if (!path.isNullOrBlank()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(path)
                .crossfade(true)
                .build(),
            placeholder = placeholderPainter,
            error = placeholderPainter,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Image(
            painter = placeholderPainter,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

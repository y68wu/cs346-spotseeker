package com.cs346.spotseeker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.cs346.spotseeker.domain.Photo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skia.Image as SkiaImage
import java.io.File
import java.net.URL

/**
 * Desktop implementation:
 *  - if photo.photoPath is a real file, load it from disk
 *  - otherwise fall back to the placeholder drawable
 */
@Composable
actual fun PhotoImage(
    photo: Photo?,
    placeholder: DrawableResource,
    modifier: Modifier,
    contentScale: ContentScale
) {
    val path = photo?.photoPath
    var bitmap by remember(path) { mutableStateOf<ImageBitmap?>(null) }

    // Load either a local file or a remote Supabase URL off the main thread.
    LaunchedEffect(path) {
        if (path.isNullOrBlank()) {
            bitmap = null
            return@LaunchedEffect
        }

        val loaded = withContext(Dispatchers.IO) {
            runCatching { loadBitmapFromPath(path) }
                .onFailure { cause ->
                    if (cause is CancellationException) throw cause
                }
                .getOrNull()
        }

        bitmap = loaded
    }

    val currentBitmap = bitmap
    if (currentBitmap != null) {
        Image(
            bitmap = currentBitmap,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Image(
            painter = painterResource(placeholder),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

private fun loadBitmapFromPath(path: String): ImageBitmap? {
    if (path.startsWith("http", ignoreCase = true)) {
        return URL(path).openStream().use { stream ->
            val bytes = stream.readBytes()
            SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
        }
    }

    val file = File(path)
    if (!file.exists()) return null

    return file.readBytes()
        .let(SkiaImage::makeFromEncoded)
        .toComposeImageBitmap()
}

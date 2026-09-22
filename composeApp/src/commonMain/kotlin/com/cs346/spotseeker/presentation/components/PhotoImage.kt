package com.cs346.spotseeker.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.cs346.spotseeker.domain.Photo
import org.jetbrains.compose.resources.DrawableResource

/**
 * Show the image for a Photo.
 *
 * On desktop (jvmMain), this tries to load the image from photo.photoPath
 * if that path is a real file. Otherwise it falls back to the given
 * placeholder drawable resource.
 *
 * On Android, we just use the placeholder for now.
 */
@Composable
expect fun PhotoImage(
    photo: Photo?,
    placeholder: DrawableResource,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
)

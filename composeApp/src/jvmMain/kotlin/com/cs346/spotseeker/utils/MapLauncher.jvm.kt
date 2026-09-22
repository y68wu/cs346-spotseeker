package com.cs346.spotseeker.utils

import java.awt.Desktop
import java.net.URI

actual fun openMapAt(lat: Double, lng: Double) {
    val desktop = runCatching { Desktop.getDesktop() }.getOrNull() ?: return
    val url = "https://www.google.com/maps/@$lat,$lng,16z"
    runCatching {
        desktop.browse(URI(url))
    }
}

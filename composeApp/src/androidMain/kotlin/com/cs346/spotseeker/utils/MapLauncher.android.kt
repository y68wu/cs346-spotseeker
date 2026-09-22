package com.cs346.spotseeker.utils

import android.content.Intent
import android.net.Uri
import com.cs346.spotseeker.MainActivity

actual fun openMapAt(lat: Double, lng: Double) {
    val activity = MainActivity.current() ?: return
    val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    activity.startActivity(intent)
}

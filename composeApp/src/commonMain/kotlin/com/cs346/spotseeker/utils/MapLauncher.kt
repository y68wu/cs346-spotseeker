package com.cs346.spotseeker.utils

/**
 * Open an external maps application or browser centered on the given latitude/longitude.
 * Desktop implementations typically forward to a browser, while other platforms can be no-ops
 * until a platform-specific hook is provided.
 */
expect fun openMapAt(lat: Double, lng: Double)

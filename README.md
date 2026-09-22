# SpotSeeker (CS346 team project)

SpotSeeker is a Kotlin Multiplatform location-guessing game built for the University of Waterloo campus and surrounding area. Players see a photo, guess its location on a map, and score points based on distance. The project targets Android and desktop (JVM) with Compose Multiplatform.

## Team and provenance

Built by Jinfeng Jiang, Chris Wu, Daria Jiang, and Yifan Zhang. This private GitHub copy is derived from the [team's UW GitLab repository](https://git.uwaterloo.ca/y472zhan/cs-346-project). It is a team project, not solely my work; the original GitLab repository remains the source of record for the contribution history.

## Features

- Campus-photo location guesses and distance-based scoring
- Single-player play and room-based multiplayer flows
- Photo upload and voting features
- Shared Kotlin UI and platform-specific Android/desktop implementations

## Build

From this directory, use the Gradle wrapper:

```sh
./gradlew :composeApp:assembleDebug  # Android
./gradlew :composeApp:run            # Desktop JVM
```

The original repository contained a literal Google Maps API key in `composeApp/src/androidMain/AndroidManifest.xml`. That value has been removed from this copy. To use Google Maps, supply a key restricted to your own application and platform before building. Do not commit a real key. Release APK/DMG files, course wiki material, and GitLab history are not mirrored here.

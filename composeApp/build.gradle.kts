import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.room.sqlite.wrapper)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation("androidx.fragment:fragment-ktx:1.6.2")

            // ===== Google Maps（Android 专用）=====
            implementation("com.google.android.gms:play-services-maps:18.2.0")
            implementation("com.google.maps.android:maps-compose:4.4.1")

            // ===== Image loading =====
            implementation("io.coil-kt:coil-compose:2.5.0")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            val voyagerVersion = "1.1.0-beta03"
            implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-kodein:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-rxjava:$voyagerVersion")

            // ===== Supabase & Serialization & Ktor（表 + 存储上传用）=====
            implementation(project.dependencies.platform("io.github.jan-tennert.supabase:bom:2.6.0"))
            implementation("io.github.jan-tennert.supabase:postgrest-kt")
            implementation("io.github.jan-tennert.supabase:gotrue-kt")
            implementation("io.github.jan-tennert.supabase:realtime-kt")
            // Storage client (add storage-kt so `client.storage` APIs are available)
            implementation("io.github.jan-tennert.supabase:storage-kt")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation("io.ktor:ktor-client-android:2.3.8")
            implementation("io.ktor:ktor-client-cio:2.3.8")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.cs346.spotseeker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.cs346.spotseeker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
}

compose.desktop {
    application {
        mainClass = "com.cs346.spotseeker.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.cs346.spotseeker"
            packageVersion = "1.0.0"
        }
    }
}

compose.resources {
    publicResClass = true
}

room {
    schemaDirectory("$projectDir/schemas")
}

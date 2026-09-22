package com.cs346.spotseeker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cs346.spotseeker.App
import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.data.MockPhotoRepository
import com.cs346.spotseeker.data.MySharedPhotoRepository
import com.cs346.spotseeker.data.ServiceLocator
import com.cs346.spotseeker.presentation.viewModels.AppViewModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.engine.cio.CIO

fun main() = application {
    fun supabaseRepoOrNull(): IPhotoRepository? {
        val url = "https://qwheyngpqggnsrdbvrym.supabase.co"
        val key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF3aGV5bmdwcWdnbnNyZGJ2cnltIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQzNDQ5NjIsImV4cCI6MjA3OTkyMDk2Mn0.Ni7ymKGgrzOUi14JSMlVvHJabfbY4DoGU0sWfIpDgV8"

        val client = createSupabaseClient(
            supabaseUrl = url,
            supabaseKey = key
        ) {
            httpEngine = CIO.create()
            install(Postgrest)
            install(Storage)
        }
        return MySharedPhotoRepository(client)
    }

    val repository: IPhotoRepository = supabaseRepoOrNull() ?: MockPhotoRepository()

    Window(onCloseRequest = ::exitApplication, title = "spotseeker") {
        App(AppViewModel(repository))
    }
}
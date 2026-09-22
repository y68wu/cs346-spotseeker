package com.cs346.spotseeker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.cs346.spotseeker.data.AndroidPhotoRepository
import com.cs346.spotseeker.data.IPhotoRepository
import com.cs346.spotseeker.data.MySharedPhotoRepository
import com.cs346.spotseeker.data.ServiceLocator
import com.cs346.spotseeker.data.getRoomDatabase
import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import com.cs346.spotseeker.presentation.viewModels.AppViewModel
import com.cs346.spotseeker.presentation.viewModels.VoteViewModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class MainActivity : ComponentActivity() {

    companion object {
        private var instanceRef: MainActivity? = null

        fun current(): MainActivity? = instanceRef
    }

    private data class PendingUpload(
        val address: Address,
        val viewModel: VoteViewModel,
        val onReturn: () -> Unit
    )

    private var pendingUpload: PendingUpload? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        val pending = pendingUpload ?: return@registerForActivityResult

        if (uri == null) {
            pendingUpload = null
            return@registerForActivityResult
        }

        lifecycleScope.launch {
            try {
                val bytes = withContext(Dispatchers.IO) {
                    contentResolver.openInputStream(uri)?.use { it.readBytes() }
                }

                if (bytes != null) {
                    val address = pending.address
                    val photo = Photo(photoPath = "", address = address)
                    pending.viewModel.uploadPhoto(photo, bytes)
                    pending.onReturn()
                }
            } finally {
                pendingUpload = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        instanceRef = this

        val repository = buildPhotoRepository()
        val appViewModel = AppViewModel(repository)

        setContent {
            App(appViewModel)
        }
    }

    override fun onDestroy() {
        if (instanceRef === this) {
            instanceRef = null
        }
        super.onDestroy()
    }

    fun launchPhotoPicker(
        address: Address,
        viewModel: VoteViewModel,
        onReturn: () -> Unit
    ) {
        pendingUpload = PendingUpload(address, viewModel, onReturn)
        pickImageLauncher.launch("image/*")
    }

    private fun buildPhotoRepository(): IPhotoRepository {
        val supabaseUrl = "https://qwheyngpqggnsrdbvrym.supabase.co"
        val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF3aGV5bmdwcWdnbnNyZGJ2cnltIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQzNDQ5NjIsImV4cCI6MjA3OTkyMDk2Mn0.Ni7ymKGgrzOUi14JSMlVvHJabfbY4DoGU0sWfIpDgV8"

        return try {
            val client = createSupabaseClient(
                supabaseUrl = supabaseUrl,
                supabaseKey = supabaseKey
            ) {
                httpEngine = Android.create()
                install(Postgrest)
                install(Storage)
            }
            MySharedPhotoRepository(client)
        } catch (error: Throwable) {
            AndroidPhotoRepository(getRoomDatabase(this).photoDao())
        }
    }

    private fun parseAddress(text: String): Address {
        val parts = text
            .split(',', ' ', '\n')
            .mapNotNull { it.trim().toDoubleOrNull() }

        return if (parts.size >= 2) {
            Address(parts[0], parts[1])
        } else {
            Address(0.0, 0.0)
        }
    }
}
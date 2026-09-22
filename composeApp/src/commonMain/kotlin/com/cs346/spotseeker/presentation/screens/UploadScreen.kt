package com.cs346.spotseeker.presentation.screens

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cs346.spotseeker.data.ServiceLocator
import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.VoteModel
import com.cs346.spotseeker.getPlatform
import com.cs346.spotseeker.presentation.components.InputField
import com.cs346.spotseeker.presentation.viewModels.VoteViewModel
import org.jetbrains.compose.resources.painterResource
import spotseeker.composeapp.generated.resources.Res
import spotseeker.composeapp.generated.resources.backicon

class UploadScreen(
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val onReturn: () -> Unit = {navigator.pop()}
        // Retrieve the shared ViewModel (Android: from MainActivity via bridge; common: create locally)
        val viewModel = ServiceLocator.voteViewModel

        var textX by remember { mutableStateOf("") }
        var x by remember { mutableStateOf(0.0) }

        var textY by remember { mutableStateOf("") }
        var y by remember { mutableStateOf(0.0) }


        Box(modifier = Modifier.fillMaxSize()) {

            // 左上角返回图标
            Image(
                painter = painterResource(Res.drawable.backicon),
                contentDescription = "Return",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onReturn() }
            )

            // 页面主体
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("Upload your photo to the game")
                Text("Make sure to enter the latitude and longitude before pressing \"Upload Photo\"")

                // 地址输入行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InputField(
                        label="Latitude",
                        text=textX,
                        onTextChange = { newText: String ->
                            // Only allow numbers and a single decimal point
//                            println(newText)
                            if (newText.isEmpty() || newText.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                textX = newText
                                x = newText.toDoubleOrNull() ?: 0.0
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )
                    InputField(
                        label="Longitude",
                        text=textY,
                        onTextChange = { newText: String ->
                            // Only allow numbers and a single decimal point
                            if (newText.isEmpty() || newText.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                textY = newText
                                y = newText.toDoubleOrNull() ?: 0.0
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )
                }

                // upload photo button
                Button(
                    onClick = {
                        val platformName = getPlatform().name
                        val isAndroid = platformName.startsWith("Android")

                        if (isAndroid) {
                            launchAndroidPhotoPicker(
                                address = Address(x, y),
                                viewModel = viewModel,
                                onReturn = onReturn
                            )
                            return@Button
                        }

                        val selection = pickAndStoreLocalPhoto(Address(x, y), viewModel.repository)
                        if (selection == null) {
                            return@Button
                        }

                        val (photo, bytes) = selection
                        if (bytes != null) {
                            viewModel.uploadPhoto(photo, bytes)
                        } else {
                            viewModel.addPhoto(photo)
                        }
                        onReturn()
                    }
                ) {
                    Text(text = "Upload Photo")
                }
            }
        }
    }
}

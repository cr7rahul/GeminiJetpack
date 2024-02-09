package com.example.geminijetpack.ui

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.geminijetpack.data.PromptData

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun InfoAboutImage(promptResultViewModel: PromptResultViewModel) {
    val viewModel by promptResultViewModel.mutablePromptResult.observeAsState()
    val context = LocalContext.current
    var photoUri: Uri? by remember { mutableStateOf(null) }
    var prompt by remember {
        mutableStateOf("")
    }
    var promptResult = ""
    var loading by remember {
        mutableStateOf(false)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //When the user has selected a photo, its URI is returned here
            photoUri = uri
        }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                //On button press, launch the photo picker
                launcher.launch(
                    PickVisualMediaRequest(
                        //Here we request only photos. Change this to .ImageAndVideo if you want videos too.
                        //Or use .VideoOnly if you only want videos.
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        ) {
            Text("Select Photo")
        }

        if (photoUri != null) {
            //Use Coil to display the selected image
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = photoUri)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .border(6.0.dp, Color.Gray),
                contentScale = ContentScale.Crop
            )

            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = {
                    Text(text = "Enter a prompt")
                }
            )

            Button(
                onClick = {

                    promptResultViewModel.getPromptResult(
                        prompt = PromptData(
                            photoUri = photoUri!!,
                            prompt = prompt,
                            context = context
                        )
                    )

                    viewModel.let {
                        if (it != null) {
                            promptResult = it
                            Log.d("Prompt Result", "InfoAboutImage: $promptResult")
                        }
                    }
                }
            ) {
                Text(text = "Check")
            }

            Text(
                text = promptResult,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showSystemUi = true)
@Composable
fun InfoAboutImagePreview() {
    InfoAboutImage(viewModel())
}
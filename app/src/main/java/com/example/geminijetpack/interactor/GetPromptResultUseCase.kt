package com.example.geminijetpack.interactor

import android.graphics.ImageDecoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.geminijetpack.constants.GEMINI_API_KEY
import com.example.geminijetpack.data.PromptData
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class GetPromptResultUseCase @Inject constructor() {


    @RequiresApi(Build.VERSION_CODES.P)
    suspend operator fun invoke(prompt: PromptData): String? {
        val bitmap = ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                prompt.context.contentResolver,
                prompt.photoUri
            )
        )
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = GEMINI_API_KEY,
            safetySettings = listOf(
                SafetySetting(
                    HarmCategory.HARASSMENT,
                    BlockThreshold.ONLY_HIGH
                )
            )
        )
        val inputContent = content {
            image(bitmap)
            text(prompt.prompt)
        }
        val response = generativeModel.generateContent(inputContent)
        return response.text
    }
}
package com.example.geminijetpack.data

import android.content.Context
import android.net.Uri

data class PromptData(
    val photoUri:Uri,
    val prompt:String,
    val context: Context
)

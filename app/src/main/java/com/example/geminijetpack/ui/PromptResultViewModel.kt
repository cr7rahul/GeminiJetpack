package com.example.geminijetpack.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminijetpack.data.PromptData
import com.example.geminijetpack.interactor.GetPromptResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromptResultViewModel @Inject constructor(
    private val getPromptResultUseCase: GetPromptResultUseCase
) : ViewModel() {

    private val _mutablePromptResult: MutableLiveData<String> = MutableLiveData()
    val mutablePromptResult: LiveData<String> = _mutablePromptResult

    @RequiresApi(Build.VERSION_CODES.P)
    fun getPromptResult(prompt: PromptData) = viewModelScope.launch {
        _mutablePromptResult.value = getPromptResultUseCase.invoke(prompt)
    }
}
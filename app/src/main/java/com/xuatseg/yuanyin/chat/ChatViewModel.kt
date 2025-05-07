package com.xuatseg.yuanyin.chat

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ChatViewModel(
    private val voiceProcessor: IVoiceProcessor
) : ViewModel() {
    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    fun startRecording(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1001
            )
            return
        }
        _isRecording.value = true
        voiceProcessor.startRecording()
    }

    fun stopRecording() {
        _isRecording.value = false
        viewModelScope.launch {
            val audioFile = voiceProcessor.stopRecording()
            // 流式语音识别
            voiceProcessor.speechToTextStream(audioFile) { partial, final ->
                _recognizedText.value = partial
                if (final != null) {
                    _recognizedText.value = final
                }
            }
        }
    }
} 
package com.xuatseg.yuanyin.chat

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onNavigateBack: () -> Unit
) {
    val recognizedText by viewModel.recognizedText.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = recognizedText,
            onValueChange = {},
            label = { Text("语音识别内容") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                onClick = { viewModel.startRecording(context) },
                enabled = !isRecording
            ) { Text("开始录音") }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = { viewModel.stopRecording() },
                enabled = isRecording
            ) { Text("停止录音") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateBack) {
            Text("返回")
        }
    }
} 
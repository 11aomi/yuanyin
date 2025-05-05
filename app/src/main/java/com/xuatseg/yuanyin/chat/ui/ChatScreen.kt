package com.xuatseg.yuanyin.chat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xuatseg.yuanyin.chat.*
import com.xuatseg.yuanyin.chat.presenter.ChatPresenter
import com.xuatseg.yuanyin.chat.presenter.IChatView

@Composable
fun ChatScreen(presenter: ChatPresenter) {
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }

    // 实现IChatView接口
    val chatView = object : IChatView {
        override fun onNewMessage(message: ChatMessage) {
            messages = messages + message
        }
        override fun onMessageStatusChanged(status: MessageStatus) {}
        override fun showMessageHistory(history: List<ChatMessage>) {
            messages = history
        }
    }

    // 绑定view
    LaunchedEffect(Unit) {
        presenter.attachView(chatView)
        presenter.loadHistory(50, 0)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages.size) { idx ->
                val msg = messages[idx]
                MessageItem(msg)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f).padding(8.dp)
            )
            Button(onClick = {
                if (input.isNotBlank()) {
                    presenter.sendTextMessage(input)
                    input = ""
                }
            }) {
                Text("发送")
            }
        }
    }
}

@Composable
fun MessageItem(msg: ChatMessage) {
    val isUser = msg.sender is MessageSender.User
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = when (val content = msg.content) {
                    is MessageContent.Text -> content.text
                    is MessageContent.Voice -> "[语音消息]"
                    is MessageContent.Error -> "[错误] ${content.error}"
                },
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
} 
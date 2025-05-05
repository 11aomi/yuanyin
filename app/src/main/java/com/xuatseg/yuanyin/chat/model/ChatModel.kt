package com.xuatseg.yuanyin.chat.model

import com.xuatseg.yuanyin.chat.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.util.*

class ChatModel : IChatInterface {
    private val messages = mutableListOf<ChatMessage>()
    private val newMessageFlow = MutableSharedFlow<ChatMessage>(replay = 1)
    private val messageStatusFlow = MutableSharedFlow<MessageStatus>(replay = 1)
    private val mutex = Mutex()

    override suspend fun sendTextMessage(message: String): String {
        val id = UUID.randomUUID().toString()
        val chatMessage = ChatMessage(
            id = id,
            content = MessageContent.Text(message),
            sender = MessageSender.User,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.Sending
        )
        mutex.withLock {
            messages.add(chatMessage)
        }
        newMessageFlow.emit(chatMessage)
        // 模拟发送成功
        val sentMessage = chatMessage.copy(status = MessageStatus.Sent)
        mutex.withLock {
            messages[messages.indexOfFirst { it.id == id }] = sentMessage
        }
        messageStatusFlow.emit(MessageStatus.Sent)
        return id
    }

    override suspend fun sendVoiceMessage(audioFile: File): String {
        // 语音消息后续实现
        throw NotImplementedError("sendVoiceMessage 未实现")
    }

    override suspend fun getMessageHistory(limit: Int, offset: Int): List<ChatMessage> {
        return mutex.withLock {
            messages.drop(offset).take(limit)
        }
    }

    override fun observeNewMessages(): Flow<ChatMessage> = newMessageFlow.asSharedFlow()

    override fun observeMessageStatus(): Flow<MessageStatus> = messageStatusFlow.asSharedFlow()

    override suspend fun retryMessage(messageId: String) {
        // 重试逻辑后续实现
    }

    override suspend fun deleteMessage(messageId: String) {
        mutex.withLock {
            messages.removeAll { it.id == messageId }
        }
    }
} 
package com.xuatseg.yuanyin.chat.presenter

import com.xuatseg.yuanyin.chat.*
import com.xuatseg.yuanyin.chat.model.ChatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChatPresenter(private val model: IChatInterface) {
    private var view: IChatView? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    fun attachView(view: IChatView) {
        this.view = view
        observeMessages()
    }

    fun detachView() {
        this.view = null
    }

    fun sendTextMessage(message: String) {
        scope.launch {
            val id = model.sendTextMessage(message)
            // 可根据需要处理id
        }
    }

    fun loadHistory(limit: Int, offset: Int) {
        scope.launch {
            val history = model.getMessageHistory(limit, offset)
            view?.showMessageHistory(history)
        }
    }

    private fun observeMessages() {
        scope.launch {
            model.observeNewMessages().collect { msg ->
                view?.onNewMessage(msg)
            }
        }
        scope.launch {
            model.observeMessageStatus().collect { status ->
                view?.onMessageStatusChanged(status)
            }
        }
    }
}

interface IChatView {
    fun onNewMessage(message: ChatMessage)
    fun onMessageStatusChanged(status: MessageStatus)
    fun showMessageHistory(history: List<ChatMessage>)
} 
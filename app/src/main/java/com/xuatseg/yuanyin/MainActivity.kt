package com.xuatseg.yuanyin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.xuatseg.yuanyin.chat.model.ChatModel
import com.xuatseg.yuanyin.chat.presenter.ChatPresenter
import com.xuatseg.yuanyin.chat.ui.ChatScreen
import com.xuatseg.yuanyin.mode.ModeManagerStub
import com.xuatseg.yuanyin.mode.ModeMonitorStub
import com.xuatseg.yuanyin.mode.ModePersistenceStub
import com.xuatseg.yuanyin.ui.control.RobotControlViewModel
import com.xuatseg.yuanyin.ui.screens.MainScreen
import com.xuatseg.yuanyin.ui.theme.YuanYinTheme
import com.xuatseg.yuanyin.viewmodel.MainViewModel
import com.xuatseg.yuanyin.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            modeManager = ModeManagerStub(),
            modePersistence = ModePersistenceStub(),
            modeMonitor = ModeMonitorStub()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YuanYinTheme {
                var showChat by remember { mutableStateOf(false) }
                MainSwitcher(showChat = showChat, onSwitch = { showChat = it }, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainSwitcher(showChat: Boolean, onSwitch: (Boolean) -> Unit, viewModel: MainViewModel) {
    if (showChat) {
        val chatModel = remember { ChatModel() }
        val chatPresenter = remember { ChatPresenter(chatModel) }
        Column {
            Button(onClick = { onSwitch(false) }) {
                Text("返回主界面")
            }
            ChatScreen(presenter = chatPresenter)
        }
    } else {
        Column {
            Button(onClick = { onSwitch(true) }) {
                Text("切换到本地聊天")
            }
            MainScreen(
                viewModel = viewModel,
                modeSwitchViewModel = viewModel,
                robotControlViewModel = RobotControlViewModel()
            )
        }
    }
}

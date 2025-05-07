package com.xuatseg.yuanyin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xuatseg.yuanyin.chat.ChatScreen
import com.xuatseg.yuanyin.chat.ChatViewModel
import com.xuatseg.yuanyin.chat.VoiceProcessorImpl
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
        val voiceProcessor = VoiceProcessorImpl(this)
        val chatViewModel = ChatViewModel(voiceProcessor)
        setContent {
            val navController = rememberNavController()
            YuanYinTheme {
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            viewModel = viewModel,
                            modeSwitchViewModel = viewModel,
                            robotControlViewModel = RobotControlViewModel(),
                            onSwitchToLocal = { navController.navigate("chat") }
                        )
                    }
                    composable("chat") {
                        ChatScreen(
                            viewModel = chatViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

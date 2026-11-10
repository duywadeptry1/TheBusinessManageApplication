package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainAdaptiveScreen
import com.example.ui.theme.GoShipTheme
import com.example.viewmodel.GoShipViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GoShipViewModel by viewModels {
        GoShipViewModel.provideFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val isDark = uiState.isDarkMode ?: systemDark

            GoShipTheme(darkTheme = isDark) {
                MainAdaptiveScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }
}

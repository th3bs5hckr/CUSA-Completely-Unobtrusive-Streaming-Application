package com.buuyst07.cusa.ui

import android.media.projection.MediaProjectionManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.buuyst07.cusa.*

enum class AppScreen {
    LOGIN,
    ACCOUNT_SELECT,
    CONFIG,
    STREAMING
}

@Composable
fun CUSAApp(
    activity: ComponentActivity,
    tokenManager: TokenManager,
    mediaProjectionManager: MediaProjectionManager
) {
    var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }
    var accounts by remember { mutableStateOf(emptyList<StreamAccount>()) }
    var selectedAccount by remember { mutableStateOf<StreamAccount?>(null) }
    var streamConfig by remember { mutableStateOf(StreamConfig()) }

    LaunchedEffect(Unit) {
        accounts = tokenManager.getAllAccounts()
        if (accounts.isNotEmpty()) {
            currentScreen = AppScreen.ACCOUNT_SELECT
            selectedAccount = accounts.firstOrNull()
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentScreen) {
                AppScreen.LOGIN -> LoginScreen(
                    tokenManager = tokenManager,
                    onAccountAdded = { account ->
                        accounts = accounts + account
                        currentScreen = AppScreen.ACCOUNT_SELECT
                    },
                    activity = activity
                )

                AppScreen.ACCOUNT_SELECT -> AccountSelectScreen(
                    accounts = accounts,
                    onAccountSelected = { account ->
                        selectedAccount = account
                        currentScreen = AppScreen.CONFIG
                    },
                    onAddAccount = {
                        currentScreen = AppScreen.LOGIN
                    },
                    onDeleteAccount = { account ->
                        accounts = accounts.filter { it.id != account.id }
                    },
                    tokenManager = tokenManager
                )

                AppScreen.CONFIG -> ConfigScreen(
                    selectedAccount = selectedAccount,
                    currentConfig = streamConfig,
                    onConfigChange = { newConfig ->
                        streamConfig = newConfig
                    },
                    onStartStream = {
                        currentScreen = AppScreen.STREAMING
                    },
                    onBack = {
                        currentScreen = AppScreen.ACCOUNT_SELECT
                    }
                )

                AppScreen.STREAMING -> StreamingScreen(
                    account = selectedAccount,
                    config = streamConfig,
                    mediaProjectionManager = mediaProjectionManager,
                    onStopStream = {
                        currentScreen = AppScreen.CONFIG
                    },
                    activity = activity
                )
            }
        }
    }
}

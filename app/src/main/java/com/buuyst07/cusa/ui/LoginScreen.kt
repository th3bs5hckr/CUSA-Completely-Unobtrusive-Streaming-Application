package com.buuyst07.cusa.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buuyst07.cusa.*

@Composable
fun LoginScreen(
    tokenManager: TokenManager,
    onAccountAdded: (StreamAccount) -> Unit,
    activity: ComponentActivity
) {
    val googleAuthManager = remember { GoogleAuthManager(activity, tokenManager) }
    val twitchAuthManager = remember { TwitchAuthManager(activity, tokenManager) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "CUSA",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Completely Unobtrusive Streaming Application",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Button(
                onClick = {
                    activity.startActivity(googleAuthManager.generateAuthIntent())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF0000)
                )
            ) {
                Text(
                    text = "Login with Google (YouTube)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = {
                    activity.startActivity(twitchAuthManager.generateAuthIntent())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9146FF)
                )
            ) {
                Text(
                    text = "Login with Twitch",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
            )

            Text(
                text = "No server required • Local token storage • Serverless streaming",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

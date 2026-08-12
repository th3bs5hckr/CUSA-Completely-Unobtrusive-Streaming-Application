package com.buuyst07.cusa

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class TwitchAuthManager(private val context: Context, private val tokenManager: TokenManager) {
    
    private val CLIENT_ID = "YOUR_TWITCH_CLIENT_ID"
    private val REDIRECT_URI = "com.buuyst07.cusa://oauth"
    private val SCOPE = "user:read:email channel:manage:broadcast"

    fun generateAuthIntent(): Intent {
        val state = UUID.randomUUID().toString()
        context.getSharedPreferences("oauth_state", Context.MODE_PRIVATE)
            .edit()
            .putString("twitch_state", state)
            .apply()

        val authUrl = "https://id.twitch.tv/oauth2/authorize?" +
                "client_id=$CLIENT_ID" +
                "&redirect_uri=$REDIRECT_URI" +
                "&response_type=code" +
                "&scope=${SCOPE.replace(" ", "%20")}" +
                "&state=$state" +
                "&force_verify=true"

        return Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
    }

    suspend fun exchangeCodeForToken(code: String): StreamAccount? = withContext(Dispatchers.IO) {
        return@withContext try {
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun refreshToken(account: StreamAccount): Boolean = withContext(Dispatchers.IO) {
        return@withContext if (account.refreshToken != null) {
            try {
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            false
        }
    }
}

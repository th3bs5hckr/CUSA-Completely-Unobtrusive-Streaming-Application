package com.buuyst07.cusa

import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.buuyst07.cusa.ui.CUSAApp

class MainActivity : ComponentActivity() {
    private val mediaProjectionManager by lazy {
        getSystemService(MediaProjectionManager::class.java)
    }
    private val tokenManager by lazy {
        TokenManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                CUSAApp(
                    activity = this@MainActivity,
                    tokenManager = tokenManager,
                    mediaProjectionManager = mediaProjectionManager
                )
            }
        }
    }

    companion object {
        const val PROJECTION_REQUEST_CODE = 1
        const val GOOGLE_AUTH_REQUEST = 2
        const val TWITCH_AUTH_REQUEST = 3
    }
}

package com.buuyst07.cusa

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class StreamingService : Service() {
    private var mediaProjection: MediaProjection? = null
    private var ffmpegRunning = false
    private var streamStartTime = 0L

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_START -> {
                val rtmpUrl = intent.getStringExtra(EXTRA_RTMP_URL) ?: ""
                val width = intent.getIntExtra(EXTRA_WIDTH, 1920)
                val height = intent.getIntExtra(EXTRA_HEIGHT, 1080)
                val fps = intent.getIntExtra(EXTRA_FPS, 30)
                val bitrate = intent.getIntExtra(EXTRA_BITRATE, 2500)
                val codec = intent.getStringExtra(EXTRA_CODEC) ?: "h264"
                val audioBitrate = intent.getIntExtra(EXTRA_AUDIO_BITRATE, 128)
                val enableAudio = intent.getBooleanExtra(EXTRA_ENABLE_AUDIO, true)

                startForegroundNotification()
                startStreaming(rtmpUrl, width, height, fps, bitrate, codec, audioBitrate, enableAudio)
            }

            ACTION_STOP -> {
                stopStreaming()
                stopSelf()
            }
        }

        return START_STICKY
    }

    private fun startForegroundNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("CUSA Streaming")
            .setContentText("Live stream active")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun startStreaming(
        rtmpUrl: String,
        width: Int,
        height: Int,
        fps: Int,
        bitrate: Int,
        codec: String,
        audioBitrate: Int,
        enableAudio: Boolean
    ) {
        if (!ffmpegRunning) {
            try {
                FFmpegBridge.startStream(
                    rtmpUrl = rtmpUrl,
                    width = width,
                    height = height,
                    fps = fps,
                    bitrate = bitrate,
                    codecName = codec,
                    audioBitrate = audioBitrate,
                    enableAudio = enableAudio
                )
                ffmpegRunning = true
                streamStartTime = System.currentTimeMillis()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopStreaming() {
        if (ffmpegRunning) {
            try {
                FFmpegBridge.stopStream()
                ffmpegRunning = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "CUSA Streaming",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for CUSA livestreaming"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "cusa_streaming_channel"

        const val ACTION_START = "com.buuyst07.cusa.action.START_STREAM"
        const val ACTION_STOP = "com.buuyst07.cusa.action.STOP_STREAM"

        const val EXTRA_RTMP_URL = "rtmp_url"
        const val EXTRA_WIDTH = "width"
        const val EXTRA_HEIGHT = "height"
        const val EXTRA_FPS = "fps"
        const val EXTRA_BITRATE = "bitrate"
        const val EXTRA_CODEC = "codec"
        const val EXTRA_AUDIO_BITRATE = "audio_bitrate"
        const val EXTRA_ENABLE_AUDIO = "enable_audio"

        fun startStream(
            context: Context,
            rtmpUrl: String,
            width: Int,
            height: Int,
            fps: Int,
            bitrate: Int,
            codec: String,
            audioBitrate: Int,
            enableAudio: Boolean
        ) {
            val intent = Intent(context, StreamingService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_RTMP_URL, rtmpUrl)
                putExtra(EXTRA_WIDTH, width)
                putExtra(EXTRA_HEIGHT, height)
                putExtra(EXTRA_FPS, fps)
                putExtra(EXTRA_BITRATE, bitrate)
                putExtra(EXTRA_CODEC, codec)
                putExtra(EXTRA_AUDIO_BITRATE, audioBitrate)
                putExtra(EXTRA_ENABLE_AUDIO, enableAudio)
            }
            context.startForegroundService(intent)
        }

        fun stopStream(context: Context) {
            val intent = Intent(context, StreamingService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}

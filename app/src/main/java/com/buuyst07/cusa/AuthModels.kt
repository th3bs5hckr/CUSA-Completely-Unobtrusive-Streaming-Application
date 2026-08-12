package com.buuyst07.cusa

data class StreamAccount(
    val id: String,
    val platform: Platform,
    val displayName: String,
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresIn: Long? = null,
    val addedAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val author: String,
    val text: String,
    val platform: Platform,
    val timestamp: Long
)

data class StreamConfig(
    val resolution: String = "1920x1080",
    val bitrate: Int = 2500,
    val fps: Int = 30,
    val codec: String = "h264",
    val audioBitrate: Int = 128,
    val enableAudio: Boolean = true,
    val audioSource: AudioSource = AudioSource.MIC
)

data class StreamStats(
    val viewerCount: Int = 0,
    val streamDuration: Long = 0,
    val bitrateCurrent: Int = 0,
    val fpsCurrent: Int = 0,
    val droppedFrames: Int = 0
)

enum class Platform {
    YOUTUBE,
    TWITCH
}

enum class AudioSource {
    MIC,
    SYSTEM,
    BOTH
}

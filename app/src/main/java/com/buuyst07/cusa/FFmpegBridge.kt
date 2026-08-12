package com.buuyst07.cusa

object FFmpegBridge {
    init {
        try {
            System.loadLibrary("ffmpeg_bridge")
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }

    external fun startStream(
        rtmpUrl: String,
        width: Int,
        height: Int,
        fps: Int,
        bitrate: Int,
        codecName: String,
        audioBitrate: Int,
        enableAudio: Boolean
    ): Int

    external fun stopStream(): Int

    external fun setVideoDimensions(width: Int, height: Int, fps: Int): Int

    external fun updateBitrate(bitrate: Int): Int

    external fun getCurrentStats(): String
}

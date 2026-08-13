package com.buuyst07.cusa

import android.util.Log

object FFmpegBridge {
    // Synchronous integer return codes kept for compatibility (0 = OK, non-zero = error)
    fun startStream(
        rtmpUrl: String,
        width: Int,
        height: Int,
        fps: Int,
        bitrate: Int,
        codecName: String,
        audioBitrate: Int,
        enableAudio: Boolean
    ): Int {
        return try {
            RTMPStreamer.start(rtmpUrl, width, height, fps, bitrate, audioBitrate, enableAudio)
            0
        } catch (e: Exception) {
            Log.e("FFmpegBridge", "startStream failed", e)
            -1
        }
    }

    fun stopStream(): Int {
        return try {
            RTMPStreamer.stop()
            0
        } catch (e: Exception) {
            Log.e("FFmpegBridge", "stopStream failed", e)
            -1
        }
    }

    fun setVideoDimensions(width: Int, height: Int, fps: Int): Int {
        return try {
            RTMPStreamer.setVideoDimensions(width, height, fps)
            0
        } catch (e: Exception) {
            Log.e("FFmpegBridge", "setVideoDimensions failed", e)
            -1
        }
    }

    fun updateBitrate(bitrate: Int): Int {
        return try {
            RTMPStreamer.updateBitrate(bitrate)
            0
        } catch (e: Exception) {
            Log.e("FFmpegBridge", "updateBitrate failed", e)
            -1
        }
    }

    fun getCurrentStats(): String {
        return try {
            RTMPStreamer.getStats()
        } catch (e: Exception) {
            Log.e("FFmpegBridge", "getCurrentStats failed", e)
            "{\"status\":\"error\"}"
        }
    }
}

# Native FFmpeg Bridge (Not Yet Implemented)

This directory is a placeholder. `FFmpegBridge.kt` declares these `external fun`
signatures, and calls `System.loadLibrary("ffmpeg_bridge")` — but no native
implementation exists here yet, so the app will compile but crash at runtime
when a stream is started, until this is filled in.

## What's needed

1. `CMakeLists.txt` — build config for the native lib
2. `ffmpeg_bridge.cpp` — JNI implementation matching:
   - `Java_com_buuyst07_cusa_FFmpegBridge_startStream`
   - `Java_com_buuyst07_cusa_FFmpegBridge_stopStream`
   - `Java_com_buuyst07_cusa_FFmpegBridge_setVideoDimensions`
   - `Java_com_buuyst07_cusa_FFmpegBridge_updateBitrate`
   - `Java_com_buuyst07_cusa_FFmpegBridge_getCurrentStats`
3. A wired `externalNativeBuild { cmake { path "src/cpp/CMakeLists.txt" } }`
   block in `app/build.gradle` (not present yet — `ndk.abiFilters` alone does
   not trigger native compilation)

## Two paths forward

- **Use `ffmpeg-kit-android` (already a Gradle dependency)** — call its
  Kotlin/Java API directly for screen-recording + RTMP push, and drop this
  `cpp/` directory and `FFmpegBridge.kt` entirely. Least native code to write.
- **Roll your own JNI** — MediaProjection → raw frames → libx264/FFmpeg
  encode → RTMP push, all in C++. Matches the approach already used for the
  SANE/libusb native bridge — more control, more work.

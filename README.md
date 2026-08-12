# CUSA: Completely Unobtrusive Streaming Application

Professional-grade Android livestreaming app with dual-platform support (YouTube + Twitch), real-time chat overlay, and zero-backend serverless architecture.

**by buuyst07** — Pure Algerian Product

## Features

- **Multi-Platform** — Simultaneously stream to YouTube and Twitch
- **Advanced Encoding** — H.264/H.265/VP9 codec selection
- **Real-time Chat** — Draggable, floating chat overlay
- **Live Statistics** — Viewer count, bitrate, FPS, dropped frames
- **No Watermark** — Professional output
- **Serverless** — No backend required, tokens encrypted locally

## Quick Start

```bash
git init
git remote add origin https://github.com/buuyst07/CUSA.git
git add .
git commit -m "CUSA: Initial release"
git push -u origin main
```

## OAuth Setup

1. Google: [Google Cloud Console](https://console.cloud.google.com)
2. Twitch: [Twitch Developer Console](https://dev.twitch.tv/console)

Add credentials to `GoogleAuthManager.kt` and `TwitchAuthManager.kt`

## Build

```bash
./gradlew assembleRelease
```

## License

MIT — Free to use and modify

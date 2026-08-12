# CUSA Quick Start

## 1. Clone & Setup

```bash
cd CUSA
```

## 2. Configure OAuth

**Google / YouTube:**
1. [Google Cloud Console](https://console.cloud.google.com)
2. Create OAuth Client ID (Android)
3. Add Client ID to `GoogleAuthManager.kt`

**Twitch:**
1. [Twitch Developer Console](https://dev.twitch.tv/console)
2. Create Application
3. Add Client ID to `TwitchAuthManager.kt`

## 3. Build

```bash
./gradlew assembleRelease
```

## 4. Sign

```bash
jarsigner -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore keystore.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk cusa-key
```

## 5. Install

```bash
adb install app-release-unsigned.apk
```

## GitHub Actions (Auto-Sign)

Add GitHub Secrets:
- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_PASSWORD`

Then push to main — auto-builds and signs!

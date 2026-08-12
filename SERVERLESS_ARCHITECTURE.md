# CUSA: Serverless Architecture

CUSA operates entirely without a backend server:

- ✓ **Zero hosting costs** — No server to maintain
- ✓ **Maximum privacy** — Tokens never leave your device
- ✓ **Offline-first** — Works without internet after initial auth
- ✓ **Direct streaming** — RTMP goes directly to YouTube/Twitch

## Token Storage

Tokens encrypted locally using Android Security Crypto (AES-256-GCM):

```
EncryptedSharedPreferences
├─ Encryption key: device KeyStore (hardware-backed if available)
├─ Each token: encrypted individually
└─ Tokens: only decrypted when needed for API calls
```

## API Communication

- YouTube: Direct RTMP to YouTube + YouTube Data API
- Twitch: Direct RTMP to Twitch + Twitch Helix API

No relay server, no intermediary.

## Multi-Account

Accounts stored as encrypted JSON locally. Switch instantly without re-authenticating.

## Data Privacy

✅ Encrypted: OAuth tokens, stream keys, account metadata  
❌ Never stored: Passwords, credit cards, personal data

See full documentation in SERVERLESS_ARCHITECTURE.md (in full package)

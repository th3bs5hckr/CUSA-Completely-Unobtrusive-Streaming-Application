# CUSA ProGuard rules

# Keep app models (serialized via Gson in TokenManager)
-keep class com.buuyst07.cusa.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Gson
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# FFmpegKit
-keep class com.arthenica.ffmpegkit.** { *; }
-dontwarn com.arthenica.ffmpegkit.**

# Google API Client / YouTube Data API
-keep class com.google.api.** { *; }
-keep class com.google.auth.** { *; }
-dontwarn com.google.api.**
-dontwarn com.google.auth.**

# OkHttp / Retrofit
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keepattributes Exceptions

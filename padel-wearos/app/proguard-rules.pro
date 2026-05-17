# Wear OS / Compose — keep rule essentials
-keep class androidx.wear.** { *; }
-keep class androidx.compose.** { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel { *; }
-dontwarn com.google.android.wearable.**

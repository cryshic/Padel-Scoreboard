# 🎾 Padel Scoreboard — Wear OS

A minimal, sport-optimized padel match scoreboard app for **Google Pixel Watch** and all Wear OS smartwatches. No internet, no ads, no login — just your match, on your wrist.

---

## 📱 Screenshots & Layout

```
┌─────────────────────────┐
│       10:42             │  ← Live clock (top arc)
│  ┌───────────────────┐  │
│  │  TEAM 1           │  │
│  │    30     [+] [−] │  │  ← Large score, tap-friendly buttons
│  │  Games: 1         │  │
│  └───────────────────┘  │
│  ─────── VS ─────────   │
│  ┌───────────────────┐  │
│  │  TEAM 2           │  │
│  │    15     [+] [−] │  │
│  │  Games: 0         │  │
│  └───────────────────┘  │
│      [ RESET MATCH ]    │
└─────────────────────────┘
```

---

## 🏗️ Project Structure

```
padel-wearos/
├── app/
│   ├── build.gradle.kts                    # App module Gradle config
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/padel/scoreboard/
│       │   ├── domain/
│       │   │   ├── model/
│       │   │   │   └── MatchState.kt       # PadelPoint enum + TeamState + MatchState
│       │   │   └── usecase/
│       │   │       └── ScoringUseCases.kt  # Increment, Decrement, Reset use cases
│       │   └── presentation/
│       │       ├── MainActivity.kt         # Entry point, screen-wake lock
│       │       ├── ScoreboardViewModel.kt  # State + haptics + debounce
│       │       ├── components/
│       │       │   ├── ScoreButton.kt      # Circular +/− button with press animation
│       │       │   ├── TeamScorePanel.kt   # One team's score card
│       │       │   └── ResetDialog.kt      # Confirmation overlay
│       │       ├── screens/
│       │       │   └── ScoreboardScreen.kt # Main watch UI
│       │       └── theme/
│       │           └── Theme.kt            # Colors, typography, MaterialTheme
│       └── res/
│           ├── values/strings.xml
│           ├── values/colors.xml
│           └── mipmap-*/ic_launcher.xml
├── build.gradle.kts                        # Root Gradle
├── settings.gradle.kts
├── gradle.properties
└── gradle/wrapper/gradle-wrapper.properties
```

---

## 🛠️ Requirements

| Tool | Version |
|------|---------|
| Android Studio | Hedgehog (2023.1.1) or newer |
| JDK | 17 or newer |
| Android SDK | API 34 (compileSdk) |
| Wear OS SDK | API 26+ (minSdk) |
| Kotlin | 1.9.22 |
| Gradle | 8.4 |

---

## 🔨 Build Instructions

### 1. Clone / Open in Android Studio

```bash
git clone <repo-url>
# OR: File → Open → select the padel-wearos folder
```

### 2. Sync Gradle

Android Studio should auto-sync. If not:
```
File → Sync Project with Gradle Files
```

### 3. Build Debug APK

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### 4. Build Release APK (signed)

```bash
./gradlew assembleRelease
```

You'll need a keystore. For testing, debug APK is sufficient.

---

## 📲 Install on Google Pixel Watch

### Option A — ADB (recommended for development)

**Prerequisites:**
- Enable Developer Options on your Pixel Watch:
  `Settings → System → About → Build number (tap 7 times)`
- Enable ADB debugging:
  `Settings → Developer Options → ADB debugging → ON`
- Connect watch to your computer via Bluetooth ADB or USB (with the charging dock):

```bash
# Find the watch IP (if using WiFi ADB — Pixel Watch 2 supports this)
adb connect <watch-ip>:5555

# Install the APK
adb -s <watch-ip>:5555 install app/build/outputs/apk/debug/app-debug.apk
```

**Alternatively via USB (Pixel Watch on charging dock):**
```bash
adb devices                     # Confirm watch is listed
adb install app-debug.apk
```

### Option B — Wear OS Companion App

1. Transfer APK to your phone
2. Use the **Wear Installer 2** app (free on Play Store) to sideload

### Option C — Google Play Store (production)

1. Sign the APK with a release keystore
2. Create a Wear OS app listing on Google Play Console
3. Upload as a Wear OS release

---

## 🎯 Padel Scoring System

| Points Scored | Display |
|--------------|---------|
| 0            | 0       |
| 1            | 15      |
| 2            | 30      |
| 3            | 40      |
| 4            | GAME ✓  |

When a team reaches **GAME**:
- Their game counter increments
- Both teams' points reset to **0**
- Ready for the next game instantly

The **−** button goes backward through the same sequence (useful for correcting mistakes).

---

## ⚙️ Features

| Feature | Status |
|---------|--------|
| Padel scoring (0→15→30→40→GAME) | ✅ |
| Games counter per team | ✅ |
| Haptic feedback on score change | ✅ |
| Double-tap prevention (500ms debounce) | ✅ |
| Keep screen awake during match | ✅ |
| Score animation (slide transition) | ✅ |
| Reset match with confirmation | ✅ |
| Circular watch UI (vignette) | ✅ |
| Dark / AMOLED optimized | ✅ |
| Offline only (no internet) | ✅ |
| No ads, no login | ✅ |

---

## 🔮 Future-Ready Architecture

The codebase is structured for easy feature additions:

### Add Match Timer
In `MatchState.kt`, uncomment:
```kotlin
val matchTimerSeconds: Long = 0
```
Create a `TimerUseCase` and add a `LaunchedEffect` in `ScoreboardScreen` to tick it.

### Add Set Counter
In `TeamState.kt`, uncomment:
```kotlin
val setsWon: Int = 0
```
Update `IncrementScoreUseCase` to track sets (typically first to 6 games wins a set).

### Add Serve Indicator
In `TeamState.kt`, uncomment:
```kotlin
val isServing: Boolean = false
```
Add a serve-toggle button in `TeamScorePanel`.

### Add Match History
In `MatchState.kt`, uncomment:
```kotlin
val matchHistory: List<GameRecord> = emptyList()
```
Create a `GameRecord` data class and persist with DataStore.

---

## 🏛️ Architecture

```
┌─────────────────────────────────────────┐
│              Presentation               │
│  MainActivity → ScoreboardScreen        │
│         ↕ StateFlow                     │
│  ScoreboardViewModel                    │
│    (debounce + haptics + state)         │
└──────────────┬──────────────────────────┘
               │ invoke()
┌──────────────▼──────────────────────────┐
│               Domain                    │
│  IncrementScoreUseCase                  │
│  DecrementScoreUseCase                  │
│  ResetMatchUseCase                      │
│         ↕ pure functions                │
│  MatchState / TeamState / PadelPoint    │
└─────────────────────────────────────────┘
```

- **Domain layer**: Pure Kotlin, zero Android imports, fully unit-testable
- **Presentation layer**: Compose UI + ViewModel, Android-aware
- **No data layer needed** (no persistence required for MVP)

---

## 🐛 Troubleshooting

**App not showing on watch after install:**
- Launch manually: App Drawer → scroll to find "Padel Score"
- Or: `adb shell am start com.padel.scoreboard/.presentation.MainActivity`

**ADB not detecting watch:**
- Ensure watch and phone are on the same WiFi network
- Try: `adb kill-server && adb start-server`

**Build fails on Compose version:**
- Ensure `kotlinCompilerExtensionVersion` in `build.gradle.kts` matches your Kotlin version
- Kotlin 1.9.22 → Compose Compiler 1.5.10 ✅

---

## 📄 License

MIT — free for personal and commercial use.

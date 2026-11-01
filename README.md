# OmniTranslate: Floating Screen & Smart Translation Hub 🌐⚡

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![ML Kit](https://img.shields.io/badge/AI-Google%20ML%20Kit-EA4335.svg?style=flat&logo=google)](https://developers.google.com/ml-kit)
[![Room DB](https://img.shields.io/badge/Database-Room%20SQLite-00599C.svg?style=flat)](https://developer.android.com/training/data-storage/room)
[![AdMob](https://img.shields.io/badge/Monetization-Google%20AdMob%2024.0.0-F4B400.svg?style=flat&logo=googleadmob)](https://admob.google.com/)

A modern, high-performance Android translation suite built with **Jetpack Compose**, **Material 3**, and **Google ML Kit**. OmniTranslate enables instant translation anywhere on Android via a draggable floating screen bubble, background clipboard logging, offline on-device language models, and camera/screenshot OCR vision.

---

## ✨ Features

- **⚡ Draggable Floating Screen Bubble**:
  - Floats smoothly over any app (WhatsApp, Chrome, YouTube, Notes, etc.) using `SYSTEM_ALERT_WINDOW`.
  - Magnetic snapping physics to screen edges with subtle neon pulsing indicator.
  - Interactive mini card overlay allows typing, pasting clipboard text, swapping languages, listening to TTS, and one-tap copying without leaving your current app.
- **🌐 100% On-Device Offline Translation (Google ML Kit)**:
  - Powered by Google ML Kit Translate and Language Identification.
  - Supports 17+ world languages (Bengali, English, Spanish, Hindi, Arabic, Japanese, French, German, Chinese, and more).
  - On-demand download and deletion of ~30MB language packs for zero-data translation anywhere.
- **📋 Smart Universal Clipboard History**:
  - Automatically captures copied text across applications in the background.
  - Backed by local **Room SQLite Database** with reactive `Flow` updates.
  - Full-text search and category filtering (`Favorites`, `Work`, `Chat`, `Starred`).
- **📷 Camera & Image Vision OCR**:
  - Extracts text from screenshots, documents, and photos using Google ML Kit Text Recognition.
  - Zero-permission modern Android Photo Picker integration (`ActivityResultContracts.PickVisualMedia`).
  - One-tap transfer from OCR text into the translation studio.
- **🔊 Natural Text-to-Speech (TTS)**:
  - Native Android TTS engine with accurate pronunciation across locales.
  - Adjustable speech speed controls (0.5x – 2.0x) with real-time test preview.
- **📲 GETMODDEVICE Channel Integration**:
  - Official channel promotion dialog (`t.me/getmoddevice`) featuring the custom GETMODDEVICE neon cyber emblem and Telegram badge.
- **💰 Google Mobile Ads (AdMob v24.0.0)**:
  - Native banner ad view docked cleanly above the navigation bar.
  - Preloaded full-screen interstitial ad manager.
  - Automatic `BuildConfig.DEBUG` detection with emulator test device configuration.

---

## 🛠️ Architecture & Tech Stack

- **Language:** Kotlin 100%
- **UI Toolkit:** Jetpack Compose with Material Design 3 (Custom Cyber Neon Dark + Crisp Light Themes)
- **Architecture:** Clean MVVM (Model-View-ViewModel) + Repository / Engine Pattern
- **Local Persistence:** Room Database with KSP (Kotlin Symbol Processing)
- **On-Device Machine Learning:**
  - `com.google.mlkit:translate`
  - `com.google.mlkit:language-id`
  - `com.google.mlkit:text-recognition`
- **Concurrency & Reactivity:** Kotlin Coroutines & StateFlow
- **Image Loading:** Coil Compose
- **Build System:** Gradle Kotlin DSL with Version Catalog (`libs.versions.toml`)

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio Ladybug / Koala** or newer.
- **JDK 17+**
- Android SDK with `compileSdk 36` and `minSdk 24` (Android 7.0+).

### Clone & Build
```bash
git clone https://github.com/your-username/OmniTranslate.git
cd OmniTranslate
gradle assembleDebug
```

---

## 🔒 Permissions & Privacy

- `SYSTEM_ALERT_WINDOW`: Exclusively used to present the user-activated floating translation bubble and mini overlay card.
- `FOREGROUND_SERVICE` & `FOREGROUND_SERVICE_SPECIAL_USE`: Maintains the background translation bubble service.
- `POST_NOTIFICATIONS`: Low-priority foreground notification required on Android 13+ to control and dismiss the floating bubble.
- **Zero-Permission Media**: Uses Android's system photo picker (`PickVisualMedia`) without requiring legacy broad storage permissions.
- **Data Safety**: All translation and clipboard history are processed strictly on-device. No clipboard data is transmitted to external servers.

---

## 👥 Community & Updates

Join our official Telegram channel for updates, tips, and new features:
👉 **[t.me/getmoddevice](https://t.me/getmoddevice)**

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).

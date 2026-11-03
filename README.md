# OmniTranslate: Floating Screen & Smart Translation Hub 🌐⚡

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![ML Kit](https://img.shields.io/badge/AI-Google%20ML%20Kit-EA4335.svg?style=flat&logo=google)](https://developers.google.com/ml-kit)
[![Room DB](https://img.shields.io/badge/Database-Room%20SQLite-00599C.svg?style=flat)](https://developer.android.com/training/data-storage/room)
[![AdMob](https://img.shields.io/badge/Monetization-Google%20AdMob%2024.0.0-F4B400.svg?style=flat&logo=googleadmob)](https://admob.google.com/)

A modern Android translation suite built with **Jetpack Compose**, **Material 3**, and **Google ML Kit**. OmniTranslate provides a draggable floating translation bubble, clipboard history, on-device language models, OCR, and text-to-speech.

---

## ✨ Features

- **⚡ Floating translation bubble:** A user-started overlay with a mini translation card, language switching, speech, and copy actions.
- **🌐 On-device translation:** Google ML Kit Translate and Language Identification, with language models downloaded on demand.
- **📋 Clipboard history:** While the floating-bubble service is running, copied text is observed and saved in the app's local Room database. Users can review and clear the history.
- **📷 Image OCR:** Extracts text from a user-selected image using Google ML Kit Text Recognition and Android's system photo picker.
- **🔊 Text-to-speech:** Uses the device's Android text-to-speech engine.
- **📲 Community link:** Optional GETMODDEVICE / Telegram community promotion.
- **💰 Advertising:** Google Mobile Ads SDK provides banner and interstitial ad placements.

---

## 🛠️ Architecture & Tech Stack

- Kotlin, Jetpack Compose, Material Design 3
- MVVM, Kotlin Coroutines, StateFlow
- Room SQLite local database
- Google ML Kit Translate, Language Identification, and Text Recognition
- Android TextToSpeech
- Google Mobile Ads SDK
- Gradle Kotlin DSL and Version Catalog

---

## 🚀 Getting Started

### Prerequisites
- Android Studio with Android SDK 36
- JDK 21 (the CI workflow uses JDK 21)
- Android 7.0+ (minSdk 24)

### Clone & Build
```bash
git clone https://github.com/ahmedshehjad/OmniTranslate.git
cd OmniTranslate
./gradlew assembleDebug
```

For a signed production APK/AAB, see [production release signing](docs/RELEASE_SIGNING.md). A debug APK is for development/testing only and must not be represented as the production release.

---

## 🔒 Permissions & Data Handling

- **Overlay permission:** Used for the floating bubble and mini-card overlay. The user must grant this special access.
- **Foreground service and notification:** Keep the user-started floating bubble active and provide a way to stop it.
- **Clipboard:** While the floating-bubble service is active, the app listens for clipboard changes, reads copied text, and stores the text and its translation in a local Room database. Stop the bubble to stop this listener; users can delete individual entries or clear the history.
- **Translation and OCR:** The app's ML Kit translation, language identification, and text-recognition features are implemented with on-device ML Kit APIs. Translation language models may need to be downloaded from Google over the internet before offline use.
- **Advertising and network:** The app includes Google Mobile Ads. The advertising SDK communicates with Google and may process device, diagnostic, and ad-interaction information under Google's applicable terms and privacy disclosures. Internet access is also used for ad requests and model downloads.
- **Local history backup:** Clipboard database content is excluded from Android cloud backup and device-to-device transfer by the app's backup rules.

The app source currently contains no app-operated backend endpoint for sending clipboard history. This statement does not cover data processed by third-party SDKs such as Google Mobile Ads. Read the [privacy policy draft](PRIVACY_POLICY.md) and verify all third-party SDK disclosures before distribution.

---

## 👥 Community & Updates

Join the community: [t.me/getmoddevice](https://t.me/getmoddevice)

---

## 📄 License

No license file is currently included. Do not assume that this repository is MIT-licensed or redistribute it under an open-source license until the copyright holder confirms the intended license and adds the corresponding license text.

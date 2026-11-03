# OmniTranslate Privacy Policy (Draft — publisher review required)

**Status:** Draft for review. Before submitting the app to any store, the publisher must replace the support-contact placeholder, confirm every statement against the final app and SDK configuration, and publish this policy at a stable public HTTPS URL.

**App:** OmniTranslate for Android  
**Effective date:** 3 November 2026

## Information handled by the app

### Clipboard text and translation history
When the user starts the floating-bubble service, OmniTranslate registers a clipboard-change listener. While that service is active, it reads text copied in other apps and stores the copied text, its translation (when available), language codes, and a timestamp in the app's local Room database. The app's main translation screen can also save translations to this history.

Clipboard history is stored on the device. Users can stop clipboard observation by stopping the floating bubble, delete individual history entries, or clear all history from the clipboard screen. The app's Android backup rules exclude the clipboard database from cloud backup and device-to-device transfer.

### Translation and language models
Translation and language identification use Google ML Kit on-device APIs. Language models may be downloaded from Google over the internet before they can be used offline. The app does not contain an app-operated server endpoint for uploading clipboard history in the reviewed source code. This does not make claims about data handling by Google SDKs or Android system services.

### Images and OCR
The user selects an image through Android's system photo picker. OmniTranslate processes the selected image with Google ML Kit Text Recognition on-device. The app does not request broad photo-library or storage access in its manifest.

### Advertising
OmniTranslate integrates Google Mobile Ads for banner and interstitial advertising. The Google Mobile Ads SDK may collect or process information such as IP address, device identifiers, app interaction, diagnostics, and advertising-related information, subject to Google's current SDK behavior, terms, and privacy disclosures. The publisher must review the exact SDK version, ad configuration, consent requirements, and regional requirements before release.

### Text-to-speech
Speech output uses the Android device's text-to-speech service. Depending on the installed TTS engine and device settings, speech processing may be handled by the device or by a system/third-party speech service. Review the selected engine's privacy information.

## Network use
The app requires internet access for Google Mobile Ads requests and for downloading ML Kit language models. No app-operated account system or application backend endpoint was identified in the reviewed source code. This must be rechecked whenever dependencies or app features change.

## Data retention and deletion
Clipboard history remains in the app's local database until the user deletes entries, clears the history, or uninstalls the app. The database is excluded from Android backup by the app's configured backup rules. Advertising SDK and system-service retention are governed by the relevant providers' policies.

## Children's privacy
The publisher must determine the intended audience and complete the relevant age-rating and child-directed advertising settings before distribution. Do not publish this draft until the audience and applicable legal requirements have been reviewed.

## Security
The app stores clipboard history in its private application database. No claim is made that this protects data on a rooted, compromised, or otherwise insecure device. Users should avoid copying highly sensitive information while clipboard capture is enabled.

## Changes to this policy
The publisher will update this policy when app behavior, SDKs, or data handling changes. The effective date will be updated with each material revision.

## Contact
**Publisher support email:** [REPLACE WITH A MONITORED SUPPORT EMAIL BEFORE PUBLICATION]

---

This is a technical draft based on the repository source reviewed on 3 November 2026. It is not legal advice and is not ready for store submission until the publisher completes the required review and replaces the contact placeholder.

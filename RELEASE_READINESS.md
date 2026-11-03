# OmniTranslate release-readiness audit

Audit baseline: `main` at `5cc92caf0e0f246a73ab96195361d63ccbe926e8` (2026-11-01).

## Verified build state

- GitHub Actions run [41311783926](https://github.com/ahmedshehjad/OmniTranslate/actions/runs/41311783926) completed successfully.
- The workflow ran `./gradlew assembleDebug`, not `assembleRelease`.
- The published GitHub asset is `app-debug.apk` (about 133 MiB). It is debug-signed and is not the production release binary.
- No production release signing key or signed release APK/AAB was verified in the repository or workflow.

## Release blockers to resolve

1. **Production signing:** establish a durable upload/signing key owned by the app publisher, store it only in GitHub Actions secrets, and build `assembleRelease`. Never publish the debug keystore or rely on an ephemeral key.
2. **Privacy disclosure:** the current README says clipboard data is not transmitted and processing is strictly on-device. The app's `FloatingBubbleService` registers a clipboard-change listener, reads copied text while the service is running, stores it in Room, and attempts ML Kit translation/model preparation. The app also initializes Google Mobile Ads. The privacy statement must be rewritten to accurately disclose clipboard access/storage, model downloads/network use, and advertising SDK data handling after a complete SDK/data-flow review.
3. **License mismatch:** README links to `LICENSE` and claims MIT, but no `LICENSE` file exists in the repository tree. Do not declare MIT on a store listing until the rights holder confirms the intended license and the actual license file is added. Uptodown defaults submissions to Proprietary if no open license is selected.
4. **Store assets and metadata:** prepare accurate short/full descriptions, icon (minimum 256x256 PNG recommended by Uptodown), screenshots, category, content/age rating answers, support contact, privacy-policy URL, and license/distribution details.
5. **Functional and policy QA:** test installation and upgrade, Android 7 through current supported Android versions, overlay permission flow, foreground-service behavior, clipboard capture consent/expectations, OCR/photo picker, language-model download/offline mode, TTS, and ads using test ads during QA. Review target SDK 36 behavior and store policy requirements.
6. **Release artifact size:** the debug APK is about 133 MiB. Inspect APK Analyzer and release build output; reduce unnecessary assets/dependencies if needed before submission.

## Store path

- **Primary:** Uptodown. Its developer registration and app publishing are documented as free. The publisher must create/verify the developer account and submit the app from that account.
- **Secondary candidate:** Xiaomi GetApps, subject to account and app-ownership verification.
- **Later candidate:** Samsung Galaxy Store. Samsung requires Seller Portal registration and commercial seller status before distributing apps.

## Release gate

Do not submit the current `app-debug.apk` as the official production release. The app is ready for a production release only after the signing, privacy, license, store metadata, and device QA gates above are completed. Account ownership, identity verification, and acceptance of store/legal terms must be performed by the app owner.

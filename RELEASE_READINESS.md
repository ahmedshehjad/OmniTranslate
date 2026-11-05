# OmniTranslate release-readiness audit

Audit updated: 5 November 2026.

## Verified repository and workflow state

- The production signing workflow exists at [`.github/workflows/release_apk.yml`](.github/workflows/release_apk.yml).
- The Android Gradle configuration assigns the `release` build type to a dedicated release signing config.
- The workflow is configured to restore a publisher-owned keystore from GitHub Actions secrets, run unit tests, build `assembleRelease` and `bundleRelease`, verify the APK signature with Android `apksigner`, verify the AAB signature with `jarsigner`, generate SHA-256 checksums, and upload APK/AAB/checksum artifacts.
- The current public GitHub Release asset `app-debug.apk` is a debug build. It is not the production release and must not be submitted as a signed production APK.
- GitHub Actions run [#19](https://github.com/ahmedshehjad/OmniTranslate/actions/runs/41957388472) was inspected. It failed at **Validate signing secrets and restore private keystore** before Gradle ran. The log explicitly reports `OMNITRANSLATE_KEYSTORE_BASE64` missing; the environment also showed blank values for the store password, key password, and alias. No signed APK/AAB was produced by this run.
- The workflow was updated to report all missing secret names in one failure message. This does not create or populate secrets.

## Required publisher action — signing credentials

The app publisher must create or select one permanent upload/signing keystore and securely back it up. Then add these four **repository Actions secrets** (never commit them to the repository and never paste them into chat):

| Secret name | Required value |
|---|---|
| `OMNITRANSLATE_KEYSTORE_BASE64` | Base64-encoded contents of the permanent JKS/keystore file |
| `OMNITRANSLATE_STORE_PASSWORD` | Keystore password |
| `OMNITRANSLATE_KEY_PASSWORD` | Private-key password |
| `OMNITRANSLATE_KEY_ALIAS` | Exact private-key alias |

GitHub path: **Repository → Settings → Secrets and variables → Actions → Repository secrets → New repository secret**.

After all four secrets are added, run **Actions → Build & Release OmniTranslate (Signed) → Run workflow**. Confirm the run reaches and passes all of these steps:

1. Validate signing secrets and restore private keystore
2. Run unit tests
3. Build signed APK and Android App Bundle
4. Verify APK and AAB signatures and generate SHA-256 checksums
5. Upload signed release artifacts

A manual workflow run uploads downloadable artifacts to the run. A public GitHub Release is created only when the workflow is triggered by a pushed `v*` tag. Do not create a release tag until the signed build and signature verification have passed.

## Other store-publication blockers

1. **Privacy policy:** `PRIVACY_POLICY.md` remains a technical draft with a support-email placeholder. The publisher must supply a monitored contact and review the complete app/SDK data flow and applicable store disclosures before publication.
2. **License:** no root `LICENSE` file is present. Do not claim MIT or another open-source license until the copyright holder chooses the license and adds its actual text.
3. **Store assets and metadata:** prepare accurate descriptions, app icon, screenshots, category, age/content rating answers, support contact, and a stable public privacy-policy URL.
4. **Device QA:** test fresh install and upgrade, overlay permission and foreground service, clipboard capture/stop/delete behavior, OCR/photo picker, model downloads and offline translation, TTS, and ad behavior on supported Android versions.
5. **Artifact size:** inspect the release APK/AAB size after the signed build and reduce unnecessary assets/dependencies if needed.

## Release decision

**Not yet production-release ready.** The signing workflow is present, but the required signing secrets are not configured, and the latest signed-workflow run failed before building. The existing `app-debug.apk` remains a development build. Store submission must wait until a signed release build is generated and verified, and the privacy, licensing, metadata, and device-QA items above are resolved.

The publisher must perform account ownership, identity verification, secret entry, keystore backup, and acceptance of store/legal terms. A successful CI signature check proves signing of the produced artifact; it does not by itself prove app-store approval or full runtime QA.

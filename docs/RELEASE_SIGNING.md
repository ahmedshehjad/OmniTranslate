# OmniTranslate production release signing

The repository must never contain a production keystore, its passwords, or a base64 copy of it.

## GitHub Actions secrets

Configure these repository Actions secrets before running the signed release workflow:

| Secret | Value |
|---|---|
| `OMNITRANSLATE_KEYSTORE_BASE64` | Base64-encoded bytes of the permanent `.jks` / `.keystore` file |
| `OMNITRANSLATE_STORE_PASSWORD` | Keystore password |
| `OMNITRANSLATE_KEY_PASSWORD` | Private-key password |
| `OMNITRANSLATE_KEY_ALIAS` | Exact alias inside the keystore |

The workflow refuses to build if any secret is missing or if the keystore cannot be opened with the supplied alias and password. It restores the keystore into the runner's temporary directory, runs unit tests, builds both `assembleRelease` and `bundleRelease`, verifies the APK signature with Android's `apksigner`, verifies the AAB signature with `jarsigner`, creates SHA-256 checksums for both release files, and uploads the APK/AAB/checksum file. A GitHub Release is created only for a pushed `v*` tag.

## Important identity and version rules

- Keep the same permanent signing key for every update distributed under this application ID.
- Do not replace or regenerate the key after publishing an APK unless a valid signing-key upgrade path is deliberately planned and tested.
- Increase `versionCode` for every public update; do not reuse a version code.
- The current application ID is `com.aistudio.omnitranslate.qkvzpt`. Changing it creates a different Android app identity and breaks in-place updates.
- Do not describe a debug-signed APK as a production release.
- A successful workflow proves the build and APK signature checks passed; it does not prove installation, upgrade compatibility, runtime behavior, privacy compliance, or approval by any app store.

## Before store submission

1. Confirm the permanent key is backed up in at least two secure locations.
2. Confirm the signer certificate fingerprint matches the previously distributed app, if one exists.
3. Install the signed APK on a clean Android device and test all core features.
4. Test upgrading over the previous public APK without uninstalling.
5. Review clipboard/overlay/notification permissions and disclose data handling accurately.
6. Provide a working privacy policy and store-specific screenshots, icon, description, and support contact.
7. Check the target store's current SDK, signing, content, and developer-account requirements.

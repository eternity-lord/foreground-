# FORGEBOUND — Release Signing (Section 16)

## Status
**Not yet produced in this sandbox.** The sandbox has no JDK and no Android
SDK, and outbound internet is limited to github.com / npm / pypi, so a
keystore cannot be generated and no APK/AAB can be built or signed here.

## Plan (to execute on a build machine with the Android SDK)
1. Generate a release keystore:
   `keytool -genkeypair -v -keystore forgebound-release.keystore -alias
   forgebound -keyalg RSA -keysize 2048 -validity 10000`
2. Store keystore details in this file (alias, passwords are NOT committed;
   keep them in a secure secret store — never in git).
3. Configure `app/build.gradle.kts` signingConfigs.release with the keystore
   path/alias (referenced via environment variables or a local.properties that
   is gitignored).
4. Build: `./gradlew :app:assembleRelease` → signed release APK +
   `./gradlew :app:bundleRelease` → signed release AAB.
5. Also build debug APK: `./gradlew :app:assembleDebug`.
6. Verify no INTERNET permission: inspect merged manifest and run
   `aapt dump badging app/build/outputs/apk/release/app-release.apk |
   grep -i permission` — must not list android.permission.INTERNET.

## Delivery paths (once built)
- app/build/outputs/apk/release/app-release.apk (signed, ≤150 MB target)
- app/build/outputs/bundle/release/app-release.aab (signed)
- app/build/outputs/apk/debug/app-debug.apk (debug)

Keep this file updated with the actual paths at delivery time. Do not commit
the .keystore/.jks file or passwords (see .gitignore).

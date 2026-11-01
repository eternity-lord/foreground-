# FORGEBOUND — Decisions Log (Sections 0, 17)

| # | Decision | Rationale |
|---|----------|-----------|
| D1 | Tech stack: Kotlin + Jetpack Compose (menus) + custom Canvas/SurfaceView 2D game loop (not LibGDX) | Prompt's decided stack; keeps one UI toolchain |
| D2 | Build tooling: Gradle Kotlin DSL, multi-module (app/core/data/game/ui/render/audio/i18n) | Section 3 architecture |
| D3 | MinSdk 24, TargetSdk 35, 64-bit arm64 + x86_64, locked sensorLandscape | Section 2/3/1 |
| D4 | NO INTERNET permission; VIBRATE only; all assets bundled | Section 3 non-negotiable rule |
| D5 | Content in JSON packs under assets/content/; generated deterministically by tools/gen_content.py (fixed seed); validated by tools/validate_content.py which fails the build | Sections 3, 12, 14 |
| D6 | Pure-Kotlin core/game modules (unit-testable on JVM, no Android deps); MVVM + unidirectional data flow in UI | Sections 2, 3 |
| D7 | Save format v1: Room + JSON serialization, versioned, atomic writes, last-3-saves rotation, checksum + time-tamper | Section 13 |
| D8 | Offline progress ticks on resume, capped at 8 hours | Section 1 (session-friendly) + 5.3a anti-cheat |
| D9 | XP(L) = round(80 * L^1.65); max level 120; +5 stat points/level, +1 skill point every 2 levels | Section 5.1 |
| D10 | DEX is the signature forging stat (widens timing windows, quality ceiling, crafting speed) | Section 5.1 |
| D11 | Death: respawn at zone entrance, small gold penalty, never lose items | Section 5.3 |
| D12 | Enhancement: failure never deletes items; at worst drops level by 1; protect scrolls prevent drop; pity floor 5% | Section 5.4 |
| D13 | Localization: English + Persian complete; RTL mirroring, Persian shaping, numerals toggle | Section 11 |
| D14 | Environment constraint: no JDK/Android SDK in this sandbox; internet limited to github.com/npm/pypi → Gradle builds, APK/AAB signing, emulator/UI tests deferred to a build machine; sources written to build with one `./gradlew` command | docs/PLAN.md constraint note |
| D15 | CI runs `:core:test` (pure-JVM balance sim `BalanceSimTest.kt`) + `testDebugUnitTest` (Android). `core/build.gradle.kts` uses JUnit 5 platform (`useJUnitPlatform()` + `junit-jupiter-engine` + `kotlin("test")`) so `kotlin.test` tests are discovered and executed. Balance is validated both in CI (`:core:test`) and by `tools/balance_sim.py` (PASSED: 0/20 soft locks, docs/BALANCE_REPORT.md). |

Open items: none blocking source-complete delivery; APK/AAB emission requires SDK machine.

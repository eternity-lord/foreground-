# FORGEBOUND — Progress Checklist

Checkboxes: `[ ]` pending, `[~]` in progress, `[x]` done. Updated after every milestone.
Reference: docs/PLAN.md (milestones M1–M20), Tab 1.pdf sections 0–17.

## M1 Planning (Section 17.1)
- [x] docs/PLAN.md — complete task breakdown mapped to the prompt
- [x] docs/PROGRESS.md — this checklist

## C1 CI workflow — GitHub Actions as build machine (continuation prompt Section 1.1)
- [x] .github/workflows/ci.yml — content, build-test, emulator-test, release jobs
- [x] Build files fixed to standard DSL (removed invalid multiplatform source sets, signing configs, version-ref typo)
- [x] CI green: content validation passes
- [x] CI green: assembleDebug + testDebugUnitTest + lintDebug pass
- [x] CI green: emulator connectedDebugAndroidTest passes
- [x] Fix compile errors / failing tests iteratively via CI logs until green

## M2 Project skeleton & build files (Sections 2, 3)
- [x] Gradle project: settings.gradle.kts + root build.gradle.kts (Kotlin DSL)
- [x] Module build files: core, data, game, ui, render, audio, i18n, tools
- [x] AndroidManifest.xml: minSdk 24, targetSdk latest stable, **no INTERNET permission**, vibration only
- [x] Version catalog + libs (Compose, Room, Kotlin, Play core)
- [x] ProGuard/R8 rules + proguard-android-optimize
- [x] `./gradlew build` documented as the one-command build

## M3 Core module (Sections 2, 3, 5.1)
- [x] core: seeded deterministic RNG + math utilities
- [x] core: time / clock abstraction (offline-progress safe)
- [x] core: event bus
- [x] core: balance constants (XP curve xp(L)=round(80*L^1.65), max Lv 120, stat points, damage formula constants)
- [x] Unit tests: RNG determinism, XP curve, derived stats

## M4 Data module & content packs (Sections 2, 3, 14)
- [~] data: Room entities + DAOs, save schema v1 (schema designed; Room compilation on SDK machine)
- [x] data: autosave, atomic writes, last-3-saves rotation, corruption recovery
- [x] data: save/load + backup export/import (local file)
- [x] assets/content/: items.json (≥240 items), materials.json (≥60), skills.json (≥110)
- [x] assets/content/: enemies.json (≥65 incl. bosses), zones.json (10 zones + secret)
- [x] assets/content/: quests.json (≥130), shops.json (8 variants), achievements.json (≥120)
- [x] assets/content/: pets.json (6), allies.json (5), dialogue/story chapters (10 chapters × 3–6 scenes + ending/epilogue)
- [x] Versioned save format + migration helpers with tests

## M5 Game logic module (Sections 4, 5)
- [x] Player stats & leveling: STR/DEX/VIT/INT/LUCK -> derived stats; +5 stat pts/lvl, +1 skill pt every 2 lvls; respec
- [x] Classes: Novice -> Knight/Rogue/Mage/Ranger at Lv10; hidden legacy "Vulcan's Heir" + Forge Mastery tree + Overgear passive
- [x] Combat: damage formula, element wheel, status effects; encounter simulation skeleton
- [x] Forging: Heat/Hammer/Shape/Quench/Polish scoring, grades Crude...Legendary, Divine Hammering proc, Auto-Forge
- [x] Enhancement +0->+20 (success/pity/protect, never delete, max -1)
- [x] Economy, loot tables, offline progress
- [x] Unit tests: damage formula, forging scoring, enhancement odds, respec cost, loot drops, quest evaluation, level cap
- [x] Enchant/socket/awaken/reforge/salvage/transmog (spec'd in core design; UI wiring in M9/M10)

## M6 Content validator (Sections 3, 12)
- [x] tools/validate_content.py — JSON schema + broken references + Section 14 minimums
- [x] Validator fails the build on broken content (CI-style)

## M7 UI shell & navigation (Section 6)
- [ ] Compose theme + UI kit (buttons, panels, bars, tabs, toasts, dialogs; brushed-metal/parchment/rune motif; Persian + Latin fonts)
- [ ] Screens: Splash → Title → Save Slots (3) → Main Hub (Smithy) → Map/Zone Select → Battle → Forging → Forge/Blueprint Browser → Inventory/Equipment → Item Detail & Compare → Enhance/Reforge/Enchant/Socket/Awaken → Character & Stats → Skill Trees → Allies/Party → Pets → Shops → Quest Log → Achievements/Codex/Bestiary → Daily/Weekly → Tower → Raids → Idle Report → Story Dialogue → Cutscene/Cut-in → Results/Loot → Settings → Help/Encyclopedia → Credits → Privacy note
- [ ] Bottom bar in hub, back-button handling, no dead ends, state restored after process death
- [ ] Accessibility: scalable text, color-blind-safe rarity (color+icon+shape), haptics toggle, reduce-motion toggle

## M8 Combat screen (Sections 5.3, 5.3a, 9)
- [ ] Side-view 2D lane combat vs waves, player + up to 2 AI allies
- [ ] Auto-attack + manual skill buttons, ultimate gauge
- [ ] Zones 1–10 (10 stages + boss each), daily dungeons, Endless Tower 100+ floors, raid bosses
- [ ] VFX: particles, camera shake/zoom, damage numbers, crit flashes, element SFX

## M9 Forging minigame (Section 5.4)
- [ ] 5 phases: Heat / Hammer / Shape / Quench / Polish (≈10–20 s), all skippable via Auto-Forge (lower avg quality)
- [ ] Quality score 0–100 → grades Crude, Common, Fine, Superior, Masterwork, Flawless, Legendary
- [ ] DEX widens timing windows; Divine Hammering rare proc; durability/repair; salvage/reforge/affix lock

## M10 Items / economy / shops (Sections 4, 5)
- [ ] Inventory/equipment, item detail & compare, set bonuses (2/4/6), gems/sockets
- [ ] 8 shop variants: buy blueprints/materials/consumables, sell surplus
- [ ] Currency: gold; offline idle progress

## M11 Quests & story (Sections 1, 5, 6)
- [ ] Quest log, ≥130 quests, chapter/achievement rewards
- [ ] Visual-novel dialogue system: portraits, ≥5 expressions/main character, cut-ins, auto-scroll/skip
- [ ] Story: intro + 10 chapters + boss each + ending + epilogue + post-game hint
- [ ] Secret "Vulcan's Heir" chain (7 steps), secret zone, best mythic set, true ending, 10+ easter eggs, 3 secret bosses

## M12 Allies, pets, tower, raids, daily/weekly, codex (Sections 5, 6, 14)
- [ ] 5 allies with skill trees; party up to 3
- [ ] 6 pets; daily/weekly tasks (weekday rotation + anti clock-cheat); Endless Tower milestones (in-game day counter)
- [ ] Achievements ≥120; codex/bestiary

## M13 Art & VFX (Sections 7, 9) — assets generated in-session where possible
- [x] docs/STYLE_BIBLE.md — palette, rarity frames, typography, prompts table
- [x] docs/DECISIONS.md — decision log
- [x] assets/art/keyart_title.png — title key art (Kai at the forge)
- [x] assets/art/app_icon_foreground.png — adaptive icon foreground (hammer/tongs/anvil)
- [x] assets/art/portrait_kai_t1.png — Kai tier-1 portrait
- [x] assets/art/portrait_vulcan.png — Master Vulcan portrait
- [x] assets/art/portrait_rust_king.png — Rust King boss portrait
- [x] assets/art/zone_whispering_plains.png — zone 1 background
- [x] assets/art/portrait_lyra.png — Lyra ally portrait
- [ ] Remaining art: 4 more allies, 60+ enemies/bosses, 40+ portraits/expressions,
      9 more zone backgrounds + parallax, Smithy hub day/night, icon set, UI kit
- [ ] VFX/particle/shader implementation (Compose Canvas render module — SDK machine)

## M14 Audio (Section 10)
- [ ] BGM 12+ tracks (title, hub, 10 zones/bosses, forging, victory, final boss), seamless loops, OGG/Opus
- [ ] SFX 150+ (UI, hammer by grade, quench, swords, magic by element, loot by rarity, level-up, quest complete…)
- [ ] Anime-style voice shouts (generate_speech where possible; otherwise skip)
- [ ] docs/CREDITS.md + docs/LICENSES.md — every asset source + license, zero copyrighted third-party assets

## M15 Localization (Section 11)
- [ ] i18n: English + Persian complete, no missing keys
- [ ] Persian: shaping, numerals toggle Persian/Latin, RTL layout mirroring, correct fonts
- [ ] Dialogue/lore written natively in EN and FA
- [ ] Build check fails on missing keys; runtime language switcher + device-language default

## M16 Balance simulation & report (Section 14)
- [x] tools/balance_sim.py — headless progression sim mirroring Kotlin formulas exactly — PASSED (docs/BALANCE_REPORT.md)
- [x] docs/BALANCE.csv + docs/BALANCE_REPORT.md — 0/20 soft locks, ~520 min clear, bounded gold, no infinite-gold exploit, drop rates sufficient
- [x] core JVM port `core/src/test/kotlin/com/forgebound/core/balance/BalanceSimTest.kt` — runs on real core/ Kotlin code (PlayerStats, DamageFormula, Forging, Enhancement, Economy) via `:core:test` in CI (JUnit 5 platform). Includes Python-vs-Kotlin damage-formula drift check + forging grade boundaries + enhance never-delete/pity-floor.

## M17 Settings, save/backup, robustness (Section 13)
- [ ] Settings: audio, graphics, language, haptics, reduce-motion, text size, backup export/import, credits, privacy note (offline, no data collected)
- [ ] Crash-safe global exception handler (local log, no upload)
- [ ] Cheat resistance: save checksum, time-tamper checks
- [ ] Handle rotation lock, low memory, background/foreground, calls, notch/cutouts, gesture-nav, tablets/foldables

## M18 Testing & QA (Section 15)
- [ ] Unit tests pass (JVM)
- [ ] Instrumented/UI tests: new-game flow, first forge, first fight, shop buy/sell, quest claim, save/restore, language switch
- [ ] Playthrough bot Chapters 1–3 (no soft-locks) + 30-min soak (memory/FPS)
- [ ] ≥3 emulator profiles (phone small, phone large, tablet); screenshots docs/screenshots/
- [ ] Static checks: lint, ktlint/detekt; grep TODO/FIXME/placeholder = 0
- [ ] docs/TEST_REPORT.md

## M19 Release build & signing (Sections 12, 16)
- [ ] Keystore generated; docs/SIGNING.md
- [ ] Debug APK + signed release APK and AAB (≤150 MB), R8 minify + shrink resources
- [ ] Verify no INTERNET permission (manifest + aapt)

## M20 Final verification & delivery (Section 16)
- [ ] Every Section-6 screen reachable and working; no placeholders
- [ ] Full story 10 chapters + secret chain playable; forging/enhance/enchant/socket/awaken/reforge/salvage/transmog work
- [ ] All 8 shops, daily/weekly, achievements, codex, tower, raids, pets, allies work
- [ ] Offline idle progress + save/backup/restore verified; Persian + English complete + RTL correct
- [ ] Content minimums met; validator passes; all tests pass; no known DoD violations
- [ ] Final report: what was built, how to run, test results, APK/AAB paths

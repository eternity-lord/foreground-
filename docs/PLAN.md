# FORGEBOUND — Implementation Plan

Source: `Tab 1.pdf` (MASTER PROMPT, 13 pages, sections 0–17).
Working title: **Forgebound** (Persian: «برگزیده آهنگرِ»).
Branch: `arena/8b46d4b3-foreground` (commit after every completed milestone, push to this branch).

## Constraint note (environment)
This sandbox has **no JDK and no Android SDK**, and outbound internet is limited to
github.com / codeload.github.com / api.github.com / registry.npmjs.org / pypi.org /
files.pythonhosted.org. Therefore:
- Pure-Kotlin modules (core, game logic) can be written and, if a Kotlin compiler
  binary can be fetched from GitHub releases, compiled + unit-tested on the JVM.
- Compose UI, Room, Gradle Android builds, signed APK/AAB, Espresso/Compose UI tests
  and emulator runs **cannot be executed here**. The source is written so it builds
  with one Gradle command on a machine with Android SDK (target SDK latest stable,
  minSdk 24, arm64 + x86_64, no INTERNET permission).
- Music ("songs") cannot be synthesized by the available speech tool; where art/audio
  cannot be created in-session, CC0/OFL sources are documented in docs/CREDITS.md and
  docs/LICENSES.md and fetched at build time per Section 12.

## Milestone map (mapped to the prompt sections)

| Milestone | Prompt sections | Deliverables | Status |
|---|---|---|---|
| M1 Planning | 0, 17 | docs/PLAN.md, docs/PROGRESS.md | done |
| M2 Project skeleton & build files | 2, 3 | settings.gradle.kts, build.gradle.kts, module dirs, AndroidManifest.xml (no INTERNET), ProGuard rules, version catalog | todo |
| M3 Core module | 2, 3, 5.1 | core/src: RNG (seeded, deterministic), math, time, event bus, balance constants | todo |
| M4 Data module & content packs | 2, 3, 14 | data/src: Room entities/DAOs (schema), JSON content packs in assets/content/ (items, materials, skills, enemies, zones, quests, shops, dialogue, story chapters), save format v1 + last-3-saves rotation | todo |
| M5 Game logic module | 4, 5 | game/src: player stats/leveling (xp(L)=round(80*L^1.65), Lv120), classes (Novice→Knight/Rogue/Mage/Ranger, Vulcan's Heir hidden legacy), combat damage formula + elements wheel + status effects, forging minigame scoring + grades, enhancement/enchant/socket/awaken/reforge/salvage/transmog, economy, offline progress, achievements | todo |
| M6 Content validator | 3, 12 | tools/validate_content.py — fails on broken references/content minimums; wired to fail build | todo |
| M7 UI shell & navigation | 6 | ui/src: Compose theme, screens (Splash→Title→Save Slots→Hub→Map→Battle→Forging→…→Settings), bottom nav, back handling, state restore | todo* |
| M8 Combat screen | 5.3, 5.3a, 9 | render/src + ui: side-view lane combat, auto-battle + manual skills, dodge/guard perfect timing, VFX (particles, shake, damage numbers), zones 1–10, tower 100 floors, raids | todo* |
| M9 Forging minigame | 5.4 | Heat/Hammer/Shape/Quench/Polish phases, Auto-Forge, grades Crude→Legendary, Divine Hammering proc | todo* |
| M10 Items/economy/shops | 4, 5 | Inventory/equipment, item detail & compare, 8 shop variants, buy/sell, salvage | todo* |
| M11 Quests & story | 1, 5, 6 | Quest log, 10 chapters × 3–6 dialogue scenes, visual-novel dialogue system, ending + epilogue + post-game hint, secret "Vulcan's Heir" 7-step chain, secret zone, secret bosses, easter eggs | todo* |
| M12 Allies, pets, tower, raids, daily/weekly, codex/bestiary, achievements | 5, 6, 14 | Party (up to 3), 6 pets, endless tower, raid bosses, daily dungeons (weekday rotation + anti-cheat), 120+ achievements, codex | todo* |
| M13 Art & VFX | 7, 9 | Key art, app icon (adaptive), feature graphic, Kai 6 tier stages, 5 allies, Vulcan, Rust King, enemies/bosses, portraits (5+ expressions), cut-ins, 10 zone backgrounds + parallax, Smithy day/night, icon set with rarity frames, UI kit, particle system, shaders (AGSL w/ Canvas fallback), style bible docs/STYLE_BIBLE.md | part* (key art/icons generated where possible) |
| M14 Audio | 10 | 12+ BGM loops, 150+ SFX, voice shouts (generate_speech where possible), OGG/Opus, loudness mix, docs/CREDITS.md + docs/LICENSES.md | part* (speech shouts + proc SFX; music documented as CC0 build-time fetch) |
| M15 Localization | 11 | i18n/: English + Persian (fa) complete strings, RTL mirroring, Persian shaping + numerals toggle, build check for missing keys | todo |
| M16 Balance simulation & report | 14 | tools/balance_sim.py (headless progression sim, time-to-clear, economy exploits, drop rates) → docs/BALANCE.csv + docs/BALANCE_REPORT.md | todo |
| M17 Settings, save/backup, robustness | 13 | Settings screen (audio/graphics/language/haptics/reduce-motion/text size/backup export-import/credits/privacy), autosave, atomic writes, migrations, checksum + time-tamper, crash handler | todo* |
| M18 Testing & QA | 15 | Unit tests (damage, forging, drops, enhancement, economy, save/migrate, i18n completeness), playthrough bot Chapters 1–3, 30-min soak test plan, screenshots docs/screenshots/, docs/TEST_REPORT.md, ktlint/detekt/lint, grep for TODO/FIXME/placeholder = zero | todo* |
| M19 Release build & signing | 12, 16 | Keystore (docs/SIGNING.md), debug APK + signed release APK/AAB (≤150 MB), R8 minify, ABI splits optional, aapt INTERNET-permission check | todo* (needs Android SDK machine) |
| M20 Final verification | 16 | Full Definition of Done check, final report, docs/PROGRESS.md fully checked | todo |

\* `todo*` = requires Android SDK; source is written and reviewed, execution deferred to a build machine.

## Execution order (per Section 17.2)
project skeleton & content pipeline → core systems & data → UI shell & navigation → combat → forging → items/economy/shops → quests/story → art/VFX/audio → localization → balance sim → tests → performance pass → polish → release build → final verification.

After each internal milestone: run build + tests, fix everything before moving on.
Ambiguities are decided and recorded in docs/DECISIONS.md; never stop to ask questions.

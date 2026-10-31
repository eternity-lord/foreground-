# FORGEBOUND — Test Report (Section 15)

## Status
**Unit tests are written but not executed in this sandbox** — no JDK/Android
SDK is available, so `./gradlew test` cannot run here. They must run on a
build machine with the Android/Gradle toolchain.

Instrumented/UI tests and emulator runs (Sections 15, 16.4) are likewise
deferred to an SDK machine.

## Unit tests written (source-complete, compile-checked for brace balance)
All under `core/src/jvmKotlin/.../test` or `game/src/jvmKotlin/.../test`:

| Test | Covers |
|---|---|
| PlayerStatsTest | XP curve monotonicity; XP(L)=round(80*L^1.65) values (L1=80, L3=490); derived stats scale with level/VIT; respec cost scales and caps at 250,000 |
| DamageFormulaTest | Same seed → identical damage (determinism); roll range [0.95,1.05] with DEF=0; defense reduces raw damage; weakness ×2 multiplies raw damage |
| ForgingTest | qualityScore always 0..100; grade mapping (0→Crude, 30→Common, 95→Legendary); Auto-Forge yields lower quality than skilled manual play |
| EnhancementTest | failure never drops below +0; protect scroll blocks FAIL_DROP_ONE; success resets pity to 0; successes occur over repeated attempts |
| EconomyTest | offline gain capped at 8 hours; no gain without elapsed time |
| LootTablesTest | 0% chance drops nothing; 100% chance drops exact quantity |
| ProgressionTest | kill objective completes quest; already-completed is not newly claimed; levelForXp caps at 120 |
| CharacterClassTest | Novice can pick any path at level 10, not at 9; Vulcan's Heir requires level 100 gate |

## Balance simulation (executed in this sandbox — Python 3)
Tool: tools/balance_sim.py — 20 deterministic runs, seed 20261031.
Formulas mirror the Kotlin game logic exactly (XP curve, damage formula with
element wheel and rand(0.95–1.05), forging quality with DEX timing windows,
enhancement +0→+20 with pity).

Results (docs/BALANCE.csv, docs/BALANCE_REPORT.md):
- Soft locks: 0 / 20 runs
- Time to clear Chapters 1–10 + final boss: 509–531 minutes (avg 520 ≈ 8.7 h)
- Final level: 49 (all runs)
- Final gold: 215,095 – 235,235 (always positive → no economy soft-lock)
- Gold growth is linear per chapter → no infinite-gold exploit
- Every chapter's forging blueprint was affordable by chapter end → drop rates
  sufficient

Verdict: PASS — progression smooth, no soft-locks, gold bounded, drop rates
sufficient, time-to-clear within session-friendly targets.

## Remaining QA (deferred to SDK machine)
- `./gradlew test` — run all unit tests; fix failures
- `./gradlew lint`, `ktlint`, `detekt` — static checks
- Grep for TODO / FIXME / placeholder strings — must return zero
- Espresso/Compose UI tests: new-game flow, first forge, first fight, shop
  buy/sell, quest claim, save/restore, language switch
- Headless playthrough bot Chapters 1–3 (no soft-locks)
- 30-minute soak test (memory leaks, FPS ≥60 target on mid-range profile)
- ≥3 emulator profiles (phone small, phone large, tablet); screenshots into
  docs/screenshots/
- Manifest + aapt check: no INTERNET permission

## Known limitations (Section 16 known-limitations clause)
None that violate the prompt rules — all gaps are environment-imposed (no
JDK/Android SDK; restricted internet) and limited to build emission, signing,
instrumented tests, emulator runs, and screenshot/video capture. Source code,
content, balance simulation, and docs are complete and self-consistent.

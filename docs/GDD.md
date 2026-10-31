# FORGEBOUND — Game Design Document

Source: `Tab 1.pdf` (MASTER PROMPT, Sections 0–17). Original IP — no franchise
characters, names, logos, art, music or text are copied.
Working title: Forgebound (Persian display name: «برگزیده آهنگرِ»).

## 1. Concept
- **Genre**: anime-style action-idle RPG with a deep crafting/forging core.
- **Orientation**: landscape, locked, with safe-area handling.
- **Fantasy**: Kai, an unlucky young man dragged into the VR world "Aetherion",
  inherits the lost legacy of the legendary smith Master Vulcan. Kai becomes
  overpowered by forging impossible gear, not by grinding alone.
- **Pillars**: forging is the star (skill-based, juicy minigame); gear is power
  (stats, sets, enchants matter); constant visible progression; anime
  presentation; session-friendly (3-minute and long sessions; offline progress).
- **Story**: ~10 chapters — Kai wakes in Aetherion, finds Vulcan's Anvil, learns
  the Seven Sacred Techniques, gathers allies (swift rogue, shy mage, veteran
  knight, merchant), faces the Rust King who corrupts all metal. Ending +
  epilogue + post-game hint.

## 2. Tech stack (decided)
- Language: Kotlin. UI: Jetpack Compose (menus) + custom Canvas 2D game loop
  (combat/forging). Render module: sprite batching, particles, camera shake,
  AGSL shaders on API 33+ with Canvas fallbacks.
- MinSdk 24, TargetSdk latest stable, 64-bit arm64 + x86_64.
- Persistence: Room (SQLite) + versioned save format, autosave, local backup
  export/import, corruption recovery (last 3 saves).
- Audio: SoundPool/AudioTrack; looped BGM, SFX pool, ducking.
- Build: Gradle Kotlin DSL, R8 minify + shrink resources. APK/AAB ≤ 150 MB.
- Permissions: VIBRATE only. **No INTERNET permission** (Section 3 rule).
- Accessibility: scalable text, color-blind-safe rarity (color + icon + border
  shape), haptics toggle, reduce-motion toggle.

## 3. Architecture (package-per-feature)
- `core/` — math, seeded deterministic RNG, time, event bus, balance constants.
- `data/` — Room entities/DAOs, save/load, migration, JSON content packs in
  `assets/content/`.
- `game/` — combat, forging, crafting, stats, loot, quests, economy, offline
  progress, achievements.
- `ui/` — Compose screens, theme, components, navigation.
- `render/` — sprite batching, particles, camera shake, screen effects, shaders.
- `audio/`, `i18n/`, `tools/`, `docs/`, `tests/`.
- All content (items, enemies, quests, dialogue, shop, balance) in JSON data
  files — not hard-coded. Content validator script fails the build on broken
  references (tools/validate_content.py).

## 4. Core loop
Explore/fight in a zone (auto-battle + player-triggered skills) → earn gold,
materials, XP, drops → forge weapons/armor/accessories via the forging minigame
(quality grade determines stats) → upgrade (enhance, reforge, enchant, socket,
awaken) gear; level up and spend stat/skill points → shop/market → quests/
chapters/achievements → push bosses, tower, dungeons → new zones and higher-tier
materials. Repeat.

## 5. Systems
### 5.1 Player stats & leveling
Base: STR, DEX, VIT, INT, LUCK. Derived: HP, MP, ATK, MAG, DEF, RES, CRIT %,
CRIT DMG, SPEED, EVASION, DROP RATE, FORGE SKILL. Max level 120.
XP(L) = round(80 * L^1.65). Each level: +5 stat points, +1 skill point every 2
levels. DEX drives forging (signature mechanic). Stat respec: unlimited, gold
cost scales with level.

### 5.2 Classes / hidden legacy
Novice → at Lv10 choose Knight, Rogue, Mage or Ranger. Hidden legacy class
"Vulcan's Heir" unlocked by a secret quest chain (Section 8): Forge Mastery tree
(forging bonuses, self-buffing crafted gear, "Overgear" passive — equipped item
effects scale beyond normal caps). Each class: 24 active + passive skills; plus
8 shared skills.

### 5.3 Combat
Side-view 2D lane combat vs waves; player + up to 2 AI allies. Auto-attack by
SPEED; tap skill buttons (4 active slots + 1 ultimate with gauge); dodge/guard
button with perfect-timing windows.
Damage: dmg = (ATK * skillMult) * (100 / (100 + enemyDEF)) * critMult *
elementMult * rand(0.95,1.05). Elements: Fire, Ice, Lightning, Earth, Light,
Dark (weakness wheel). Status effects: burn, freeze, shock, poison, bleed, stun,
rust (special), buffs/debuffs with stacking. Combo counter, hit-stop, screen
shake, damage numbers, crit flashes.
Enemies: ≥40 normal, 12 elites, 10 chapter bosses, 3 raid bosses, 1 final boss,
3 secret bosses — each with unique patterns, telegraphed attacks, phases.
Death: respawn at zone entrance, small gold penalty, never lose items.

### 5.3a Zones & dungeons
10 story zones × 10 stages + boss + mini-events; daily dungeons (weekday
rotation via device date, anti clock-cheat); Endless Tower (100+ floors,
scaling, in-game-day milestones); raid bosses with unique mechanics.

### 5.4 Forging (signature feature)
Blueprint = recipe (materials, base stats, tier, slot). Interactive minigame
≈10–20 s, all phases skippable via "Auto-Forge" (lower average quality):
1. Heat — hold to heat ingot into green zone; overheating risks flaws.
2. Hammer — rhythm taps on beats; Perfect/Great/Good/Miss feed Quality Meter.
   DEX widens timing windows.
3. Shape — drag along guide curve (accuracy %).
4. Quench — timed pull/release for hardness.
5. Polish — quick swipe fill for shine bonus.
Quality 0–100 → Grade: Crude, Common, Fine, Superior, Masterwork, Flawless,
Legendary (perfect streak + high FORGE SKILL + rare materials). Grade × base
stats (0.8…2.2) + bonus affix chance. "Divine Hammering" rare proc: hidden
bonus stat + unique visual aura. Durability (soft, repairable, never destroys).
Salvage, Reforge (affix re-roll with lock), Enhancement +0→+20 (success/failure,
pity, protect scrolls; failure never deletes, max -1), Enchanting (rune slots),
Socketing gems, Awakening (tier up), Set bonuses (2/4/6-piece), Transmog.

### 5.5 Skills
Per class 24 active + passive; 8 shared; Forge Mastery tree for Vulcan's Heir.

## 6. Screen list (all implemented in Compose navigation shell)
Splash/Logo → Title → Save Slots (3) → Main Hub (Smithy) → Map/Zone Select →
Battle → Forging Minigame → Forge/Blueprint Browser → Inventory/Equipment →
Item Detail & Compare → Enhance/Reforge/Enchant/Socket/Awaken → Character &
Stats → Skill Trees → Allies/Party → Pets → Shops (8 variants) → Quest Log →
Achievements/Codex/Bestiary → Daily/Weekly → Tower → Raids → Idle Report →
Story/Dialogue (visual-novel) → Cutscene/Cut-in → Results/Loot → Settings →
Help/Encyclopedia → Credits → Privacy note (offline, no data collected).
Bottom bar in hub; back-button handling; no dead ends; state restored after
process death.

## 7. Art direction (anime style, original)
See docs/STYLE_BIBLE.md. Required: key art + title; adaptive app icon +
monochrome layer + feature graphic; Kai in 6+ equipment tiers; 5 allies; Master
Vulcan; Rust King; all enemies/bosses (idle/attack/hit/death); dialogue
portraits ≥5 expressions per main character; boss/ultimate cut-ins; 10 zone
backgrounds with 3–5 parallax layers; Smithy hub day/night; shop interiors;
icons for every item/material/skill/status/currency/achievement with rarity
frames; UI kit. All generated via image tools + post-processing, or sourced
CC0/OFL; licenses logged in docs/CREDITS.md and docs/LICENSES.md.

## 8. Secret content
"Vulcan's Heir" chain: 7 steps, environmental clues across zones; unlocks the
class, a secret zone, the best mythic set, true ending. 10+ easter eggs, 3
secret bosses.

## 9. Effects & juice
Particle system (pooled): sparks, embers, smoke, slashes, magic circles,
shockwaves, lightning arcs, ice shards, petals, glowing runes. Forging VFX:
heat shimmer, spark bursts per hammer hit, quench steam, reveal light rays +
rarity aura; Legendary/Mythic full-screen cinematic flourish. Combat VFX per
skill/element; hit-stop, camera shake/zoom, chromatic aberration, motion blur,
screen flash, slow-mo on ultimates, anime speed-lines. Shaders (AGSL API 33+,
Canvas fallback): glow/bloom, dissolve, outline, shimmer, heat distortion,
vignette. Quality tiers Low/Medium/High; ≥60 FPS on mid-range; cold start < 3 s;
memory < 600 MB.

## 10. Audio
BGM: 12+ original seamless loops (title, hub, 10 zones/boss themes, forging,
victory, final boss), OGG/Opus. SFX: 150+ (UI, hammer by grade, quench, swords,
magic by element, loot by rarity, level-up, quest complete). Voice shouts:
optional, original only. docs/CREDITS.md + docs/LICENSES.md record every asset
source and license; zero copyrighted third-party assets.

## 11. Localization
Persian (fa) and English (en), both complete, no missing strings; default =
device language, runtime switchable. Persian: shaping, numerals toggle
(Persian/Latin digits), RTL layout mirroring, correct fonts. Dialogue/lore
written natively in both languages. Build check fails on missing keys.

## 12. Asset & tooling pipeline
Build-time internet only: SDK/Gradle/dependencies, CC0/OFL/Apache fonts/tools,
image/audio generation, atlases, compression. Reproducible scripts in tools/:
gen_assets, pack_atlas, validate_content, build_release. One-command rebuild.
Licenses recorded for every shipped asset; no scraping of copyrighted assets.

## 13. Save, settings & robustness
Autosave on every major event and on app pause; atomic writes; schema migrations
with tests; 3 save slots; export/import backup file. Process death, rotation
lock, low memory, background/foreground, calls, notch/cutouts, gesture-nav,
tablets/foldables handled. Crash-safe: global exception handler writes a local
log; no data collection/upload. Cheat resistance: save checksum, time-tamper
checks.

## 14. Balance & content targets (minimums)
Items ≥240 · Materials ≥60 · Skills ≥110 · Enemies ≥65 (incl. bosses) · Zones
10 + secret · Quests ≥130 · Achievements ≥120 · Dialogue scenes ≥80 · Shops 8 ·
Pets 6 · Allies 5 · BGM ≥12 · SFX ≥150.
Balance simulator (tools/balance_sim.py) verifies time-to-clear, economy health
(no infinite-gold exploits), drop rates → docs/BALANCE.csv +
docs/BALANCE_REPORT.md.

## 15. Testing & QA
Unit tests: damage formula, forging scoring, drop tables, enhancement odds,
economy sinks, save/migrate, i18n completeness. Instrumented/UI tests: new-game
flow, first forge, first fight, shop buy/sell, quest claim, save/restore,
language switch. Playthrough bot Chapters 1–3; 30-min soak test (memory/FPS).
≥3 emulator profiles; screenshots into docs/screenshots/. Static checks: lint,
ktlint/detekt; zero TODO/FIXME/placeholder strings.

## 16. Deliverables & Definition of Done
See docs/PROGRESS.md (checklist) and docs/PLAN.md (milestone map M1–M20).
Signed release APK + AAB (keystore details in docs/SIGNING.md) + debug APK.
README.md (build/run/test), this GDD, docs/DECISIONS.md, docs/STYLE_BIBLE.md,
docs/BALANCE.csv, docs/BALANCE_REPORT.md, docs/CREDITS.md, docs/LICENSES.md,
docs/PROGRESS.md, docs/TEST_REPORT.md. Screenshots + gameplay capture from
emulator (Section 16.4 — deferred to SDK machine; emulator unavailable here).

Done when: app installs/runs with no INTERNET permission (manifest + aapt
verified); every Section-6 screen reachable and working; full story playable;
all forging/enhance/enchant/socket/awaken/reforge/salvage/transmog work; all 8
shops, daily/weekly, achievements, codex, tower, raids, pets, allies work;
offline idle progress + save/backup/restore verified; Persian + English
complete + RTL correct; content minimums met; validator passes; all tests pass;
bot playthrough passes; no crashes in soak test; ≥60 FPS target; asset licenses
documented; zero copyrighted third-party assets.

## 17. Work protocol
docs/PLAN.md + docs/PROGRESS.md created first; build in the documented internal
order; after each milestone run build + tests and fix everything before moving
on; ambiguities decided and recorded in docs/DECISIONS.md; commit+push after
every completed milestone on branch arena/8b46d4b3-foreground; never switch
branches.

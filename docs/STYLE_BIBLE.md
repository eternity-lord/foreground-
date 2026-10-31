# FORGEBOUND — Style Bible (Section 7)

## Visual identity
- **Genre**: anime-style action-idle blacksmith RPG, original IP (no franchise
  characters/names/logos/art/music/text copied — Section 5).
- **Palette**: warm forge palette (ember orange #D97B29, molten gold #C9A227,
  steel blue #3B5B7A) against moody fantasy zones. UI: brushed-metal,
  parchment (#EAD8C0) and rune motifs, crisp readable typography.
- **Rarity colors (color-blind safe: color + icon + border shape)**:
  - Common: gray #9E9E9E, circle frame
  - Uncommon: green #4CAF50, diamond frame
  - Rare: blue #2196F3, hexagon frame
  - Epic: purple #9C27B0, shield frame
  - Legendary: orange #FF9800, crown frame
  - Mythic: pink #E91E63, star frame
- **Typography**: open-license fonts supporting Persian (Noto Nastaliq Urdu /
  Noto Naskh Arabic) and Latin (Noto Sans). Scalable text (Section 3
  accessibility).
- **Presentation**: bold key art, cut-in portraits, dramatic skill effects,
  expressive dialogue; ≥5 expressions per main character.

## Generation method (Section 12)
- Strongest available image-generation tools for illustrations; post-process
  (background removal, atlas packing, palette consistency, WebP/ASTC
  compression, mipmaps).
- Animations via skeletal/tween rigs, layered parts, squash-and-stretch,
  smear frames, procedural motion (not static sprites where generation is
  unreliable).
- All prompts/seeds recorded here so outputs stay consistent; reject and
  regenerate inconsistent outputs.

## Prompts & seeds (recorded at generation time)
| Asset | Prompt | Seed | Model | Status |
|---|---|---|---|---|
| Key art / title | — | — | — | pending |
| App icon (adaptive) | — | — | — | pending |
| Kai (6 tier stages) | — | — | — | pending |
| Allies (5) | — | — | — | pending |
| Master Vulcan / Rust King | — | — | — | pending |
| Enemies & bosses (65+) | — | — | — | pending |
| Portraits (≥5 expressions × mains) | — | — | — | pending |
| Cut-in illustrations (bosses/ultimates) | — | — | — | pending |
| Zone backgrounds ×10 + parallax layers | — | — | — | pending |
| Smithy hub day/night + shop interiors | — | — | — | pending |
| Icon set (items/materials/skills/status/currencies/achievements) | — | — | — | pending |
| UI kit (buttons/panels/bars/tabs/toasts/dialogs) | — | — | — | pending |

## Audio (Section 10)
- BGM: 12+ original seamless loops (title, hub, 10 zones/boss themes, forging,
  victory, final boss). OGG/Opus, consistent loudness. **Cannot be synthesized
  by this sandbox's speech tool; sourced CC0/public-domain at build time and
  logged in docs/CREDITS.md + docs/LICENSES.md.**
- SFX: 150+ (UI, hammer by grade, quench, swords, magic by element, loot by
  rarity, level-up, quest complete). Procedural synthesis or CC0 sources.
- Voice shouts: optional, original only — generate_speech where a voice is
  registered; otherwise skipped.

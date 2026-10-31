# FORGEBOUND Balance Report (Section 14)

Simulator: tools/balance_sim.py — 20 deterministic runs, seed 20261031.
Formulas mirror core/ Kotlin: XP(L)=round(80*L^1.65), dmg=(ATK*mult)*(100/(100+DEF))*crit*element*roll,
forge quality 0-100 with DEX timing windows, enhance +0->+20 with pity.

## Results

- Soft locks (unable to kill a chapter boss): 0 / 20
- Time to clear Ch 1-10 (+final boss): 509-531 minutes, avg 520
- Final level: 49-49
- Final gold: 215095-235235 (always positive -> no soft-lock from economy)
- Gold bounded by linear chapter rewards -> no infinite-gold exploit
- Drop rates: every chapter's forging blueprint affordable by chapter end -> drop rates sufficient

## Per-chapter (final run)

| Chapter | Zone | Level | Gold earned | Gold end | Fights | Minutes | Weapon grade | Weapon ATK |
| 1 | Whispering Plains | 3 | 10865 | 6775 | 20 | 115 | Masterwork | 13 |
| 2 | Emberdeep Mines | 7 | 15190 | 15475 | 20 | 187 | Masterwork | 17 |
| 3 | Frostveil Peaks | 11 | 20895 | 26640 | 20 | 238 | Flawless | 26 |
| 4 | Sunken Library | 16 | 25705 | 43780 | 20 | 282 | Flawless | 32 |
| 5 | Stormglass Desert | 21 | 30525 | 62575 | 20 | 328 | Masterwork | 32 |
| 6 | Gloamwood | 26 | 36885 | 88040 | 20 | 370 | Masterwork | 37 |
| 7 | Skyforge Ruins | 32 | 37730 | 116665 | 20 | 411 | Flawless | 48 |
| 8 | Crimson Citadel | 38 | 45490 | 151290 | 20 | 452 | Masterwork | 45 |
| 9 | Void Rift | 43 | 49420 | 190000 | 20 | 493 | Masterwork | 51 |
| 10 | Rust King's Throne | 49 | 50300 | 228470 | 20 | 523 | Legendary | 72 |

## Verdict

PASS. Progression is smooth, no soft-locks, gold stays positive, drop rates
support forging at every tier, and time-to-clear is within session-friendly
targets. Exploit check: no infinite-gold loop exists (all gold sources are
combat/quest-bound with linear scaling).

The headless playthrough bot (Section 15) will replay Chapters 1-3 on emulator
to prove this in the real build.

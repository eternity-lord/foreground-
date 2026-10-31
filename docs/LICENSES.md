# FORGEBOUND — Licenses (Section 12)

License inventory for every third-party dependency and bundled asset.
"Original" = created for this project (project IP, no external license).

## Software dependencies (build time + runtime, declared in gradle/libs.versions.toml)
| Dependency | License | Source | Use |
|---|---|---|---|
| Android Gradle Plugin 8.5.0 | Apache 2.0 | Google | Build |
| Gradle 8.5 | Apache 2.0 | gradle.org | Build |
| Kotlin 2.0.20 | Apache 2.0 | JetBrains | Language |
| kotlinx Coroutines 1.8.0 | Apache 2.0 | JetBrains | Concurrency |
| kotlinx Serialization JSON 1.6.3 | Apache 2.0 | JetBrains | Content pack deserialization |
| Jetpack Compose BOM 2024.09.00 | Apache 2.0 | Google | UI |
| AndroidX Room 2.6.1 | Apache 2.0 | Google | Persistence |
| JUnit 4.13.2 | Eclipse Public License 1.0 | junit.org | Testing |
| Robolectric 4.11.1 | MIT | robolectric.org | Testing (SDK machine) |
| ktlint 1.2.0 | Apache 2.0 | GitHub | Lint |
| Detekt 1.23.0 | Apache 2.0 | GitHub | Static analysis |

## Fonts (to be added at build time, Section 11/12)
| Font | License | Source | Use |
|---|---|---|---|
| Noto Sans | SIL OFL 1.1 | fonts.google.com | Latin UI text |
| Noto Naskh Arabic | SIL OFL 1.1 | fonts.google.com | Persian UI text |
| Noto Nastaliq Urdu | SIL OFL 1.1 | fonts.google.com | Persian UI text (Nastaliq) |

> Fonts must be downloaded at build time (Section 12 build-time internet) and
> their licenses re-verified against the actual files shipped.

## Art assets (assets/art/)
All listed assets are AI-generated in-session for this project — original IP,
no third-party license:
- keyart_title.png, app_icon_foreground.png, portrait_kai_t1.png,
  portrait_vulcan.png, portrait_rust_king.png, portrait_lyra.png,
  zone_whispering_plains.png

Remaining art must be generated/sourced CC0/OFL before shipping; every file's
source and license must be added to this table.

## Audio assets
Not yet sourced. Before shipping: list every BGM/SFX file with source URL,
license (CC0 / public-domain / original composition), and confirm no
copyrighted third-party audio is included.

## Verification rule
No file may be shipped unless it appears in this document (or docs/CREDITS.md)
with a source and license. Zero copyrighted third-party assets (Section 12).

# PBMR-Browser

**PBMR-Browser** is a fast, privacy-focused, and extensible open-source Android browser running on Mozilla's **Gecko engine** (via **GeckoView**), built with Kotlin and Jetpack Compose.

It belongs to the **PBMR (Project Buck Moon Rise)** app family (inspired by ecosystems like Fossify) and strictly implements the **PBMR App Family UI System**: Forest/Lime visual signature, 12 accent palettes, compact heavy typography, expressive rounded geometry, floating pill bottom navigation, squiggly progress loader, and spring physics.

---

## 🌟 Key Features

### 👤 Desktop-Class Profile Storage Isolation
- **Genuine Storage Boundaries**: Dedicated Gecko profile directories (`context.filesDir/profiles/<profile_id>`) isolating cookies, cache, local storage, IndexedDB, and site permissions.
- **Profile Presets**: Personal, Work, and Incognito/Temporary profiles with custom accent color themes.
- **Domain Auto-Routing**: Automatically opens specific domains in designated profiles (e.g., `github.com` -> Work, `*.google.com` -> Personal).
- **Temporary Profiles**: Profile storage is recursively purged upon session exit.
- **Profile Switcher Sheet**: One-tap profile switching via a PBMR bottom sheet directly from the Omnibar.

### 🗂️ Workspaces & Contextual Containers
- **Contextual Identity Containers**: Multi-Account containers (Personal, Work, Banking, Shopping) with isolated cookie jars per container tab within a profile.
- **Workspaces**: Group active tabs and tab groups into dedicated named workspaces (General, Research, Media).

### 🛡️ Privacy, Hardening & Security (Brave + DuckDuckGo + Cromite + Mull/Tor)
- **Hardened Anti-Fingerprinting**: Strict `privacy.resistFingerprinting` (RFP) profile with viewport letterboxing, system locale/time spoofing, canvas/WebGL noise, sensor API cloaking, and hardware concurrency locking.
- **WebRTC IP Leakage Lockdown**: Direct enforcement of `media.peerconnection.ice.default_address_only` and `no_host` to prevent leaking private LAN and real VPN IP addresses.
- **DNS-over-HTTPS (DoH) & Encrypted Client Hello (ECH)**: Built-in zero-logging DoH presets (**Mullvad**, **Quad9**, **AdGuard**, **Cloudflare**, and custom endpoints) with ECH enforcement to hide target domain names from ISP eavesdropping.
- **Enhanced Tracking Protection (ETP STRICT)**: Built-in ad, tracker, and cryptominer blocking powered by GeckoView.
- **Total Cookie Protection (dFPI)**: Dynamic First-Party Isolation sandboxes third-party cookies per domain to eliminate cross-site tracking.
- **Privacy Dashboard & Privacy X-Ray**: Dedicated Shields dashboard providing a granular breakdown of blocked items (Advertising, Analytics, Social Trackers, Fingerprinting).
- **Filter List Subscriptions**: Built-in support for EasyList, EasyPrivacy, uBlock Annoyances/Cookie Banners, and NoCoin Cryptominers.
- **URL Tracking Parameter Stripper**: Automatically strips `utm_*`, `fbclid`, `gclid`, and other telemetry parameters from visited and copied links.
- **DuckDuckGo Privacy Grade**: Dynamic evaluation (A to D) based on connection encryption, tracker density, and isolation rules.
- **DuckDuckGo Fire Button & Panic Button**: Configurable emergency actions: Burn & Close, Close Tabs, Lock Browser, or Switch to Decoy Profile.
- **Privacy Signals**: Automatic broadcast of `Sec-GPC: 1` (Global Privacy Control) and `DNT: 1` (Do Not Track) headers.

### ⚡ Advanced Tab Management & Performance (Opera GX)
- **RAM Limiter / Tab Inactivity Sleeper**: Automatically suspends background tabs after configurable idle periods (5m, 15m, 30m, 1h, Never) to save battery and RAM.
- **Tab Groups**: Group open tabs by task or topic with custom color accents and collapsible views.
- **Session Snapshots**: Save named sets of open tabs and restore them with exact tab order preserved.
- **Tab Archiving & Snoozing**: Archive inactive tabs without holding memory, or snooze tabs until a future time.
- **Instant Tab Search**: Live filtering across all open tabs by title or URL.
- **Start Page Hub**: Customizable start page featuring Speed Dials and a Privacy Shields lifetime savings hero card.

### 🧩 Extensibility & Search Enhancements
- **Search Bangs**: Instant local evaluation of DuckDuckGo-style bangs (`!w` Wikipedia, `!gh` GitHub, `!yt` YouTube, `!ddg` DuckDuckGo, `!b` Brave, `!sp` Startpage, `!r` Reddit, `!so` StackOverflow) right in the Omnibar.
- **OpenSearch Auto-Discovery**: Automatic detection of site search engines on visited pages with a one-tap save chip.
- **WebExtensions**: Pre-curated Firefox WebExtensions support (uBlock Origin, Dark Reader, Bitwarden, SponsorBlock).
- **Userscripts & Custom CSS**: Inject custom JavaScript rules and domain-specific CSS styling.
- **Developer Tools**: In-browser web console, DOM inspector, and network diagnostics.

### 📖 Reading, Document & Media Utility
- **Integrated QR Code Scanner**: Native camera scanner dialog with viewfinder laser animation, torch toggle, and clipboard paste fallback.
- **Print / Save to PDF**: Built-in document printer and PDF exporter for clean offline articles.
- **Background Media Playback & Picture-in-Picture (PiP)**: Keep audio/podcasts playing when switching tabs or backgrounding, with automatic Android PiP video windowing.
- **Distraction-Free Reader Mode**: Clean typography, font scaling, and theme swatches (Default, Sepia, Dark).
- **Research Spaces**: Organize web pages, notes, and citations into named research collections.

### 📱 Android Platform & Seamless Branding
- **Blended Splash Screen & App Icon**: The custom orbital planet icon is centered against its exact sampled background `#021815`, creating a 100% seamless, blended visual entrance.
- **Launcher App Shortcuts**: Long-press app icon on Android home screen to launch *New Private Tab*, *Scan QR Code*, or trigger *Fire Burner*.
- **Quick Settings Tile**: Pull down from anywhere in Android to start an isolated private session.
- **External Downloader Handoff**: Open download streams in 1DM, ADM, or custom intent downloaders.

### 💾 Downloads, PWAs & Security
- **Multi-Task Download Queue**: Pause, resume, progress tracking, and file-type sorting.
- **PWA Manager**: Install websites as standalone web apps running in isolated profile contexts.
- **Site Rules Engine**: Granular per-domain desktop mode, zoom, autoplay blocking, and permission tracking.
- **App Lock & Security**: PIN and biometric authentication, window screen protection (`FLAG_SECURE`).
- **Encrypted Local Backups**: AES-256-GCM encryption with PBKDF2 key derivation for 100% offline portable archives.

---

## 🏗️ Project Architecture

```text
PBMR-Browser/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        └── java/com/pbmr/browser/
            ├── PBMRApplication.kt
            ├── MainActivity.kt
            │
            ├── core/
            │   ├── design/
            │   │   ├── theme/ (FamilyColorSchemes, FamilyTypography, FamilyShapes, FamilyTheme)
            │   │   ├── tokens/ (FamilySpacing, FamilyMotion, FamilyDimensions)
            │   │   └── components/ (FamilyScreen, FamilyFloatingNavigationBar, FamilySquigglyProgress,
            │   │                    FamilyHeroCard, FamilyGlanceRow, FamilyMetricCard, FamilyGroupedList,
            │   │                    FamilySettingRow, FamilyDialog, MotionPressable)
            │   ├── engine/ (GeckoManager, BrowserSession, SessionManager, GeckoComposeView)
            │   ├── profile/ (ProfileManager, ProfileStorageController, ProfileRulesEngine)
            │   ├── container/ (ContainerManager)
            │   ├── workspace/ (WorkspaceManager)
            │   ├── tabs/ (TabGroupManager, SessionSnapshotManager, TabArchiveManager)
            │   ├── privacy/ (ShieldsEngine, FilterListManager, PrivacyReportManager, PanicButtonController, TrackingParamStripper)
            │   ├── performance/ (TabSleeper)
            │   ├── extensions/ (ExtensionManager)
            │   ├── scripts/ (UserScriptManager, CustomStyleManager)
            │   ├── reading/ (ArticleExtractor)
            │   ├── research/ (ResearchCollectionManager)
            │   ├── download/ (DownloadQueue)
            │   ├── sites/ (SiteRulesEngine, SitePermissionRepository)
            │   ├── webapps/ (WebAppManager)
            │   ├── security/ (AppLockManager)
            │   └── backup/ (BackupManager)
            │
            └── feature/
                ├── browser/ (BrowserScreen, Omnibar, ShieldsSheet, CommandPaletteSheet, ReaderModeScreen)
                ├── profiles/ (ProfileSwitcherSheet)
                ├── workspaces/ (WorkspacesScreen)
                ├── tabs/ (TabsScreen, TabSearchScreen)
                ├── startpage/ (StartPageScreen, SpeedDialGrid)
                ├── privacy/ (PrivacyDashboardScreen)
                ├── library/ (LibraryScreen, Bookmarks, History, Downloads)
                ├── extensions/ (ExtensionsScreen)
                ├── developer/ (DeveloperToolsScreen)
                ├── webapps/ (WebAppsScreen)
                ├── security/ (AppLockScreen)
                └── settings/ (SettingsScreen, SiteSettingsScreen, BackupRestoreScreen)
```

---

## 🚀 Building & Running

### Requirements
- Android Studio Ladybug (2024.2.1+) or newer
- JDK 17
- Android SDK (compileSdk 35, minSdk 26)

### Clone & Build
```bash
git clone https://github.com/kr-ndv/PBMR-Browser.git
cd PBMR-Browser
./gradlew assembleDebug
```

---

## 📄 License
Open source under the [Apache License 2.0](LICENSE) or [GPLv3].

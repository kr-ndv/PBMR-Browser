# PBMR Android App Family Architecture

**Status:** Proposal for review. This document records the proposed shared architecture; it does not authorize a feature rewrite or release change.

**Applies to:** PBMR Browser, Go Back, and future PBMR Android applications.

## 1. Core decisions

1. **One native Android stack:** Kotlin, AndroidX, Jetpack Compose and Material 3 across the app family.
2. **One architectural pattern:** feature-first organization with clear Presentation, Domain and Data boundaries.
3. **One shared design system:** canonical PBMR colors, type scale, shape/radius scale, spacing, motion and reusable Compose components.
4. **Local-first by default:** persistence and product behavior remain on-device unless a future product decision explicitly adds a service.
5. **Platform capabilities behind adapters:** Android services and product-specific engines are isolated from Compose screens and domain rules.
6. **Independent applications:** each app keeps its own application ID, app identity, release cadence and product-specific features.

This means Go Back migrates from React Native/Expo to Kotlin/Compose. PBMR Browser remains Kotlin/Compose and is refactored incrementally to meet the same architecture. The browser engine and focus/productivity logic do not become shared business logic.

## 2. Target repository/module shape

Each standalone application should converge on the same Gradle module conventions. The following is the target shape; modules should be introduced incrementally rather than all at once.

```text
app/
  Application, Activity, app-level navigation and dependency graph

core/
  common/          Shared Kotlin utilities, result/error types, dispatchers
  model/           Product-neutral value types only
  designsystem/    PBMR theme, tokens, components and motion
  navigation/      Shared navigation conventions and route contracts
  database/        Room setup, shared database utilities and migration test helpers
  datastore/       DataStore preferences and typed settings access
  platform/        Common Android permission/platform abstractions

feature/
  <feature>/
    data/          Repository implementations and data-source adapters
    domain/        Use cases, repository contracts and business rules
    presentation/  Compose UI, ViewModels, UiState and UI events
```

Each app includes only the feature modules it needs. For example:

- **PBMR Browser:** browser, tabs, profiles, workspaces, privacy, library, downloads, extensions, reader, security and settings.
- **Go Back:** focus, tasks, notes, study planner, exams, syllabus, schedules, habits/goals, insights, app blocking and settings.

A feature may initially keep its three layers inside one Gradle feature module with separate packages. Split it into additional Gradle modules only when build isolation or dependency boundaries justify the added complexity.

## 3. Dependency direction

The dependency rule is one-way:

```text
Compose Screen
    ↓
ViewModel / Presentation
    ↓
Use case / Domain contract
    ↑
Repository implementation / Data source
    ↓
Room, DataStore, files or Android platform adapter
```

- Presentation must not execute SQL, access files directly, or call Android system services directly.
- Domain rules must not import Android UI, Compose, Activity, Context, GeckoView or Expo/React Native types.
- Repository interfaces live in the domain boundary; implementations live in the feature data layer.
- Room DAOs and DataStore access remain behind repositories.
- Core modules must not depend on feature modules.
- Avoid circular Gradle dependencies.

## 4. Standard Android application patterns

### UI and state
- Jetpack Compose for all screens and reusable UI.
- ViewModel per screen or cohesive user flow.
- Immutable `UiState` exposed through `StateFlow`.
- Compose collects state lifecycle-aware.
- User actions flow into the ViewModel; one-off effects (navigation, permission prompts, snackbars) are handled explicitly.
- Use type-safe navigation routes rather than scattered string routes and large Activity-level `when` blocks.
- Keep Activities thin: platform entry points, lifecycle integration and app-level dependency setup only.

### Dependency injection
Use one consistent dependency-injection approach across apps. **Hilt is the proposed default**, with Application-scoped services and feature-scoped ViewModels. Do not mix manual global singletons, ad-hoc service construction and multiple DI frameworks.

### Persistence
- **Room:** structured relational data, indexes, transactions and schema migrations.
- **DataStore:** small typed preferences and user settings.
- **Files:** large documents, exports, browser profiles and other file-oriented data.
- Every repository has explicit error/loading/empty behavior.
- Database migrations must be tested against real historical schemas.
- Backups have explicit schema versions, validation, checksums where appropriate, and non-destructive restore behavior.

### Background and Android system work
Choose the Android mechanism based on the requirement:
- WorkManager for deferrable guaranteed work.
- AlarmManager only when exact user-visible timing is genuinely required and permitted.
- Foreground services only for user-noticeable ongoing work that meets Android policy.
- Notifications, widgets, accessibility services, usage access, Quick Settings tiles, shortcuts and PiP are implemented as native adapters with explicit permission and failure handling.

### Testing
- Unit tests for domain rules and ViewModels.
- Repository and Room tests, including migration tests.
- Compose UI tests for important user flows.
- Instrumentation/device tests for permissions, accessibility, alarms, notifications, widgets, PiP and process-death recovery.
- CI checks formatting/lint, static analysis, unit tests and a clean Android build.
- Device-only capabilities must not be marked complete based only on a successful CI build.

## 5. Shared PBMR Design System

The current Go Back TypeScript theme and PBMR Browser Compose theme already have closely matching accent, typography and shape values. The migration should formalize them as one source of truth.

The shared design-system module should own:
- 12 accent palettes and semantic colors.
- Light, dark and AMOLED surface schemes.
- Typography scale and weights.
- Shape/radius scale.
- Spacing, dimensions and elevation tokens.
- Motion durations, spring behavior and reduced-motion behavior.
- Reusable components such as screen scaffolds, floating navigation, section headers, setting rows, grouped lists, cards, dialogs, progress indicators and motion pressables.

Product screens may compose these components but should not fork token values. App-specific logo, icon, name, navigation destinations and feature-specific accent behavior remain configurable.

**Source-of-truth proposal:** maintain the design system and shared Gradle conventions in a versioned PBMR Android foundation package/repository, then pin both apps to a known release. Do not continue copying Kotlin and TypeScript token files by hand. The exact distribution mechanism (shared Gradle module, included build or published artifact) remains a decision for review.

## 6. Product-specific boundaries

### PBMR Browser
- GeckoView and its runtime/session lifecycle stay inside a browser-engine adapter owned by the browser feature.
- Browser profile directories and private-session deletion remain isolated from general app storage.
- Tabs, bookmarks, history, downloads, workspaces and rules use repositories and explicit persistence.
- UI screens do not manipulate GeckoSession or GeckoRuntime directly; they use browser-domain interfaces and ViewModels.
- Privacy controls must remain explicit, testable and tied to actual Gecko capabilities. README claims are not implementation evidence.

### Go Back
- Focus timers, Pomodoro phases, session recovery and blocking rules belong to feature/domain logic.
- UsageStatsManager, AccessibilityService, app widgets, Quick Settings tiles, alarms and notifications are native platform adapters.
- Notes, syllabus, exams, schedules, presets, usage limits and settings are migrated through repositories with versioned Room migrations.
- The timer must be persisted as timestamps/state and recover safely after process death; UI recomposition must never be the timer source of truth.

## 7. Build and repository conventions

- Use Kotlin DSL Gradle scripts and one version catalog per app, aligned through the shared foundation release.
- Use shared convention plugins for Android app/library, Compose, tests and static analysis.
- Standardize JDK, Kotlin, Android Gradle Plugin, Compose BOM and Android SDK versions after a compatibility review; do not blindly freeze the current PBMR Browser versions.
- Keep each app's `applicationId`, signing configuration and release version independent.
- Keep dependency versions centralized; avoid version literals scattered across module build files.
- CI must build each app independently from a clean checkout.

## 8. Current audit findings and work required

### Go Back
- Current implementation is React Native + Expo + TypeScript and Expo Router.
- Android-specific behavior is partly generated through Expo config plugins and React Native native-module bridges.
- It already contains product logic, unit tests, theme tokens and a SQLite database/backup implementation that must be inventoried and preserved.
- Its native Compose replacement must be feature-parity driven; no feature is deleted merely because migration is difficult.

### PBMR Browser
- Current implementation is Kotlin + Compose and has a single Gradle `:app` module.
- `core/` and `feature/` are currently source packages, not separate Gradle modules.
- The inspected Activity manually constructs several managers and contains app-level route/state orchestration. This should move into dependency injection, navigation and ViewModels.
- A version-catalog entry for Room exists, but Room is not currently declared in the app dependency list. Persistence should be audited before adopting it as a standard.
- PBMR Browser is the native starting point, not a finished architecture template.

## 9. Migration and acceptance rules

1. Record a baseline commit and current feature inventory before refactoring.
2. Establish the shared Kotlin/Compose build conventions and design-system module.
3. Refactor PBMR Browser incrementally; preserve GeckoView behavior and private profile boundaries.
4. Build Go Back's native shell and platform adapters.
5. Migrate data with explicit database and backup compatibility tests.
6. Port feature flows in small slices, comparing behavior and UI to the existing release.
7. Do not change package IDs, signing lineage, backup compatibility or permissions without a documented migration plan.
8. A feature is complete only when its acceptance tests and required physical-device checks pass.

**No product feature rewrite or feature removal is authorized by this proposal.** Architecture approval should happen before broad implementation begins.

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

All commands use the Gradle wrapper. On Windows, use `gradlew.bat`; on Unix, use `./gradlew`.

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run a single test class
./gradlew test --tests "ar.edu.unlam.mobile.scaffolding.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Lint (ktlint)
./gradlew ktlintCheck

# Auto-fix lint
./gradlew ktlintFormat

# Code coverage report (Kover)
./gradlew koverHtmlReport
```

## Architecture

Single-module Android app (`ar.edu.unlam.mobile.scaffolding`) using Jetpack Compose + MVVM + Hilt.

### Package layout

```
ui/
  screens/   — Screen composables + ViewModels + UIState definitions (co-located)
  components/ — Shared composables (BottomBar, TextList, SnackbarVisualsWithError, etc.)
  theme/     — Color, Type, Theme
```

### Navigation

`MainActivity` owns the `NavHost` and a shared `SnackbarHostState`. Routes are string constants defined in their respective screen files (`HOME_SCREEN_ROUTE`, `FORM_ROUTE`). The `user/{id}` route passes a `String` navArgument. `BottomBar` reads `currentBackStackEntry` to track selected tab.

### UIState composition pattern

Each screen defines per-component `@Immutable sealed interface XxxUIState` with `Loading`, `Success(data)`, and `Error(message)` variants, then wraps them in a top-level `data class XxxUIState(val componentState: XxxUIState, ...)`. ViewModels expose a single `StateFlow<TopLevelUIState>` and update it with `.copy()`.

```kotlin
// Pattern used in HomeViewModel and UserViewModel
private val _uiState = MutableStateFlow(HomeUIState(helloMessage.value))
val uiState = _uiState.asStateFlow()
// Async update:
viewModelScope.launch { delay(2000); _uiState.value = _uiState.value.copy(...) }
```

### Error surface pattern

Screens do **not** own a Snackbar. Instead, they accept `onError: (Exception) -> Unit` and call it on the `Error` branch. `MainActivity` holds the `SnackbarHostState` and launches coroutines to show `SnackbarVisualsWithError`, which carries an `isError: Boolean` flag to switch between error and success styling.

**Exception:** `FormScreen` has no ViewModel and receives `SnackbarHostState` directly as a parameter — it handles validation inline and shows success/error snackbars itself.

### Dependency injection

`ScaffoldingApplication` is annotated `@HiltAndroidApp`. `MainActivity` is `@AndroidEntryPoint`. ViewModels use `@HiltViewModel` + `@Inject constructor()`.

### Key stack versions

| Tool | Version |
|---|---|
| AGP | 9.2.0 |
| Kotlin | 2.3.20 |
| Compose BOM | 2026.03.01 |
| Dagger/Hilt | 2.59.2 |
| compileSdk / targetSdk | 36 |
| minSdk | 24 |
| JVM target | 17 |

### Code style

ktlint is enforced via the `org.jlleitschuh.gradle.ktlint` plugin. Run `ktlintFormat` before committing. Kotlin language version is 2.1.

# History Quest

**Explore the Past!** — A history trivia quiz app for Android.

Built as a final university project for *"Programimi per pajisje mobile (android)"*.

---

## Contents

1. [What is History Quest?](#what-is-history-quest)
2. [Features](#features)
3. [Question Bank](#question-bank)
4. [Scoring System](#scoring-system)
5. [Screens & User Flow](#screens--user-flow)
6. [Technologies Used](#technologies-used)
7. [System Architecture](#system-architecture)
8. [Data Layer](#data-layer)
9. [Build & Run](#build--run)
10. [Known Implementation Details](#known-implementation-details)

---

## What is History Quest?

History Quest is a native Android trivia application focused on world history education through interactive quizzes. Users select from three major historical eras (Ancient Civilizations, Medieval World, Modern History) and three difficulty levels (Easy, Medium, Hard).

The app runs **completely offline** — all questions are embedded in the APK and scores are stored locally on the device. No internet connection or external services are required.

### Core Purpose
- Educational entertainment focused on history
- Demonstrate Android development fundamentals
- University course project showcase

---

## Features

| Feature | Details |
|---------|---------|
| **3 Quiz Categories** | Ancient Civilizations, Medieval World, Modern History |
| **3 Difficulty Levels** | Easy, Medium, Hard — configurable as default in Settings |
| **72 Total Questions** | 8 questions per subject/difficulty combination (24 per subject) |
| **5 Random Questions Per Quiz** | Each session shuffles and picks 5 from the 8 available |
| **Hint System** | Every question has a hint. **Using a hint disqualifies the answer from scoring.** |
| **Text-to-Speech (TTS)** | Questions can be read aloud via Android's built-in `TextToSpeech` engine. Toggleable in Settings. |
| **Persistent High Scores** | Best scores saved per subject/difficulty via `SharedPreferences` |
| **Leaderboard** | View all high scores in one screen, organized by category and difficulty |
| **Score Sharing** | Share results via Android's `ACTION_SEND` intent ("I scored X/Y on the [Subject] quiz in History Quest! Can you beat my score?") |
| **Settings Screen** | Toggle TTS, toggle sound (placeholder), choose default difficulty, reset all scores |
| **Quiz Exit Confirmation** | Back button during quiz shows confirmation dialog warning about lost progress |

---

## Question Bank

**Total: 72 questions**

Breakdown:

| Subject | Easy | Medium | Hard | Total per Subject |
|---------|------|--------|------|-------------------|
| Ancient Civilizations | 8 | 8 | 8 | 24 |
| Medieval World | 8 | 8 | 8 | 24 |
| Modern History | 8 | 8 | 8 | 24 |
| **Total** | **24** | **24** | **24** | **72** |

### Sample Questions

**Ancient — Easy:**
- "Which ancient civilization built the Great Pyramids of Giza?"
- "Along which river did ancient Egyptian civilization develop?"

**Medieval — Medium:**
- "In which year did the Hundred Years' War between England and France begin?"
- "Who invented the mechanical printing press in Europe around 1440?"

**Modern — Hard:**
- "In what year was the United Nations founded?"
- "Who was the first woman to travel into space?"

Each question includes:
- Question text
- 4 multiple-choice options
- Correct answer index
- Hint text

---

## Scoring System

| Rule | Outcome |
|------|---------|
| **Correct answer + NO hint used** | +1 point |
| **Correct answer + HINT used** | 0 points |
| **Wrong answer (any)** | 0 points |
| **Per quiz** | Always 5 questions |

### Star Rating
The app shows a star rating on the Results screen based on percentage:
- 3 stars: 90%+
- 2 stars: 60%+
- 1 star: <60%

### Score Persistence
- Best score tracked per `(Subject, Difficulty)` combination
- Saved in `SharedPreferences` with key pattern: `high_score_{SUBJECT}_{DIFFICULTY}`
- Also saves timestamp: `high_score_date_{SUBJECT}_{DIFFICULTY}`
- Settings has "Reset All Scores" which clears all high score entries

---

## Screens & User Flow

### Activity Structure (5 total)

| Activity | Layout | Responsibility |
|----------|--------|----------------|
| `MainActivity` | `activity_main.xml` | Home screen. Shows 3 subject cards (Ancient, Medieval, Modern) + Leaderboard + Settings buttons. Launches QuizActivity with selected Subject. |
| `QuizActivity` | `activity_quiz.xml` | Core gameplay. Shows question, 4 answer buttons, score counter, question number, TTS button, Hint button. Handles answer feedback (green=correct, red=wrong), navigation to next question or results. |
| `ResultActivity` | `activity_result.xml` | Post-quiz summary. Shows final score (X/5), star rating, personalized message ("Outstanding!" / "Well done!" / "Keep studying!"), and buttons: Play Again, Home, Share. |
| `SettingsActivity` | `activity_settings.xml` | App configuration. Toggle switches for "Read questions aloud" (TTS) and "Sound effects" (placeholder). Spinner for default difficulty. "Reset All Scores" button with confirmation dialog. |
| `LeaderboardActivity` | `activity_leaderboard.xml` | High scores display. Organized by Subject/Difficulty. Shows best score and timestamp for each combination. |

### User Flow

```
MainActivity
    ├──→ cardAncient   → QuizActivity (SUBJECT=ANCIENT)
    ├──→ cardMedieval  → QuizActivity (SUBJECT=MEDIEVAL)
    ├──→ cardModern    → QuizActivity (SUBJECT=MODERN)
    ├──→ btnLeaderboard → LeaderboardActivity
    └──→ btnSettings    → SettingsActivity

QuizActivity (5 questions)
    ├──→ Answer selected → Shows green/red feedback
    ├──→ btnHint → Shows hint dialog (marks hintUsed=true)
    ├──→ btnTts → Speaks question via TTS
    ├──→ btnNext → Next question OR finish if last
    └──→ onBackPressed → Confirmation dialog ("Leave Quiz? Progress will be lost.")
            └──→ "Leave" → TTS shutdown + finish
            └──→ "Stay" → Cancel

Quiz finish → ResultActivity
    ├──→ btnPlayAgain → QuizActivity (same subject)
    ├──→ btnHome → MainActivity
    └──→ btnShare → Android share sheet with pre-filled message
```

---

## Technologies Used

### Languages
- **Kotlin 1.9.0** — 100% of application code
- **XML** — layouts, resources, manifest

### Android SDK
| Property | Value |
|----------|-------|
| `namespace` | `com.up.projectmanager` |
| `applicationId` | `com.up.projectmanager` |
| `minSdk` | 24 (Android 7.0 Nougat) |
| `targetSdk` | 34 |
| `compileSdk` | 35 |
| `versionCode` | 1 |
| `versionName` | "1.0" |

### Dependencies (actually used)

| Library | Version | Purpose |
|---------|---------|---------|
| `androidx.core:core-ktx` | 1.15.0 | Kotlin extensions for Android framework |
| `androidx.appcompat:appcompat` | 1.7.0 | AppCompat compatibility layer |
| `com.google.android.material:material` | 1.12.0 + 1.8.0 | Material Design 3 components (Cards, Buttons, Toolbar) |
| `androidx.constraintlayout:constraintlayout` | 2.2.0 | ConstraintLayout for complex UIs |
| `androidx.activity:activity` | 1.9.3 | Activity APIs |
| `androidx.fragment:fragment-ktx` | 1.8.5 | Fragment KTX |
| `androidx.lifecycle:lifecycle-runtime-ktx` | 2.8.7 | Lifecycle runtime |

### Dependencies (declared but NOT used)

| Library | Status |
|---------|--------|
| `androidx.lifecycle:lifecycle-viewmodel-ktx` | Declared, no ViewModels in code |
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | Plugin + dep included, no `@Serializable` usage |
| Firebase (BOM, Analytics, Auth, Firestore) | In version catalog, not in app dependencies |
| Google Play Services | In version catalog, not in app dependencies |

### Build System
- **Gradle Wrapper**: 8.13
- **Android Gradle Plugin (AGP)**: 8.13.2
- **Version Catalog**: `gradle/libs.versions.toml` (centralized dependency versions)
- **Kotlin Serialization Plugin**: Applied but unused
- **Java Target**: 1.8
- **ProGuard/R8**: Disabled for release (`isMinifyEnabled = false`)

### Android APIs Used

| API | Usage |
|-----|-------|
| `View Binding` | `ActivityMainBinding`, `ActivityQuizBinding`, etc. — type-safe view access instead of `findViewById` |
| `SharedPreferences` | `ScoreManager` — stores high scores, TTS preference, difficulty preference, sound preference |
| `TextToSpeech` | `TTSManager` — speaks question text, controlled via settings toggle |
| `Material Components` | `MaterialCardView`, `MaterialButton`, `Toolbar` — UI components |
| `AlertDialog` | Hint display, back confirmation, score reset confirmation |
| `Intent` | Inter-activity navigation, `ACTION_SEND` for sharing |
| `ColorStateList` | Dynamic button tinting (correct=green, wrong=red) |

---

## System Architecture

### Actual Architecture: Activity-Based MVC

While the original README mentioned "MVVM", the **actual implementation is a simpler Activity-based MVC pattern**. There are **no ViewModels**, no `@Serializable` data classes, no repository abstraction beyond a singleton object, and no reactive streams.

```
┌─────────────────────────────────────────────────────────────┐
│                         View Layer                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │  Activity    │  │  Activity    │  │  Layout XML       │  │
│  │  (Controller)│  │  (Controller)│  │  (Visual Layout)  │  │
│  │              │  │              │  │                  │  │
│  │ MainActivity │  │ QuizActivity │  │ activity_main.xml │  │
│  │ ResultActivity │ │ SettingsActivity │ activity_quiz.xml │  │
│  │ LeaderboardActivity │            │ etc.               │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
│           │                   │                               │
│           └────────┬──────────┘                               │
│                    ▼                                          │
│  ┌───────────────────────────────────────────────────────┐   │
│                    Model / Data Layer                      │   │
│  ┌─────────────┐  ┌─────────────┐  ┌──────────────────┐ │   │
│  │   Enums     │  │ Data Class  │  │  Repository      │ │   │
│  │             │  │             │  │                  │ │   │
│  │ Subject.kt  │  │ Question.kt │  │ QuizRepository.kt│ │   │
│  │ Difficulty.kt│ │             │  │ (72 hardcoded)   │ │   │
│  └─────────────┘  └─────────────┘  └──────────────────┘ │   │
│                                              │              │   │
│                                              ▼              │   │
│  ┌───────────────────────────────────────────────────────┐ │   │
│  │              Persistence / Utilities                   │ │   │
│  │  ┌───────────────┐  ┌──────────────────┐            │ │   │
│  │  │ ScoreManager  │  │   TTSManager     │            │ │   │
│  │  │               │  │                  │            │ │   │
│  │  │ SharedPreferences │ │ TextToSpeech API │            │ │   │
│  │  │ wrapper       │  │ wrapper          │            │ │   │
│  │  └───────────────┘  └──────────────────┘            │ │   │
│  └───────────────────────────────────────────────────────┘ │   │
└─────────────────────────────────────────────────────────────┘
```

### Architectural Responsibilities

| Layer | Component | Responsibility |
|-------|-----------|----------------|
| **View + Controller** | `*Activity.kt` | Handles all: UI events (clicks), navigation, UI state, calling model/data, applying visual feedback |
| **Layout** | `res/layout/*.xml` | Only visual structure, no logic |
| **Model** | `Subject.kt`, `Difficulty.kt` | Simple enums |
| **Model** | `Question.kt` | Data class with `id`, `text`, `options`, `correctIndex`, `hint` |
| **Data Source** | `QuizRepository.kt` | Singleton `object` holding all 72 hardcoded questions in memory |
| **Persistence** | `ScoreManager.kt` | Wraps `SharedPreferences`. Provides: `saveScore()`, `getHighScore()`, `getHighScoreDate()`, `resetAllScores()`, TTS/sound/difficulty preference getters/setters |
| **Utility** | `TTSManager.kt` | Wraps Android `TextToSpeech`. Provides: `speak()`, `setEnabled()`, `shutdown()` |

### Data Flow Example: Taking a Quiz

```
1. MainActivity
    User clicks "Ancient Civilizations" card
    Intent.putExtra("EXTRA_SUBJECT", Subject.ANCIENT.name)
    startActivity → QuizActivity

2. QuizActivity.onCreate()
    subject = Subject.valueOf(intent.getStringExtra(...))
    difficulty = Difficulty.valueOf(scoreManager.getDifficultyPref())
    questions = QuizRepository.getQuestions(subject, difficulty)
        → QuizRepository filters by (subject,difficulty), shuffles, takes 5
    showQuestion()

3. Quiz Flow (per question)
    showQuestion() → Reset state, populate UI, attach click listeners

    User clicks answer button:
        onAnswerSelected(index)
            isAnswered = true
            if (index == question.correctIndex && !hintUsed) score++
            ColorStateList tinting: green=correct, red=wrong
            Show correct answer if wrong
            Show "Next" button

    User clicks Hint:
        onHintUsed()
            hintUsed = true
            AlertDialog with question.hint
            (Correct answer later will NOT add to score)

    User clicks TTS:
        ttsManager.speak(questions[currentIndex].text)

    User clicks Next:
        if (more questions) showQuestion()
        else finishQuiz()

4. finishQuiz()
    scoreManager.saveScore(subject, difficulty, score)
        → ScoreManager checks if new score > current best, updates if so
    Intent to ResultActivity with EXTRA_SCORE, EXTRA_TOTAL, EXTRA_SUBJECT, EXTRA_DIFFICULTY
    startActivity → ResultActivity

5. ResultActivity
    Shows score (X/5), star rating, message
    Buttons:
        Play Again → QuizActivity (same subject)
        Home → MainActivity
        Share → ACTION_SEND intent with formatted message
```

---

## Data Layer

### Package Structure

```
com.up.projectmanager/
├── model/
│   ├── Subject.kt
│   └── Difficulty.kt
├── data/
│   ├── Question.kt
│   ├── QuizRepository.kt
│   └── ScoreManager.kt
└── util/
    └── TTSManager.kt
```

### model/

**`Subject.kt`**
```kotlin
enum class Subject { ANCIENT, MEDIEVAL, MODERN }
```

**`Difficulty.kt`**
```kotlin
enum class Difficulty { EASY, MEDIUM, HARD }
```

### data/

**`Question.kt`**
```kotlin
data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val hint: String
)
```

**`QuizRepository.kt`**
- Singleton `object QuizRepository`
- `fun getQuestions(subject: Subject, difficulty: Difficulty): List<Question>`
    - Filters by subject → filters by difficulty → `shuffled().take(5)`
- Holds 3 lists: `ancientQuestions`, `medievalQuestions`, `modernQuestions`
- Each is `List<Pair<Difficulty, Question>>`
- Total: 72 `Question` instances hardcoded

**`ScoreManager.kt`**
```kotlin
class ScoreManager(context: Context)
```
- Uses `context.getSharedPreferences("brainquest_prefs", Context.MODE_PRIVATE)`
- Key patterns:
  - High score: `"high_score_${subject.name}_${difficulty.name}"`
  - High score date: `"high_score_date_${subject.name}_${difficulty.name}"`
- Preferences:
  - `"tts_enabled"` (boolean, default `true`)
  - `"difficulty_pref"` (string, default `EASY.name`)
  - `"sound_enabled"` (boolean, default `true`)

### util/

**`TTSManager.kt`**
- Wraps Android `TextToSpeech`
- `speak(text: String)`
- `setEnabled(enabled: Boolean)`
- `shutdown()` — important to call in `QuizActivity.onDestroy()`

### SharedPreferences Contents

| Key Type | Key Pattern | Type |
|----------|-------------|------|
| High Score | `high_score_ANCIENT_EASY` | Int |
| High Score Date | `high_score_date_ANCIENT_EASY` | Long (timestamp) |
| TTS Enabled | `tts_enabled` | Boolean |
| Default Difficulty | `difficulty_pref` | String |
| Sound Enabled | `sound_enabled` | Boolean |

Multiply by `3 subjects × 3 difficulties = 9` high score entries + dates, plus 3 preferences = **21 potential keys total**.

---

## Build & Run

### Prerequisites
- Android Studio (Hedgehog or later recommended)
- Android SDK Platform 35
- JDK 17+ (bundled with Android Studio)
- No API keys or external accounts needed — fully offline

### Android Studio

1. `File → Open`
2. Select `AndroidProjectManager-main` folder
3. Wait for Gradle Sync (auto-prompts)
4. Select device/emulator (API 24+)
5. **Run** (Shift + F10)

### Command Line

```bash
# Windows
gradlew.bat assembleDebug
gradlew.bat installDebug

# macOS/Linux
./gradlew assembleDebug
./gradlew installDebug
```

APK output location:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release-unsigned.apk`

### Build Configuration Notes

- **View Binding enabled**: `buildFeatures { viewBinding = true }`
- **Kotlin Serialization plugin applied**: but no `@Serializable` usage found
- **minSdk 24**: means the app cannot run on Android 6.0 (API 23) or below
- **ProGuard disabled**: `isMinifyEnabled = false` — no code shrinking/obfuscation
- **Version Catalog**: All versions in `gradle/libs.versions.toml`
- **Material duplicated**: Both `material:1.12.0` and `material:1.8.0` in dependencies

---

## Known Implementation Details

### Quirks & Technical Debt

| Item | Details |
|------|---------|
| **Project name vs App name** | Folder/package: `ProjectManager`, `com.up.projectmanager`. Actual app name: `History Quest`. |
| **SharedPreferences name** | `"brainquest_prefs"` — suggests the project may have been renamed from "BrainQuest" to "History Quest". |
| **Dual Material versions** | `app/build.gradle.kts` includes both `material` (1.12.0) and `material.v180` (1.8.0). Unusual. |
| **ViewModels declared but unused** | `lifecycle-viewmodel-ktx` in dependencies, no `ViewModel` subclasses exist. |
| **Kotlin Serialization declared but unused** | Plugin applied + `kotlinx-serialization-json` dependency, no `@Serializable` classes. |
| **Sound toggle is placeholder** | Settings has "Sound effects" toggle. `ScoreManager` stores it. But no actual `MediaPlayer` / `SoundPool` code exists. |
| **TTS availability not checked** | `TTSManager` uses `TextToSpeech` but doesn't gracefully handle devices without a TTS engine installed. |
| **No Fragments** | All UI is in Activities. No Jetpack Navigation component used. Simple `startActivity()` navigation. |
| **No dependency injection** | `ScoreManager`, `TTSManager`, `QuizRepository` instantiated directly in Activities. |
| **Repository is fake** | `QuizRepository` is a singleton `object` with hardcoded lists. No Room, no network, no DAO. |

### Intent Extras (Contract)

`MainActivity` defines constants used across activities:

| Constant | Value | Type | Usage |
|----------|-------|------|-------|
| `EXTRA_SUBJECT` | `"EXTRA_SUBJECT"` | String | Subject enum name (`ANCIENT` etc) |
| `EXTRA_SCORE` | `"EXTRA_SCORE"` | Int | Final score (0-5) |
| `EXTRA_TOTAL` | `"EXTRA_TOTAL"` | Int | Total questions (always 5) |
| `EXTRA_DIFFICULTY` | `"EXTRA_DIFFICULTY"` | String | Difficulty enum name |

### Back Press Handling

- `QuizActivity` overrides `onBackPressed()`
- Shows `AlertDialog`: Title "Leave Quiz?", Message "Your progress will be lost."
- Positive "Leave": `ttsManager.shutdown()` + `super.onBackPressed()`
- Negative "Stay": cancels

### TTS Lifecycle

- `QuizActivity.onCreate()`: `ttsManager = TTSManager(this)`, `ttsManager.setEnabled(scoreManager.isTtsEnabled())`
- `QuizActivity.onDestroy()`: `ttsManager.shutdown()`
- If user leaves via back confirmation: also calls `shutdown()`

---

## License

University educational project.

# QuizGame

Modern Android quiz app with Firebase auth, timed gameplay, and a global leaderboard.

| Category | Details |
| --- | --- |
| Platform | Android |
| Language | Kotlin |
| Auth | Firebase (Email/Password, Google Sign-In) |
| Core Features | Timed quiz, score tracking, leaderboard |

## Highlights
- Fast onboarding with Firebase auth
- Timed questions with score calculation
- Leaderboard to compare performance
- Clean UI and focused gameplay loop

## Tech Stack
- Android SDK
- Kotlin
- Firebase Authentication
- Firebase Realtime Database or Firestore (depending on config)

## Prerequisites
- Android Studio (latest stable)
- Android SDK 24+
- A Firebase project

## Quick Start
1. Open the project in Android Studio.
2. Add your `google-services.json` to `app/`.
3. Update `default_web_client_id` in `app/src/main/res/values/strings.xml`.
4. Sync Gradle and run on a device or emulator.

## Firebase Configuration
| Step | What to do |
| --- | --- |
| 1 | Create a Firebase project |
| 2 | Add an Android app with your package name |
| 3 | Download `google-services.json` and place it in `app/` |
| 4 | Enable Email/Password and Google Sign-In in Firebase Auth |

## Build
| Type | Command |
| --- | --- |
| Debug | Run from Android Studio |
| Release | `./gradlew assembleRelease` |

## Project Structure
| Path | Purpose |
| --- | --- |
| `app/src/main/java` | App source code |
| `app/src/main/res` | Layouts, strings, drawables |
| `app/google-services.json` | Firebase config (local) |
| `app/proguard-rules.pro` | R8/Proguard rules |

## Notes
- Release builds use R8 and resource shrinking.
- If a release crash occurs, add keep rules in `app/proguard-rules.pro`.

## License
MIT License. See `LICENSE`.

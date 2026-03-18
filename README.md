# QuizGame

Android quiz app with Firebase authentication and leaderboard.

## Features
- Email/password auth
- Google sign-in
- Quiz gameplay with timer
- Leaderboard

## Setup
1. Open the project in Android Studio.
2. Add your `google-services.json` to `app/`.
3. Replace `default_web_client_id` in `app/src/main/res/values/strings.xml` with your Firebase web client ID.
4. Sync Gradle and run.

## Build
- Debug: Build and run from Android Studio
- Release: `./gradlew assembleRelease`

## Notes
- Release builds use R8 and resource shrinking. If you hit a crash in release, add keep rules in `app/proguard-rules.pro`.

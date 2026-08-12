# CTRL Android

Native Android / Kotlin / Jetpack Compose implementation of the CTRL life operating system prototype.

## Current build
- Today timeline and live current-task card
- Timer turns red at 2:00 remaining
- Done records actual duration and updates future learned estimates
- Flexible task pull-forward when time is saved
- Swap flexible tasks while protected/fixed blocks stay distinct
- Persistent tasks via local Android SharedPreferences JSON
- 14-day Plan view
- Automatic Life scores
- Capture that adds real tasks
- Change My Day modes
- White / grey / gold UI

## Open and run
1. Open this folder in the latest stable Android Studio.
2. Allow Android Studio to install Android SDK 37 if prompted.
3. Let Gradle sync.
4. Run `app` on an Android device or emulator (minimum API 28).

Current build configuration uses AGP 9.3.1, Gradle 9.5.0 and the Compose 2026.06.00 BOM.

## Next integrations
Microsoft 365 free/busy, Google Calendar, Howbout via calendar sync, Gmail CTRL-label capture, live travel/weather, Health Connect, activity recognition and Android Focus/app blocking.

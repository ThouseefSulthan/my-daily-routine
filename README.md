# My Daily Routine (Android)

A simple offline-first Android routine tracker with daily checklist, reminders, exercise checklist and helpful video links.

## Build without Android Studio
1. Create a GitHub repository named `my-daily-routine`.
2. Extract this ZIP on your computer.
3. Upload the **contents** of this folder to the repository root, including the hidden `.github` folder.
4. Commit to `main`.
5. Open the repository's **Actions** tab and select **Build Android APK**. If it did not start automatically, choose **Run workflow**.
6. When complete, open the successful run and download the `MyDailyRoutine-debug-apk` artifact.
7. Extract the artifact ZIP and install `app-debug.apk` on your Android phone. You may need to allow installation from that source.

## Notes
- Package: `com.example.mydailyroutine`
- Minimum Android: 6.0 (API 23); target SDK 35.
- Notifications require permission on Android 13 and newer.
- Android battery restrictions may delay reminders; this starter uses inexact alarms.
- This starter project has not been built or tested on a physical device. Review and test before relying on reminders.

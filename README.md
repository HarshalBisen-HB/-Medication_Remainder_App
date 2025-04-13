# - Medication Reminder App


## Overview

Med Reminder is a modern Android application designed to help users track and manage their medication schedules. The app provides timely reminders for medication doses, supports both one-time and repeating reminders, and offers an intuitive interface for managing your medication regimen.

## Features

- **User-friendly Interface**: Clean, modern UI built with Jetpack Compose
- **Medication Tracking**: Add and manage all your medication details in one place
- **Flexible Reminders**: Set up one-time or daily repeating medication reminders
- **Customizable Dosage Information**: Track specific dosage instructions for each medication
- **Visual Status Indicators**: Quick visual indicators for medication status and schedule type
- **Easy Management**: Simple interface to mark medications as taken or delete reminders
- **Timed Notifications**: Receive notifications exactly when you need to take your medication

## Screenshots
 ![image](https://github.com/user-attachments/assets/dbbc4fd3-c32e-4567-8dae-f2cb9cbbb24f)
 ![image](https://github.com/user-attachments/assets/9b55b696-8400-44ba-be44-8d4930335fd3)
 ![image](https://github.com/user-attachments/assets/fe7ed24f-8e4f-4775-88c8-3aeb235618a6)
 
## Technology Stack

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern declarative UI toolkit for Android
- **MVVM Architecture**: Clean separation of concerns with ViewModel pattern
- **Hilt & Dagger**: Dependency injection
- **Kotlin Coroutines**: Asynchronous programming
- **Material Design 3**: Modern UI components and styles
- **Room Database**: Local data persistence
- **Alarm Manager**: Scheduling notifications and reminders
- **Android Architecture Components**: ViewModel, StateFlow, etc.

## Installation

1. Clone this repository
2. Open the project in Android Studio
3. Connect your Android device or use an emulator
4. Build and run the application

```bash
git clone https://github.com/yourusername/-medication_reminder_app.git
cd medication-reminder-app
```

## Requirements

- Android Studio Arctic Fox or later
- Android SDK 21+
- Kotlin 1.6+
- JDK 11

## Usage

### Adding a Medication Reminder
1. Tap the "+" button on the main screen
2. Enter the medication name and dosage
3. Select the reminder time
4. Toggle the "Repeating Schedule" option if the medication needs to be taken daily
5. Tap "Save Reminder"

### Managing Reminders
- **Mark as Taken**: Tap the checkmark icon next to a repeating reminder
- **Delete Reminder**: Tap the trash icon to remove a reminder

## Future Enhancements

- Multiple daily reminder times per medication
- Support for different reminder frequencies (weekly, bi-weekly, etc.)
- Medication inventory tracking
- Medication history and statistics
- Dark/Light theme support
- Multi-language support
- User profiles for managing medications for multiple people
- Export/Import functionality


## Acknowledgements

- [Material Design Icons](https://material.io/resources/icons/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Android Alarm Manager](https://developer.android.com/reference/android/app/AlarmManager)


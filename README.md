<div align="center">

# ✈️ Travel Planning App

**Plan your trip in one place: sign in, build an itinerary, explore destinations, find places to stay, and see them on a map.**

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/language-Java%2011-orange?logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-34-blue)
![Target SDK](https://img.shields.io/badge/targetSdk-35-blue)
![Backend](https://img.shields.io/badge/backend-Firebase-FFCA28?logo=firebase&logoColor=black)
![Status](https://img.shields.io/badge/status-prototype-yellow)
![License](https://img.shields.io/badge/license-All%20Rights%20Reserved-red)

</div>

> **Status:** Prototype. Authentication, itinerary storage and the map work end to end. Destination and accommodation search currently return **placeholder data**. See [Known Limitations & Roadmap](#known-limitations--roadmap).

<!-- Add screenshots here, for example:
<p align="center">
  <img src="docs/screenshots/home.png" width="240" alt="Home screen" />
</p>
-->

---

## Table of Contents

- [Features](#features)
- [Screens & App Flow](#screens--app-flow)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Data Model](#data-model)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [1. Clone the repository](#1-clone-the-repository)
  - [2. Configure Firebase](#2-configure-firebase)
  - [3. (Optional) Configure a Google Maps API key](#3-optional-configure-a-google-maps-api-key)
  - [4. Build and run](#4-build-and-run)
- [Running Tests](#running-tests)
- [Configuration Reference](#configuration-reference)
- [Known Limitations & Roadmap](#known-limitations--roadmap)
- [Security Notes](#security-notes)
- [Contributing](#contributing)
- [License](#license)
- [Author & Ownership](#author--ownership)
- [Third-Party Software](#third-party-software)
- [Contact](#contact)

---

## Features

| Feature | Description | Backend |
| --- | --- | --- |
| **Welcome screen** | Intro screen with a "Get Start" button | – |
| **Authentication** | Register and log in with email + password; log out from the home screen | Firebase Authentication |
| **Home dashboard** | Greets the signed-in user by email and links to every feature | Firebase Authentication |
| **Itinerary planner** | Add entries (destination, date, activity), view the list, and clear all entries | Cloud Firestore (`itineraries` collection) |
| **Destination explorer** | Enter travel preferences and get a list of suggested destinations | Placeholder data (to be replaced with a real API) |
| **Accommodation search** | Enter a location and get a list of hotels with price and amenities | Placeholder data (to be replaced with a real API) |
| **Map view** | Geocodes a destination name and drops a marker on an OpenStreetMap map | osmdroid + Android `Geocoder` |
| **Input validation** | Friendly toasts when required fields are empty | – |

## Screens & App Flow

```
StartActivity  ──►  LoginActivity  ──►  MainActivity (home)
 (launcher)         (login/register)     │
                                         ├──►  ItineraryActivity       (Firestore CRUD)
                                         ├──►  DestinationsActivity ──► MapActivity (OSM map)
                                         ├──►  AccommodationActivity
                                         └──►  Logout ──► LoginActivity
```

| Activity | Purpose |
| --- | --- |
| `StartActivity` | Launcher activity; "Get Start" opens the login screen. |
| `LoginActivity` | Email/password sign-in and registration through `FirebaseAuth`. On successful login it opens `MainActivity`. |
| `MainActivity` | Home dashboard: welcome message, navigation buttons, and logout. |
| `ItineraryActivity` | Adds itinerary entries to Firestore, lists them in a `RecyclerView`, and can clear all entries. |
| `DestinationsActivity` | Takes the user's preferences, lists suggested destinations, and opens the map for the entered destination. |
| `AccommodationActivity` | Takes a location and lists matching accommodations. |
| `MapActivity` | Receives `destination_name` via an `Intent` extra, geocodes it, centres the map and adds a marker (falls back to *San Francisco*). |

## Tech Stack

- **Language:** Java 11
- **Platform:** Android (`compileSdk 35`, `targetSdk 35`, **`minSdk 34`** – Android 14 or newer)
- **Build system:** Gradle (Groovy DSL) with a version catalog (`gradle/libs.versions.toml`), Android Gradle Plugin `8.11.1`
- **UI:** AndroidX AppCompat, Material Components, ConstraintLayout, RecyclerView
- **Backend services:** Firebase Authentication, Cloud Firestore, Firebase Analytics (via Firebase BoM `33.12.0`)
- **Maps:** [osmdroid](https://github.com/osmdroid/osmdroid) `6.1.11` (OpenStreetMap tiles). `play-services-maps` is also declared as a dependency.
- **Testing:** JUnit 4, AndroidX Test (JUnit ext), Espresso

## Project Structure

```
Travel_Planning_App/
├── app/
│   ├── build.gradle                     # App module build config & dependencies
│   ├── google-services.json             # Firebase client config
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/travelplanningapp/
│       │   │   ├── StartActivity.java
│       │   │   ├── LoginActivity.java
│       │   │   ├── MainActivity.java
│       │   │   ├── ItineraryActivity.java      ├── Itinerary.java      ├── ItineraryAdapter.java
│       │   │   ├── DestinationsActivity.java   ├── Destination.java    ├── DestinationAdapter.java
│       │   │   ├── AccommodationActivity.java  ├── Accommodation.java  ├── AccommodationAdapter.java
│       │   │   └── MapActivity.java
│       │   └── res/
│       │       ├── layout/              # activity_*.xml and item_*.xml (RecyclerView rows)
│       │       ├── drawable/            # icons and images
│       │       ├── mipmap-*/            # launcher icons
│       │       └── values/ values-night/  # colors, strings, themes
│       ├── test/                        # Local unit tests (JUnit)
│       └── androidTest/                 # Instrumented tests (Espresso)
├── gradle/
│   ├── libs.versions.toml               # Version catalog
│   └── wrapper/                         # Gradle wrapper
├── build.gradle                         # Root build file
├── settings.gradle
└── gradle.properties
```

## Data Model

Plain Java objects (POJOs) used by the adapters and, for itineraries, by Firestore.

**`Itinerary`** – stored in the Firestore collection `itineraries`

| Field | Type | Example |
| --- | --- | --- |
| `destination` | `String` | `"Paris"` |
| `date` | `String` | `"2026-12-24"` |
| `activity` | `String` | `"Visit the Louvre"` |

**`Destination`** – `name`, `description`, `latitude`, `longitude`

**`Accommodation`** – `name`, `location`, `price` (`double`), `amenities`

> Itineraries are currently stored in one shared top-level collection and are **not** scoped per user. See [Known Limitations](#known-limitations--roadmap).

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (a recent version that supports AGP 8.11.x, e.g. Ladybug/Meerkat or newer)
- JDK 11 or newer (the one bundled with Android Studio works)
- Android SDK Platform **35**
- An Android emulator or physical device running **Android 14 (API 34) or higher**
- A Google/Firebase account (to use your own Firebase project)
- Internet access on the device (Firebase and map tiles)

### 1. Clone the repository

```bash
git clone https://github.com/Touchdown11/Travel_Planning_App.git
cd Travel_Planning_App
```

Open the folder in Android Studio and let Gradle sync.

### 2. Configure Firebase

The repo contains an `app/google-services.json` for the original Firebase project. To run the app against **your own** backend (recommended):

1. Go to the [Firebase Console](https://console.firebase.google.com/) and create a project.
2. Add an **Android app** with the package name `com.example.travelplanningapp`.
3. Download the generated `google-services.json` and replace `app/google-services.json`.
4. In the console, enable:
   - **Authentication → Sign-in method → Email/Password**
   - **Firestore Database** (create a database; start in *test mode* for development only)
5. (Optional) Add your debug SHA-1/SHA-256 fingerprints in the Firebase project settings:
   ```bash
   ./gradlew signingReport
   ```

### 3. (Optional) Configure a Google Maps API key

The map screen uses **osmdroid/OpenStreetMap**, which needs no key. The manifest does contain a Google Maps `API_KEY` meta-data placeholder (`YOUR_API_KEY_HERE`) and the `play-services-maps` dependency. If you later switch to Google Maps, replace the placeholder in `app/src/main/AndroidManifest.xml` with your own key. **Do not commit real API keys to a public repository.**

### 4. Build and run

From Android Studio: choose a device and press **Run ▶**.

From the command line:

```bash
# Build a debug APK  (Windows: gradlew.bat assembleDebug)
./gradlew assembleDebug

# Install on a connected device/emulator
./gradlew installDebug
```

The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

#### Using the app

1. Launch the app and tap **Get Start**.
2. Enter an email and password, then tap **Register** to create an account, and **Log In** to sign in.
3. From the home screen choose **Plan** (itinerary), **Destination** or **Booking** (accommodation).
4. In *Itinerary*, fill in destination, date and activity, then tap **Add Your Plan**. Use **Clear Plans** to delete all entries.
5. In *Destinations*, type your preferences and tap **Search Destinations** for suggestions, or tap **Open Map** to see the typed place on a map.
6. In *Accommodation*, type a location and tap **Search**.
7. Use **Log Out** on the home screen to sign out.

## Running Tests

```bash
# Local unit tests (JVM)
./gradlew test

# Instrumented tests (requires a connected device or emulator)
./gradlew connectedAndroidTest
```

The repository currently only contains the default template tests (`ExampleUnitTest`, `ExampleInstrumentedTest`).

## Configuration Reference

| Setting | Value | Where |
| --- | --- | --- |
| Application ID | `com.example.travelplanningapp` | `app/build.gradle` |
| Min / Target / Compile SDK | 34 / 35 / 35 | `app/build.gradle` |
| Version | `1.0` (code `1`) | `app/build.gradle` |
| Java compatibility | 11 | `app/build.gradle` |
| Android Gradle Plugin | 8.11.1 | `gradle/libs.versions.toml` |
| Google Services plugin | 4.4.2 | `build.gradle` |
| Firebase BoM | 33.12.0 | `app/build.gradle` |
| osmdroid | 6.1.11 | `app/build.gradle` |
| Launcher activity | `StartActivity` | `AndroidManifest.xml` |

## Known Limitations & Roadmap

Honest notes on the current state of the project, which also double as a to-do list:

- **Destination and accommodation results are hard-coded.** Replace the dummy data in `DestinationsActivity.fetchDestinations()` and `AccommodationActivity.fetchAccommodations()` with a real API (e.g. Places, Amadeus, Booking-style APIs) or Firestore queries.
- **Itineraries are not per-user.** All entries live in a shared `itineraries` collection; add a `userId` field (or a `users/{uid}/itineraries` sub-collection) and filter by the current user.
- **No edit/delete of a single itinerary item**, only "clear all".
- **Manifest declares a missing `RegisterActivity`.** Registration is handled inside `LoginActivity`; the stale `<activity android:name=".RegisterActivity">` entry (and the commented-out profile button in `MainActivity`) should be removed or implemented.
- **Duplicate/conflicting Gradle dependencies.** `app/build.gradle` declares `firebase-firestore` and `firebase-auth` several times with different versions in addition to the version catalog and the BoM. Consolidate to the BoM and drop explicit versions.
- **`minSdk 34`** limits the audience to Android 14+. Lowering it (e.g. to 24) would make the app available to far more devices.
- **Unused Google Maps dependency/placeholder key** – the map uses osmdroid. Remove the unused pieces or migrate the map to Google Maps.
- **Map robustness:** `Geocoder` is called on the main thread and osmdroid is not configured with a custom user agent; consider moving geocoding to a background thread.
- **Tests:** Only template tests exist. Add unit tests for validation logic and UI tests for the main flows.

Possible future features: trip budgeting, shareable itineraries, offline mode, push-notification reminders, favourites, ratings and weather information.

## Security Notes

- `app/google-services.json` identifies a Firebase project and is not a secret in itself, but access must be protected with **Firebase Security Rules**. Never leave Firestore in permanent test mode; for example, restrict access to authenticated users:
  ```
  rules_version = '2';
  service cloud.firestore {
    match /databases/{database}/documents {
      match /itineraries/{doc} {
        allow read, write: if request.auth != null;
      }
    }
  }
  ```
- Never commit private API keys; use `local.properties` or Gradle secrets to inject them at build time.
- Passwords are handled entirely by Firebase Authentication and are never stored by the app.

## Contributing

This is a proprietary project, and it is not open to unsolicited code contributions. Bug reports, questions and suggestions are welcome through [GitHub Issues](https://github.com/Touchdown11/Travel_Planning_App/issues).

If you would like to contribute code, please open an issue or email the author first. Any contribution is subject to section 5 of the [LICENSE](LICENSE).

## License

Copyright (c) 2025-2026 Touchdown11. **All Rights Reserved.**

This software is proprietary. You may view the source and run it personally for non-commercial evaluation. Copying, modification, redistribution and commercial use are not permitted without written permission from the author. See the [LICENSE](LICENSE) file for the full terms.

## Author & Ownership

| | |
| --- | --- |
| **Author / Owner** | [Touchdown11](https://github.com/Touchdown11) |
| **Copyright holder** | Touchdown11, 2025-2026 |
| **Repository** | <https://github.com/Touchdown11/Travel_Planning_App> |

"Travel Planning App", the app's design, source code and original assets are the property of the author. Third-party components are the property of their respective owners.

## Third-Party Software

This project builds on the following open-source libraries and services. They are **not** covered by this project's license and keep their own licenses.

| Component | Purpose | License |
| --- | --- | --- |
| [AndroidX](https://developer.android.com/jetpack/androidx) (AppCompat, Activity, ConstraintLayout, RecyclerView) | UI framework | Apache-2.0 |
| [Material Components for Android](https://github.com/material-components/material-components-android) | UI components | Apache-2.0 |
| [Firebase Android SDK](https://github.com/firebase/firebase-android-sdk) (Auth, Firestore, Analytics) | Authentication, database, analytics | Apache-2.0 (use of the Firebase service is subject to the [Firebase Terms](https://firebase.google.com/terms)) |
| [osmdroid](https://github.com/osmdroid/osmdroid) | Map rendering | Apache-2.0 |
| Map data: [OpenStreetMap](https://www.openstreetmap.org/copyright) | Map tiles | ODbL, © OpenStreetMap contributors |

Please follow each library's attribution requirements if you distribute a build.

## Contact

**Touchdown11**, [@Touchdown11](https://github.com/Touchdown11), <buddhikalakshn02@gmail.com>

For licensing or permission requests, email the address above with the subject "Travel Planning App: licensing".

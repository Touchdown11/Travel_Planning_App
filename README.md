# ✈️ Travel Planning App

An Android application (Java) that helps travellers plan a trip in one place. Users sign in with email and password, build a day-by-day itinerary stored in the cloud, explore destination ideas, browse accommodation options, and view a destination on an interactive map.

> **Status:** Prototype / coursework-style project. Authentication, itinerary storage and the map are wired up for real. Destination and accommodation search currently return **placeholder (dummy) data**. See [Known Limitations](#known-limitations--roadmap).

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

1. Fork the repository and create a feature branch: `git checkout -b feature/my-feature`
2. Make your changes and make sure `./gradlew assembleDebug test` passes.
3. Commit with a clear message and open a pull request describing the change.

## License

No license file is included in this repository yet. Add a `LICENSE` file (for example MIT or Apache-2.0) to specify how others may use this project; until then, all rights are reserved by the author.

# Final Year University Project - 2019

Android app to enrich the lives of those living in a Residential Care Home, with an emphasis on autism. 

Final Grade - 1st

## Build
* Clone git repo
* Open project in Android Studio 3 or above
* Build the "app" configuration

## Run
* Run the apk through the built in AVD manager in Android studio using > API 22
OR
* Grab apk file from \app\build\outputs\apk\debug
* Upload the apk onto an Android device > API 22, enable "Unknown App Sources" in settings, navigate to the apk in a File Explorer and install.
OR
* Enable USB debugging on your Android device > API 22
* Run (Shift-F10) the app inside Android Studio whilst the device is plugged in
* Select your device when prompted
* Allow USB debugging when prompted



## Files
### /app/

#### CalendarActivity.kt
Contains Calendar Applet implementation

#### CameraActivity.kt
Contains Camera Activity implementation

#### CameraPreview.kt
Contains Camera View implementation

#### DrawingActivity.kt
Contains Drawing Activity and DrawView implementations

#### GlobalApp.kt
Contains Global Variable and Method Definitions

#### FileExplorer.kt
Contains File Explorer implementation

#### Home.kt
Contains Home screen implementation

#### PECSActivity.kt
Contains PECS applet implementation

#### LoginActivity.kt
Contains Login screen implementation

#### SettingsActivity.kt
Contains Settings screen implementation

#### User.kt
Contains database and User object definition and methods

### /src/

#### /drawable/
Contains all images used in the application

#### /layout/
Contains all XML schemas used in the UI

#### /values/
Contains XML values used elsewhere

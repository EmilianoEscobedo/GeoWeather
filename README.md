<div align="center">
<h1>GeoWeather</h1>
<h3>An Android weather app with geolocation capabilities</h3>
<p>
  GeoWeather is an Android weather application built with Jetpack Compose that provides current weather conditions and extended forecast with geolocation capabilities, developed using MVI architecture for Mobile Applications course at ISTEA. 
</p>
<br>
  
<img width="256" height="642" alt="image" src="https://github.com/user-attachments/assets/42be3abd-fd22-437a-b03e-488fc85c08cb" style="margin: 0 10px;" />
<img width="256" height="642" alt="image" src="https://github.com/user-attachments/assets/98a68dfe-43c3-419d-a69c-83e2541410b7" style="margin: 0 10px;" />
<img width="256" height="642" alt="image" src="https://github.com/user-attachments/assets/aad835d1-0df1-4480-9eec-1dcce24489bd" style="margin: 0 10px;" />

</div>

<br>

<h3> ✅ Features </h3>

- Current weather conditions display
- 5-day weather forecast
- Geolocation-based weather detection
- Favorite cities management
- Weather data sharing

<h3> 📐 Architecture </h3> 

- Jetpack Compose for modern declarative UI
- MVI (Model-View-Intent) pattern for predictable state management
- Repository pattern for data abstraction

<h3> 🛠️ Built with </h3>

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) 
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) 
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white) 
![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white) <br>
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white) 
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)

<h3> 📜 Code Standards </h3>

- Keep in mind rules from [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)
- Code must be in English
- Modules names are in singular

<h3> :bookmark: Git Standards </h3>

##### FORMAT 
- Always create the branch from develop
- The branch name format is: `feature/bugfix{ticketNumber}`
- The commits format is: `ticketNumber: {commitDescription}`
- Small commits are a nice to have
- Pull request should always be from your current branch to develop

##### BRANCHES 

This repository was built using [Gitflow workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) branching strategy, so you will see two different branches:
- `main` -> this branch is only for productive versions, it has official release history
- `develop` -> this branch serves as an integration branch for feature


<h3> 👨🏻‍💻 Developers </h3>

| <img src="https://avatars.githubusercontent.com/u/83373185?v=4" width=50>| <img src="https://avatars.githubusercontent.com/u/131006597?v=4" width=50>| <img src="https://avatars.githubusercontent.com/u/139862239?v=4" width=50>| <img src="https://avatars.githubusercontent.com/u/114182597?v=4" width=50>| <img src="https://avatars.githubusercontent.com/u/147558766?v=4" width=50>|
|:-:|:-:|:-:|:-:|:-:|
| **Emiliano Escobedo**| **Nicolas Ibarra**| **Juan Picabea**| **Santiago Andini**| **Nicolás Gonzalez**|
| <a href="https://github.com/EmilianoEscobedo"><img src="https://img.shields.io/badge/github-%23121011.svg?&style=for-the-badge&logo=github&logoColor=white"/></a>| <a href="https://github.com/NicolasAgustinIbarra"><img src="https://img.shields.io/badge/github-%23121011.svg?&style=for-the-badge&logo=github&logoColor=white"/></a>| <a href="https://github.com/pica89"><img src="https://img.shields.io/badge/github-%23121011.svg?&style=for-the-badge&logo=github&logoColor=white"/></a>| <a href="https://github.com/Sandinius"><img src="https://img.shields.io/badge/github-%23121011.svg?&style=for-the-badge&logo=github&logoColor=white"/></a>| <a href="https://github.com/NicoFox01"><img src="https://img.shields.io/badge/github-%23121011.svg?&style=for-the-badge&logo=github&logoColor=white"/></a>|

<br>

<h2> 📋 Run the app </h2>

#### Run on emulator

##### Prerequisites

- [Android Studio installed](https://developer.android.com/studio).  
- [Android 11 SDK configured](https://developer.android.com/about/versions/11/setup-sdk).

##### Steps

1. Open the project in Android Studio.  
2. Build the project by selecting **Build > Make Project**.  
3. Run the emulator from **Tools > AVD Manager**.  
4. Click the **Run** button in Android Studio and select the emulator.  
5. The app will launch in the emulator, showing the balance page.  

#### Run with adb on Android device

##### Prerequisites

- Android device with **USB debugging** enabled.  
- Android SDK platform tools installed (you can get it by installing Android Studio).  
- USB cable to connect device to PC.  

##### Steps

1. Build the APK from Android Studio: **Build > Build Bundle(s) / APK(s) > Build APK(s)**.  
2. Locate the generated APK in `app/build/outputs/apk/debug/app-debug.apk`.  
3. Connect your Android device via USB.  
4. Open a terminal and navigate to the folder containing the APK.  
5. Install the APK using adb:

```bash
adb install -r app-debug.apk
```

<br>
<br>
<br>

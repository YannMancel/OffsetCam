# OffsetCam

**Goal**: Create an application allowing to use the cameras of your device then to take pictures.


## Phone display
<p align="middle">
     <img src="./screenshots/record.gif" width="30%" height="30%">
</p>
<p align="middle">
     <img src="./screenshots/phone_1.png" width="30%" height="30%"> <img src="./screenshots/phone_2.png" width="30%" height="30%"> <img src="./screenshots/phone_3.png" width="30%" height="30%">
</p>


## Requirements
* Computer (Windows, Mac or Linux)
* Android Studio


## Setup the project in Android studio
1. Download the project code, preferably using `git clone https://github.com/YannMancel/OffsetCam.git`.
2. In Android Studio, select *File* | *Open...*
3. Select the project
     
     
## Compile and execute the project in Android studio
1. In Android Studio, select *Run* | *Run...*
2. Choose `app` in *Run dialog*
3. Select a device (*Available Virtual Devices* or *Connected Devices*)
4. Select `OK` in *Select Deployment Target dialog*


## Wiki
* [Android Jetpack](https://developer.android.com/jetpack)
  * [Architecture Components](https://developer.android.com/topic/libraries/architecture/)
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    * [Navigation](https://developer.android.com/guide/navigation/)
  * [Behavior CameraX](https://developer.android.com/training/camerax)
* [Library Material Components](https://github.com/material-components/material-components-android)
* [Reactive programming](http://reactivex.io/)
  * [Library RxAndroid](https://github.com/ReactiveX/RxAndroid)
  * [Library RxKotlin](https://github.com/ReactiveX/RxKotlin)
* [Library Timber](https://github.com/JakeWharton/timber)
* [Library Picasso](https://github.com/square/picasso)


## Troubleshooting

### No device available during the compilation and execution steps 
* If none of device is present (*Available Virtual Devices* or *Connected Devices*),
    * Either select `Create a new virtual device`
    * or connect and select your phone or tablet
     
     
## Useful
* [Download Android Studio](https://developer.android.com/studio)
* [Create a new virtual device](https://developer.android.com/studio/run/managing-avds.html)
* [Enable developer options and debugging](https://developer.android.com/studio/debug/dev-options.html#enable)
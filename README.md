Maklarappen
===========

Mäklarappen, a android school application that searches for realtors in a specific area. 

Reqs:

## Android 4.2.2 API
    This project depends on Android 4.2.2 (Note: Not GoogleAPI)
## Google Play Services
### This app needs play services (Also see alternative solution)
    1. Install Google Playservices for your SDK (sdk manager)
    2. In project settings, add these dependcies:
        Add google_play_services library 
        Add google_play_serivces_lib module
    3. Now we need to add a few apk deps that doesnt come as default on emu:
        Start the emu as follows:
            emulator -avd VM_NAME_HERE -partition-size 500 -no-audio -no-boot-anim
        Then use the following commands:

        # Remount in rw mode
        adb shell mount -o remount,rw -t yaffs2 /dev/block/mtdblock0 /system

        # Allow writing to app directory on system partition
        adb shell chmod 777 /system/app

        # Install following apk
        adb push GoogleLoginService.apk /system/app/.
        adb push GoogleServicesFramework.apk /system/app/.
        adb push Phonesky.apk /system/app/.
        adb shell rm /system/app/SdkSetup*

        These apks can be found in a real device, or be downloaded. They are included in the project tree.

    4. Open up the Play Store, enter your Google Creds and install Google Play Services
#### Google Play alternative solution
	Download Genymotion and start a Google Play Device
#### Troubleshooting
    If this doesnt work on your x86 emu, download Gapps from 
        http://wiki.rootzwiki.com/Google_Apps
    And perform this additional step before pushing the previous apks:
        adb shell rm /system/app/SdkSetup*
        for i in gapps/system/app/*; do adb push "$i" /system/app/; done
    Then proceed with installiong the apks in step 3.
## Setting up the project
    1. Import this project as Module.
    2. Import google-play-services_lib as Module, from path $ANDROID_SDK_PATH/extras/goolge/google_play_services/libproject/google-play-serivces_lib
    3. In this projects Project Settings, define the deps as follows:
        Add jackson_core*.jar, jackson_mapper*.jar, google-play-services.jar (found in the libs dir) and android-support-v4.jar
        Next, add the imported play services module as a Module dep.
        



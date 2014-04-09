Android-NokiaX-Push-Notification
================================

# About Application

1. This application shows how can you integrate PushNotification using App42 API in Android as well as NokiaX application.
2. This Sample supports both platform GCM and NokiaX.
3. You can also publish your same app in Google play store as well as on NokiaX play store.

# Running Sample

This is a sample Android app is made by using App42 Action-Script API. It uses PushNotification API of App42 platform.
Here are the few easy steps to run this sample app.

1. [Register] (https://apphq.shephertz.com/register) with App42 platform.
2. Create an app once, you are on Quick start page after registration.
3. If you are already registered, login to [AppHQ] (http://apphq.shephertz.com) console and create an app from App Manager Tab.
4. Download the project from [here] (https://github.com/VishnuGShephertz/Android-NokiaX-Push-Notification/archive/master.zip) and import it in your Eclipse.
5. Open MainActivity.java file of sample project and make following changes.

```
A. Replace api-Key and secret-Key that you have received in step 2 or 3 at line number 34 and 35.
B. Replace your user-id by which you want to register your application for PushNotification at line number 36.
```

# GCM based Push Integration : 
4. Create a project and get your Project Number from [google developer console](https://cloud.google.com/console/project). It would be available in Overview section of your created project.
5.Select your created project and click on APIs option in Google developer console and enable Google Cloud Messaging for Android service.
6.Click on Credentials(in APIs option) from left menu -> Create New Key -> Server Key.
7.Keep Accept requests from these server IP addresses as blank and click on Create button.
7.Go to [AppHQ] (http://apphq.shephertz.com) click on Push Notification and select Android Settings in Settings option.
8.Select your app and provide as GCM and copy server key that is generated in Google developer console in above step and submit it.
9.Open CommonUtilities.java file of sample project and make following changes.

```
A. Replace your GcmProjectNo by your Google Project No.
```

# NokiaX based Push Integration : 
4.Navigate to the [Nokia Notifications developer console] (https://console.push.nokia.com).Login with your credentials or register accordingly.
5.Click on Create services button on right side.
6.Fill necessary information to create NokiaX service and click on create button.
7.Now you get your Authorization key of NokiaX
7.Go to [AppHQ] (http://apphq.shephertz.com) click on Push Notification and select Android Settings in Settings option.
8.Select your app and provide as NokiaX and copy Authorization key that is generated in above step.(please copy value of key not copy 'key=' text in that key)
9.Open CommonUtilities.java file of sample project and make following changes.

```
A. Replace your NokiaXSenderId  by your NokiaX Sender ID created in above step.
```


11.Build your android application and run on your android device.

__Test and verify PushNotification from AppHQ console__
 
```
A. After registering for PushNotification go to AppHQ console and click on PushNotification and select
  application after selecting User tab.
B. Select desired user from registered User-list and click on Send Message Button.
C. Send appropriate message to user by clicking Send Button.

```
# Design Details:

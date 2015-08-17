Advanced Android Sample App
===================================

Synchronizes weather information from OpenWeatherMap on Android Phones and Tablets. Used in the Udacity Advanced Android course.

Pre-requisites
--------------
Android SDK 21 or Higher
Build Tools version 21.1.2
Android Support AppCompat 22.2.0
Android Support Annotations 22.2.0
Android Support GridLayout 22.2.0
Android Support CardView 22.2.0
Android Support Design 22.2.0
Android Support RecyclerView 22.2.0
Google Play Services GCM 7.0.0
BumpTech Glide 3.5.2


Getting Started
---------------
This sample uses the Gradle build system.  To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

Support
-------

- Google+ Community: https://plus.google.com/communities/105153134372062985968
- Stack Overflow: http://stackoverflow.com/questions/tagged/android

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.

License
-------
Copyright 2015 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.

Notes:
-------
TUTORIAL1: http://www.programmableweb.com/news/how-to-develop-android-wear-application/how-to/2014/10/17
TUTORIAL2: https://www.binpress.com/tutorial/a-guide-to-the-android-wear-message-api/152

Android automatically pushes device notifications to Android wear.
good starting point: https://www.udacity.com/course/viewer#!/c-ud875-nd/l-4613198543/m-4579030547


Prerequisites:
Understanding for Notifications (GCM but not necessarily)
Sample project with notification and Wearable interface:
Android Studio: File -> Sample -> (search for notification)

use v4 support library for ubiquitous computing
'com.android.support:support-v4:21.0.2'

-use notificationmanagercompat for notificationmanager to be in sync with v4 support library


Notification action features in Android wear:
1st: add extra action buttons to notification
 - to view them on wearable, just swipe left on the notification
 - https://www.udacity.com/course/viewer#!/c-ud875-nd/l-4613198543/m-4579030549
2nd: add the action to notificationbuilder

Notification styles:
https://www.udacity.com/course/viewer#!/c-ud875-nd/l-4613198543/m-4579030550
'Big view' styles in notifications also work in Android wear
'Inbox style' is notifications for emails

Notification backgrounds:
https://www.udacity.com/course/viewer#!/c-ud875-nd/l-4613198543/m-4579030551
recommended sizes:
400x400 - for static backgrounds
6408400 - for image parallax background
make sure to save them on <b>drawable-nodpi</b> so that framework does not resize them


Replies on wearable
https://www.udacity.com/course/viewer#!/c-ud875-nd/l-4613198543/m-4579030552
 Voice replies
    Reply button is clickes on wear, converted to string, then delivered to app
   RemoteInput
 Precanned replies
   - like yes, no, maybe buttons
   - utilizes the setChoices() on RemoteInput



Pages
https://www.udacity.com/course/viewer#!/c-ud875-nd/l-4613198543/m-4579030553
- allows you give extra pages of information to a notification
- create 2 notification objects


Stacks
https://www.udacity.com/course/viewer#!/c-ud875-nd/l-4613198543/m-4579030554

Multiple notifications groupings
https://www.udacity.com/course/viewer#!/c-ud875-nd/l-4613198543/m-4579030554

Local notifications
- notifications that are only available on the mobile device but not on the wearable like download progress

Android wear sample
https://youtu.be/Z7UR7Xw7T40


-----------------------------------
ANDROID WeAR APPS START
requirements: https://youtu.be/rTAimkWopEs
phone application needs to be embeded to the wearable application

Connection between wearable and phone
https://youtu.be/OopjNYlqTQ4
http://stackoverflow.com/questions/25413162/sending-data-to-android-wear-device
- thru DATA ITEMS and MESSAGES framework
 DATA ITEMS
  - Key- value pairs
  - Syncronized
  - guaranteed
 MESSAGES
  -





ANDROID WeAR APPS END
-----------------------------------

GOOGLE PLAY SERVICES SETUP
https://developers.google.com/android/guides/setup
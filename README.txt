#Welcome to the Gather Around project!

## Objective:

Gather Around is a service that aims to build a democratic platform where all users have the same, if not, similar advertisement and broadcasting power. We are able to achieve this by discarding the traditional “scroll-feed” UI that social networking services such as Facebook, Twitter, and Instagram use, that forces the users to gain new information in a linear format. Although this is how we traditionally read information, it gives too much power to their algorithms that feed us information. The users needs to have the right to explore information as if it was the real world - like a map!

## Our Solution

The Gather Around App replaces all scroll-like interfaces with a map-based UI that gives a 2 dimensional freedom for exploring content on the service. 

##Functionalities of the App

This app is fundamentally an SNS app that allows you to share real-life events that you might be planning, which is why a map-based UI was used instead of a scroll-feed UI: to be fair to everyone.

Users will be able to:

*create an account
*create their own profile
*share events
	*set event date, location, details, duration, category
*register in other's events
	*get notified of events they have registered and it's cancellations
*use virtual-capsule functionality (more on this later)
*generate customized-designable QR code (more on this later)


The virtual-capsule functionality allows users to virtually plant a message at a certain location on the map. The message only gets activated when a user arrives at that location and opens the app. This feature can be used to hid coupon codes or secret messages that can be activated to gather more customers.

The QR code functionality allows event creators to past a unique QR on their website or a poster in the city. Users of the app will be able to read that code with their camera and get instant information on their events, and will be able to interact with the event.


## Videos and Pictures

https://drive.google.com/drive/folders/1e0RdNqvKkY65S7VyDV4kSGdM760swYUW?usp=sharing


## API and Dependency Details

UI Elements's API:

```
com.android.support.constraint:constraint-layout:1.0.2
com.android.support:appcompat-v7:26
com.android.support:design:26.0.0-alpha1
com.android.support:support-v4:26.0.0-alpha1
com.android.support:recyclerview-v7:26.0.0-alpha1
com.android.support:cardview-v7:26.0.0-alpha1
de.hdodenhof:circleimageview:2.1.0
com.github.wdullaer:MaterialDateTimePicker:v3.0.0

```

Google Map API:

```
com.google.android.gms:play-services-maps:11.0.4
com.google.code.gson:gson:2.2.4
```

Server:

```
com.google.firebase:firebase-database:10.0.1
com.firebase:firebase-client-android:2.5.2
```


Camera Scanner:

```
com.google.zxing:core:3.2.1
com.journeyapps:zxing-android-embedded:3.2.0@aar
```

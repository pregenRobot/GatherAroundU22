package com.example.android.gatheraround.custom_classes;

import android.util.EventLog;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tamimazmain on 2017/11/13.
 */

public class Capsule {

    private LatLng latlng;
    private String information;
    private EventDate eventDate;
    private String mCapsuleId;
    private String userId;

    public Capsule(LatLng location, String details, EventDate Date, String uid){
        latlng = location;
        information = details;
        eventDate = Date;
        userId = uid;
    }

    public LatLng getLocation(){
        return latlng;
    }
    public String getInformation(){
        return information;
    }
    public EventDate getDate(){
        return eventDate;
    }
}

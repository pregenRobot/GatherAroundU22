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

    public Capsule(LatLng location, String details){
        latlng = location;
        information = details;
    }
    public Capsule(LatLng location, String details, EventDate Date){
        latlng = location;
        information = details;
        eventDate = Date;
    }

    public LatLng getLocation(){
        return latlng;
    }
    public String getMessage(){
        return information;
    }
    public EventDate getDate(){
        return eventDate;
    }
}

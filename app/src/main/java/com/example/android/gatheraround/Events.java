package com.example.android.gatheraround;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class Events {
    long mUnixTimeStanp;
    private String mName;
    private Participants mParticipants;
    private LatLng mLocation;
    private String mLocationName;
    private Calculations calcref = new Calculations();

    Events(long unixTimeStanp, String name,Participants participants,LatLng Latilong,String locationname){
        mUnixTimeStanp = unixTimeStanp;
        mName = name;
        mParticipants = participants;
        mLocation = Latilong;
        mLocationName = locationname;
    }

    public long getUnixTimeStamp(){
        return mUnixTimeStanp;
    }
    public String getName(){
        return mName;
    }
    public Participants getParticipants(){
        return mParticipants;
    }
    public LatLng getLocation(){
        return mLocation;
    }
    public String getLocationName(){
        return mLocationName;
    }

}
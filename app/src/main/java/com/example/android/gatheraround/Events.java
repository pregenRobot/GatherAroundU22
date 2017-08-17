package com.example.android.gatheraround;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static com.example.android.gatheraround.MainActivity.participants;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class Events {
    long mUnixTimeStanp;
    private String mName;
    private ArrayList<Participants> mParticipants;
    private LatLng mLocation;
    private String mLocationName;

    Events(long unixTimeStanp, String name,ArrayList<Participants> participants,LatLng Latilong,String locationname){
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
    public ArrayList<Participants> getParticipants(){
        return mParticipants;
    }
    public LatLng getLocation(){
        return mLocation;
    }
    public String getLocationName(){
        return mLocationName;
    }

}
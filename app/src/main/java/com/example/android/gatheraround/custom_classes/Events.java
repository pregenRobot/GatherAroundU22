package com.example.android.gatheraround.custom_classes;

import com.example.android.gatheraround.Calculations;
import com.example.android.gatheraround.custom_classes.Participants;
import com.google.android.gms.maps.model.LatLng;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class Events {
    long mUnixTimeStanp;
    private String mName;
    private int mParticipants;
    private LatLng mLocation;
    private String mLocationName;
    private Calculations calcref = new Calculations();
    private String mEventSummary;

    public static final String CATEGORY_NPO = "NPO";
    public static final String CATEGORY_INDIVIDUAL = "INDIVIDUAL";
    public static final String CATEGORY_CORPORATE = "CORPORATE";

    public Events(long unixTimeStanp, String name,int participants,LatLng Latilong,String locationname, String eventsummary){
        mUnixTimeStanp = unixTimeStanp;
        mName = name;
        mParticipants = participants;
        mLocation = Latilong;
        mLocationName = locationname;
        mEventSummary = eventsummary;
    }

    public long getUnixTimeStamp(){
        return mUnixTimeStanp;
    }
    public String getName(){
        return mName;
    }
    public int getParticipants(){
        return mParticipants;
    }
    public LatLng getLocation(){
        return mLocation;
    }
    public String getLocationName(){
        return mLocationName;
    }
    public String getEventSummary(){return mEventSummary;}

    public String toString(){
        String returner = String.valueOf(mUnixTimeStanp) + " / " + mName + " / " + mParticipants + " / " + mLocation.toString() + " / " + mLocationName + " / " + mEventSummary;
        return returner;
    }

}
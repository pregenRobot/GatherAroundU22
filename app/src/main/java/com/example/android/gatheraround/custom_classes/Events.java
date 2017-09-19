package com.example.android.gatheraround.custom_classes;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class Events {

    private EventDate mDate;
    private String mName;
    private int mParticipants;
    private LatLng mLocation;
    private String mLocationName;
    private String mEventSummary;
    private String mCategory;
    private String mGlobalId;
    private boolean mDoesExistsOnServer;

    public static String CATEGORY_INDIVIDUAL = "INDIVIDUAL";
    public static String CATEGORY_NPO = "NPO";
    public static String CATEGORY_CORPORATE = "CORPORATE";

    public Events(EventDate date, String name,int participants,LatLng Latilong,String locationname, String eventsummary, String category, String key, boolean doesExistsOnServer){
        mDate = date;
        mName = name;
        mParticipants = participants;
        mLocation = Latilong;
        mLocationName = locationname;
        mEventSummary = eventsummary;
        mCategory = category;
        mGlobalId = key;
        mDoesExistsOnServer =doesExistsOnServer;
    }

    public EventDate getDate(){
        return mDate;
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

    public String getEventSummary(){
        return mEventSummary;
    }

    public String getCategory(){
        if(mCategory != null) {
            return mCategory;
        } else{
            return  null;
        }
    }

    public String getGlobalId(){
        return mGlobalId;
    }

    public boolean getDoesExitsOnServer(){
        return mDoesExistsOnServer;
    }

    public String toString(){
        return  mDate.makeOneLineText() + " / " + mName + " / " + mParticipants + " / " + mLocation.toString() + " / " + mLocationName + " / " + mEventSummary + " / " + mCategory;
    }
}
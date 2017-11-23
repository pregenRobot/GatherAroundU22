package com.example.android.gatheraround.custom_classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class Events implements Parcelable{

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

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events (in);
        }

        @Override
        public Events[] newArray(int i) {
            return new Events[i];
        }
    };


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

    public Events(Parcel in){
        this.mDate = in.readParcelable(EventDate.class.getClassLoader());
        this.mName = in.readString();
        this.mParticipants = in.readInt();
        this.mLocation = in.readParcelable(LatLng.class.getClassLoader());
        this.mLocationName = in.readString();
        this.mEventSummary = in.readString();
        this.mGlobalId = in.readString();
        this.mDoesExistsOnServer = in.readByte() != 0;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mDate,flags);
        dest.writeString(this.mName);
        dest.writeInt(this.mParticipants);
        dest.writeParcelable(this.mLocation,flags);
        dest.writeString(this.mLocationName);
        dest.writeString(this.mEventSummary);
        dest.writeString(this.mGlobalId);
        dest.writeByte((byte) (this.mDoesExistsOnServer ? 1:0));
    }

    @Override
    public String toString() {
        return "Events{" +
                "mDate='" + mDate + '\'' +
                ", mName='" + mName + '\'' +
                ", mParticipants='" + mParticipants + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mLocationName='" + mLocationName + '\'' +
                ", mEventSummary='" + mEventSummary + '\'' +
                ", mGlobalId='" + mGlobalId + '\'' +
                ", mDoesExistOnServer='" + mDoesExistsOnServer + '\'' +
                '}';
    }


}
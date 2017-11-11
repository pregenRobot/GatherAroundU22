package com.example.android.gatheraround.custom_classes;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chiharu_miyoshi on 2017/09/15.
 */

public class EventDate implements Parcelable{

    private String mYear;
    private String mMonth;
    private String mDay;
    private String mHour;
    private String mMinute;

    private String mYear2;
    private String mMonth2;
    private String mDay2;
    private String mHour2;
    private String mMinute2;

    public static String DEFAULT_TIME = "-1";
    //Test

    public EventDate (String year, String month, String day, String hour, String minute, String year2, String month2, String day2, String hour2, String minute2){
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;
        mYear2 = year2;
        mMonth2 = month2;
        mDay2 = day2;
        mHour2 = hour2;
        mMinute2 = minute2;
    }
    private static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public EventDate createFromParcel(Parcel parcel) {
            return new EventDate (parcel);
        }

        @Override
        public EventDate[] newArray(int i) {
            return new EventDate[i];
        }
    };

    public void updateDate1(String year,String month,String day){
        mYear = year;
        mMonth = month;
        mDay = day;
    }
    public void updateDate2(String year,String month, String day){
        mYear2 = year;
        mMonth2 = month;
        mDay2 = day;
    }
    public void updateTime1(String hour,String minute){
        mHour = hour;
        mMinute = minute;
    }
    public void updateTime2(String hour,String minute){
        mHour2 = hour;
        mMinute2 = minute;
    }
    public void defaultDay2(){
        mYear2 = DEFAULT_TIME;
        mMonth2 = DEFAULT_TIME;
        mDay2 = DEFAULT_TIME;
    }
    public void defaultTime1(){
        mHour = DEFAULT_TIME;
        mMinute = DEFAULT_TIME;
    }
    public void defaultTime2(){
        mHour2 = DEFAULT_TIME;
        mMinute2 = DEFAULT_TIME;
    }

    public String getmYear(){return mYear;}

    public String getmMonth(){return mMonth;}

    public String getmDay(){return mDay;}

    public String getmHour(){return mHour;}

    public String getmMinute(){return mMinute;}

    public String getmYear2(){return mYear2;}

    public String getmMonth2(){return mMonth2;}

    public String getmDay2(){return mDay2;}

    public String getmHour2(){return mHour2;}

    public String getmMinute2(){return mMinute2;}

    public String makeOneLineText(){

        String result;

        if (mMonth2.equals("-1")){
            result = String.valueOf(mYear) + " / " + String.valueOf(mMonth) + " / " + String.valueOf(mDay) + " / " + String.valueOf(mHour) + " : " + String.valueOf(mMinute);
        } else {
            result = String.valueOf(mYear) + " / " + String.valueOf(mMonth) + " / " + String.valueOf(mDay) + " / " + String.valueOf(mHour) + " : " + String.valueOf(mMinute)
                    + " ~ " + String.valueOf(mYear) + " / " + String.valueOf(mMonth) + " / " + String.valueOf(mDay) + " / " + String.valueOf(mHour2) + " : " + String.valueOf(mMinute2);
        }

        return result;
    }

    public EventDate(Parcel in){
        this.mYear = in.readString();
        this.mMonth = in.readString();
        this.mDay = in.readString();
        this.mHour = in.readString();
        this.mMinute = in.readString();

        this.mYear2 = in.readString();
        this.mMonth2 = in.readString();
        this.mDay2 = in.readString();
        this.mHour2 = in.readString();
        this.mMinute2 = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mYear);
        dest.writeString(this.mMonth);
        dest.writeString(this.mDay);
        dest.writeString(this.mHour);
        dest.writeString(this.mMinute);
    }

    @Override
    public String toString() {
        return "Events{" +
                "mYear='" + mYear + '\'' +
                ", mMonth='" + mMonth + '\'' +
                ", mDay='" + mDay + '\'' +
                ", mHour='" + mHour + '\'' +
                ", mMinute='" + mMinute + '\'' +
                ", mYear2='" + mYear2 + '\'' +
                ", mMonth2='" + mMonth2 + '\'' +
                ", mDay2='" + mDay2 + '\'' +
                ", mHour2='" + mHour2 + '\'' +
                ", mMinute2='" + mMinute2 + '\'' +
                '}';
    }
}

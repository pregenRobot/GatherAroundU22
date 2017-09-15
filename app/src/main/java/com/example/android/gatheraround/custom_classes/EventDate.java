package com.example.android.gatheraround.custom_classes;

/**
 * Created by chiharu_miyoshi on 2017/09/15.
 */

public class EventDate {

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private int mYear2;
    private int mMonth2;
    private int mDay2;
    private int mHour2;
    private int mMinute2;

    public EventDate (int year, int month, int day, int hour, int minute, int year2, int month2, int day2, int hour2, int minute2){
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
    public EventDate (int year, int month, int day, int year2, int month2, int day2){
        mYear = year;
        mMonth = month;
        mDay = day;
        mYear2 = year2;
        mMonth2 = month2;
        mDay2 = day2;

    }


    public int getmYear(){return mYear;}

    public int getmMonth(){return mMonth;}

    public int getmDay(){return mDay;}

    public int getmHour(){return mHour;}

    public int getmMinute(){return mMinute;}

    public int getmYear2(){return mYear2;}

    public int getmMonth2(){return mMonth2;}

    public int getmDay2(){return mDay2;}

    public int getmHour2(){return mHour2;}

    public int getmMinute2(){return mMinute2;}
}

package com.example.android.gatheraround.custom_classes;

import static com.example.android.gatheraround.R.id.m;

/**
 * Created by chiharu_miyoshi on 2017/09/15.
 */

public class EventDate {

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
    public EventDate (){

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

    public String makeDateText(Boolean withNewLine){

        String result;

        if (mMonth2.equals("-1")){
            result = String.valueOf(mYear) + " / " + String.valueOf(mMonth) + " / " + String.valueOf(mDay);
        } else {

            if (withNewLine) {
                result = String.valueOf(mYear) + " / " + String.valueOf(mMonth) + " / " + String.valueOf(mDay)
                        + "\n" + String.valueOf(mYear2) + " / " + String.valueOf(mMonth2) + " / " + String.valueOf(mDay2);
            } else {
                result = String.valueOf(mYear) + " / " + String.valueOf(mMonth) + " / " + String.valueOf(mDay)
                        + " ~ " + String.valueOf(mYear2) + " / " + String.valueOf(mMonth2) + " / " + String.valueOf(mDay2);
            }
        }

        return result;
    }


    public String makeTimeText(boolean withNewLine){

        String result;

        if (mHour2.equals("-1")){
            result = String.valueOf(mHour) + " : " + String.valueOf(mHour);
        } else {

            if (withNewLine){
                result = String.valueOf(mHour) + " : " + String.valueOf(mMinute) + "\n" + String.valueOf(mHour2) + " : " + String.valueOf(mMinute2);
            } else {
                result = String.valueOf(mHour) + " : " + String.valueOf(mMinute) + " ~ " + String.valueOf(mHour2) + " : " + String.valueOf(mMinute2);
            }
        }

        return result;
    }

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
}

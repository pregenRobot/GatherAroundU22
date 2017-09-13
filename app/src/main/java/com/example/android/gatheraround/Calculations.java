package com.example.android.gatheraround;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tamimazmain on 2017/08/15.
 */

public class Calculations {

    public String[] UnixTimeConverter(long unixTimeStamp){
        String Month;
        String Year;
        String Day;

        String Hour;
        String Minute;

        String[] returnDate = new String[3];

        Date date = new Date(unixTimeStamp*1000+6*60*60*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-9")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);

        Year = formattedDate.substring(0,4);
        Month = formattedDate.substring(5,7);
        Day = formattedDate.substring(8,10);

        Hour = formattedDate.substring(11,13);
        Minute = formattedDate.substring(14,16);


        returnDate[0] = Month + " / " + Day;
        returnDate[1] = Hour + ":" + Minute;
        returnDate[2] = Year + "-" + Month + "-" + Day;
        return returnDate;
    }
    public String UnixTimeConverterString(long unixTimeStamp){
        Date date = new Date(unixTimeStamp*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-9"));
        return  sdf.format(date);
    }

    public String ParticipantConcatenation(String[] participants){
        String returner="";

        for (String x:participants) {
            returner = returner + x + ", ";
        }
        return returner;
    }
}

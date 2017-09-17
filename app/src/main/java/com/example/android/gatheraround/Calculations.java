package com.example.android.gatheraround;

import com.example.android.gatheraround.custom_classes.EventDate;

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
    public String[] concatenate(EventDate testEvent,boolean fullorNot,boolean cardornot){
        String[] result= new String[2];

        if(cardornot){
            if (testEvent.getmHour2().equals("-1")&&testEvent.getmMinute2().equals("-1")
                    &&testEvent.getmHour().equals("-1")&&testEvent.getmMinute().equals("-1")) {

                if(fullorNot){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay())+
                            " ~ " + String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                    result[1]="WHOLE DAY";
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay())+
                            " ~ " +String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                    result[1]="WHOLE DAY";
                }

            }
            //No Date2 selected -> Day1 and Time1 to Time 2
            else if(testEvent.getmMonth2().equals("-1")&&testEvent.getmYear2().equals("-1")&&testEvent.getmDay2().equals("-1")
                    &&testEvent.getmHour2().equals("-1")&&testEvent.getmMinute2().equals("-1")){
                if(fullorNot){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                }
            }
            //No Date2 and No Time2 selected -> Day1
            else if(testEvent.getmMonth2().equals("-1")&&testEvent.getmYear2().equals("-1")&&testEvent.getmDay2().equals("-1")
                    &&testEvent.getmHour().equals("-1")&&testEvent.getmMinute().equals("-1")
                    &&testEvent.getmHour2().equals("-1")&&testEvent.getmMinute2().equals("-1")){
                if(fullorNot){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+ String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = "WHOLE DAY";
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = "WHOLE DAY";
                }
            }
            //Everything Selected -> Day1 to Day2 and Time1 to Time2
            else{
                if(fullorNot){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay()) + "~" +
                            String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute()) + " ~ " +
                            String.valueOf(testEvent.getmHour2())+":"+String.valueOf(testEvent.getmMinute2());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay()) + "~" +
                            String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute()) + " ~ " +
                            String.valueOf(testEvent.getmHour2())+":"+String.valueOf(testEvent.getmMinute2());
                }
            }
        }else{
            if (testEvent.getmHour2().equals("-1")&&testEvent.getmMinute2().equals("-1")
                    &&testEvent.getmHour().equals("-1")&&testEvent.getmMinute().equals("-1")) {

                if(fullorNot){
                    result[0]= String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1]= String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                }

            }
            //No Date2 selected -> Day1 and Time1 to Time 2
            else if(testEvent.getmMonth2().equals("-1")&&testEvent.getmYear2().equals("-1")&&testEvent.getmDay2().equals("-1")
                    &&testEvent.getmHour2().equals("-1")&&testEvent.getmMinute2().equals("-1")){
                if(fullorNot){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay())+"\n"+
                            String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                    result[1] = "";
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay())+"\n"+
                            String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                    result[1] = "";
                }
            }
            //No Date2 and No Time selected -> Day1
            else if(testEvent.getmMonth2().equals("-1")&&testEvent.getmYear2().equals("-1")&&testEvent.getmDay2().equals("-1")
                    &&testEvent.getmHour().equals("-1")&&testEvent.getmMinute().equals("-1")
                    &&testEvent.getmHour2().equals("-1")&&testEvent.getmMinute2().equals("-1")){
                if(fullorNot){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+ String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = "";
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = "";
                }
            }
            //Everything Selected -> Day1 to Day2 and Time1 to Time2
            else{
                if(fullorNot){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay()) + "\n" +
                            String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                    result[1] = String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2()) + "\n" +
                            String.valueOf(testEvent.getmHour2())+":"+String.valueOf(testEvent.getmMinute2());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay()) + "\n" +
                            String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                    result[1] = String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2()) + "\n" +
                            String.valueOf(testEvent.getmHour2())+":"+String.valueOf(testEvent.getmMinute2());
                }
            }
        }

        //No Time selected -> Day1 and Day2

        return result;
    }
}

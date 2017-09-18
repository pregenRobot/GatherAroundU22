package com.example.android.gatheraround;

import com.example.android.gatheraround.custom_classes.EventDate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tamimazmain on 2017/08/15.
 */

public class Calculations {

    public static final String NO_INFO = "NO_INFO";

    public String[] concatenate(EventDate testEvent, boolean isFull, boolean withTilde){
        String[] result= new String[2];

        if(withTilde){
            
            if (testEvent.getmHour().equals(EventDate.DEFAULT_TIME) && testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)) {

                if(isFull){
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
            else if(testEvent.getmYear2().equals(EventDate.DEFAULT_TIME) && testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)){
                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                }
            }
            //No Date2 and No Time2 selected -> Day1
            else if(testEvent.getmHour().equals(EventDate.DEFAULT_TIME) && testEvent.getmYear2().equals(EventDate.DEFAULT_TIME) && testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)){
                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+ String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = "WHOLE DAY";
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = "WHOLE DAY";
                }
            }
            //Everything Selected -> Day1 to Day2 and Time1 to Time2
            else{
                if(isFull){
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
            if (testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)&&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour().equals(EventDate.DEFAULT_TIME)&&testEvent.getmMinute().equals(EventDate.DEFAULT_TIME)) {

                if(isFull){
                    result[0]= String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1]= String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                }

            }
            //No Date2 selected -> Day1 and Time1 to Time 2
            else if(testEvent.getmMonth2().equals(EventDate.DEFAULT_TIME)&&testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)&&testEvent.getmDay2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)&&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)){
                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay())+"\n\n"+
                            String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                    result[1] = "";
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay())+"\n\n"+
                            String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                    result[1] = "";
                }
            }
            //No Date2 and No Time selected -> Day1
            else if(testEvent.getmMonth2().equals(EventDate.DEFAULT_TIME)&&testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)&&testEvent.getmDay2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour().equals(EventDate.DEFAULT_TIME)&&testEvent.getmMinute().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)&&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)){
                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+ String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = "";
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = "";
                }
            }
            //Everything Selected -> Day1 to Day2 and Time1 to Time2
            else{
                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay()) + "\n\n" +
                            String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                    result[1] = String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2()) + "\n\n" +
                            String.valueOf(testEvent.getmHour2())+":"+String.valueOf(testEvent.getmMinute2());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay()) + "\n\n" +
                            String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                    result[1] = String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2()) + "\n\n" +
                            String.valueOf(testEvent.getmHour2())+":"+String.valueOf(testEvent.getmMinute2());
                }
            }
        }

        //No Time selected -> Day1 and Day2

        return result;
    }

    public String[] getTimeContents(EventDate eventDate){

        String[] result = new String[4];
        /*
        1: date1 date
        2: date1 time
        3: date2 date
        4: date2 time
         */

        result[0] = String.valueOf(eventDate.getmYear()) + "/" + String.valueOf(eventDate.getmMonth() + "/" + String.valueOf(eventDate.getmDay()));

        if (!eventDate.getmHour().equals(EventDate.DEFAULT_TIME)){
            result[1] = String.valueOf(eventDate.getmHour()) + ":" + String.valueOf(eventDate.getmMinute());
        } else {
            result[1] = NO_INFO;
        }

        if (!eventDate.getmYear2().equals(EventDate.DEFAULT_TIME)){
            result[2] = String.valueOf(eventDate.getmYear2()) + "/" + String.valueOf(eventDate.getmMonth2() + "/" + String.valueOf(eventDate.getmDay2()));

            if (!eventDate.getmHour2().equals(EventDate.DEFAULT_TIME)){
                result[3] = String.valueOf(eventDate.getmHour2()) + ":" + String.valueOf(eventDate.getmMinute2());
            } else {
                result[3] = NO_INFO;
            }
        } else {
            result[2] = NO_INFO;
            result[3] = NO_INFO;
        }

        return result;
    }

    public String makeOneLineText(EventDate eventDate){

        String result;

        if (eventDate.getmMonth2().equals("-1")){
            if (eventDate.getmHour().equals(EventDate.DEFAULT_TIME)){
                result = String.valueOf(eventDate.getmYear()) + "/" + String.valueOf(eventDate.getmMonth()) + "/" + String.valueOf(eventDate.getmDay()) + "/";
            } else {
                result = String.valueOf(eventDate.getmYear()) + "/" + String.valueOf(eventDate.getmMonth()) + "/" + String.valueOf(eventDate.getmDay()) + "/" + String.valueOf(eventDate.getmHour()) + ":" + String.valueOf(eventDate.getmMinute());
            }
        } else {
            if (eventDate.getmHour().equals(EventDate.DEFAULT_TIME)){
                result = String.valueOf(eventDate.getmYear()) + "/" + String.valueOf(eventDate.getmMonth()) + "/" + String.valueOf(eventDate.getmDay()) + "/"
                        + "~" + String.valueOf(eventDate.getmYear2()) + "/" + String.valueOf(eventDate.getmMonth2()) + "/" + String.valueOf(eventDate.getmDay2()) + "/";
            }else{
                result = String.valueOf(eventDate.getmYear()) + "/" + String.valueOf(eventDate.getmMonth()) + "/" + String.valueOf(eventDate.getmDay()) + "/" + String.valueOf(eventDate.getmHour()) + ":" + String.valueOf(eventDate.getmMonth())
                        + "~" + String.valueOf(eventDate.getmYear2()) + "/" + String.valueOf(eventDate.getmMonth2()) + "/" + String.valueOf(eventDate.getmDay2()) + "/" + String.valueOf(eventDate.getmHour2()) + ":" + String.valueOf(eventDate.getmMinute2());

            }
        }

        return result;
    }
}

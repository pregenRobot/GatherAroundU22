package com.example.android.gatheraround;

import android.util.Log;

import com.example.android.gatheraround.custom_classes.EventDate;
import com.firebase.client.core.view.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tamimazmain on 2017/08/15.
 */

public class Calculations {

    public String[] concatenate(EventDate testEvent, boolean isFull, boolean withTilde){
        String[] result= new String[2];

        if(withTilde){
            //No Time1 and Time2 selected -> Day1 and Day2
            if (testEvent.getmHour().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)) {

                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());

                    result[1]= String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());;
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1]=String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                }
            }
            //No Date2 selected -> Day1 and Time1
            else if(testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMonth2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmDay2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmHour().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmMinute().equals(EventDate.DEFAULT_TIME)){
                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                }
            }
            //No Date2 and No Time2 selected -> Day1
            else if(testEvent.getmHour().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMonth2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmDay2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)){
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
                Log.v("Concatenating:","choice4");
            }
        }else{
//
            if (testEvent.getmHour().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)) {

                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());

                    result[1]= String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());;
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1]=String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
                }

            }
            //No Date2 selected -> Day1 and Time1
            else if(testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMonth2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmDay2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmHour().equals(EventDate.DEFAULT_TIME)
                    &&!testEvent.getmMinute().equals(EventDate.DEFAULT_TIME)){
                if(isFull){
                    result[0] = String.valueOf(testEvent.getmYear())+"/"+String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                }else{
                    result[0] = String.valueOf(testEvent.getmMonth())+"/"+String.valueOf(testEvent.getmDay());
                    result[1] = String.valueOf(testEvent.getmHour())+":"+String.valueOf(testEvent.getmMinute());
                }
            }
            //No Date2 and No Time2 selected -> Day1
            else if(testEvent.getmHour().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmYear2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMonth2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmDay2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmHour2().equals(EventDate.DEFAULT_TIME)
                    &&testEvent.getmMinute2().equals(EventDate.DEFAULT_TIME)){
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

        return result;
    }

}

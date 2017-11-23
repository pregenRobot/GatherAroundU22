package com.example.android.gatheraround.processes;

import android.util.Log;

import com.example.android.gatheraround.custom_classes.EventDate;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;


/**
 * Created by tamimazmain on 2017/08/15.
 */

public class Calculations {

    public int checkcloseness(LatLng mylocation,LatLng testlocation){
        int returner = 0;
        if((mylocation.latitude - testlocation.latitude)<0.002||(mylocation.latitude - testlocation.latitude)>-0.002){
            if((mylocation.longitude - testlocation.longitude)<0.002||(mylocation.longitude - testlocation.longitude)>-0.002){
                returner = 2;
                return returner;
            }
            returner = 1;
        }else if((mylocation.longitude - testlocation.longitude)<0.002||(mylocation.longitude - testlocation.longitude)>-0.002){
            returner = 1;
        }
        return returner;
    }

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

                    result[1]= String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
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

                    result[1]= String.valueOf(testEvent.getmYear2())+"/"+String.valueOf(testEvent.getmMonth2())+"/"+String.valueOf(testEvent.getmDay2());
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

    public EventDate getTime(){

        Calendar calendar = Calendar.getInstance();

        EventDate time = new EventDate(String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), String.valueOf(calendar.get(Calendar.HOUR)), String.valueOf(calendar.get(Calendar.MINUTE)), EventDate.DEFAULT_TIME, EventDate.DEFAULT_TIME, EventDate.DEFAULT_TIME, EventDate.DEFAULT_TIME, EventDate.DEFAULT_TIME);

        return time;
    }
}

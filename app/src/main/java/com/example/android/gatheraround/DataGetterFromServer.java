package com.example.android.gatheraround;

import android.util.Log;
import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.Events;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by chiharu_miyoshi on 2017/08/30.
 */

public class DataGetterFromServer {
    ArrayList<Events> returnerArrayList = new ArrayList<Events>();

    public ArrayList<Events> getDataFromServer(){

        Firebase firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/");

        final ArrayList<Events> eventsArrayList = new ArrayList<Events>();
        firebase.child("eventPostDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Firebase", dataSnapshot.getValue().toString());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.i("Firebase", "name="+snapshot.child("name").getValue());

//                    long unixtime = (long)snapshot.child("unixTimeStamp").getValue();

                    int year = (int)snapshot.child("year").getValue();
                    int month = (int)snapshot.child("month").getValue();
                    int day = (int)snapshot.child("day").getValue();
                    int hour = (int)snapshot.child("hour").getValue();
                    int minute = (int)snapshot.child("minute").getValue();

                    int year2 = (int)snapshot.child("year2").getValue();
                    int month2 = (int)snapshot.child("month2").getValue();
                    int day2 = (int)snapshot.child("day2").getValue();
                    int hour2 = (int)snapshot.child("hour2").getValue();
                    int minute2 = (int)snapshot.child("minute2").getValue();

                    EventDate date = new EventDate(year, month, day, hour, minute, year2, month2, day2, hour2, minute2);

                    String event_name = snapshot.child("name").getValue().toString();
                    int participants = Integer.parseInt(snapshot.child("participants").getValue().toString());
                    double longitude = (double)snapshot.child("location/longitude").getValue();
                    double latitude = (double)snapshot.child("location/latitude").getValue();
                    String locationName = snapshot.child("locationName").getValue().toString();
                    String summary = snapshot.child("eventSummary").getValue().toString();
                    String category = snapshot.child("category").getValue().toString();
                    String globalId = snapshot.child("key").getValue().toString();

                    LatLng location = new LatLng(longitude, latitude);

                    Events newEvents = new Events(date, event_name, participants, location, locationName, summary, category, globalId);
                    Log.v("InsertedEntriesServer:",eventsArrayList.size()+"");

                    eventsArrayList.add(newEvents);
                    returnerArrayList = eventsArrayList;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Log.v("InsertedEntriesReturn:",eventsArrayList.size()+"");
        Log.v("EntriesReturner:",returnerArrayList.size()+"");
        return  returnerArrayList;
    }
}

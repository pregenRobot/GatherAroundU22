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

        Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_TITLE_URL);

        final ArrayList<Events> eventsArrayList = new ArrayList<Events>();
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Firebase", dataSnapshot.getValue().toString());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.i("Firebase", "name="+snapshot.child("name").getValue());

//                    long unixtime = (long)snapshot.child("unixTimeStamp").getValue();

                    String year = snapshot.child("year").getValue().toString();
                    String month = snapshot.child("month").getValue().toString();
                    String day = snapshot.child("day").getValue().toString();
                    String hour = snapshot.child("hour").getValue().toString();
                    String minute = snapshot.child("minute").getValue().toString();

                    String year2 = snapshot.child("year2").getValue().toString();
                    String month2 = snapshot.child("month2").getValue().toString();
                    String day2 = snapshot.child("day2").getValue().toString();
                    String hour2 = snapshot.child("hour2").getValue().toString();
                    String minute2 = snapshot.child("minute2").getValue().toString();

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

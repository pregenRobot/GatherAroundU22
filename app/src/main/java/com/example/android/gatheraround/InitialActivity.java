package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.EventMarker;
import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.example.android.gatheraround.MainActivity.mMap;

/**
 * Created by tamimazmain on 2017/10/01.
 */

public class InitialActivity extends AppCompatActivity {


    DatabaseHelper eventsDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity);
        final Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_TITLE_URL);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventsDBHelper = new DatabaseHelper(InitialActivity.this);
                ArrayList<Events> eventsonServer = new ArrayList<Events>();

                ArrayList<String> idsOnServer = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String year = snapshot.child("date/mYear").getValue().toString();
                    String month = snapshot.child("date/mMonth").getValue().toString();
                    String day = snapshot.child("date/mDay").getValue().toString();
                    String hour = snapshot.child("date/mHour").getValue().toString();
                    String minute = snapshot.child("date/mMinute").getValue().toString();

                    String year2 = snapshot.child("date/mYear2").getValue().toString();
                    String month2 = snapshot.child("date/mMonth2").getValue().toString();
                    String day2 = snapshot.child("date/mDay2").getValue().toString();
                    String hour2 = snapshot.child("date/mHour2").getValue().toString();
                    String minute2 = snapshot.child("date/mMinute2").getValue().toString();

                    EventDate date = new EventDate(year, month, day, hour, minute, year2, month2, day2, hour2, minute2);

                    String event_name = snapshot.child("name").getValue().toString();
                    int participants = Integer.parseInt(snapshot.child("participants").getValue().toString());
                    double longitude = (double)snapshot.child("location/longitude").getValue();
                    double latitude = (double)snapshot.child("location/latitude").getValue();
                    String locationName = snapshot.child("locationName").getValue().toString();
                    String summary = snapshot.child("eventSummary").getValue().toString();
                    String category = snapshot.child("category").getValue().toString();
                    String globalId = snapshot.child("key").getValue().toString();

                    idsOnServer.add(globalId);

                    LatLng location = new LatLng(latitude,longitude);

                    Events newEvents = new Events(date, event_name, participants, location, locationName, summary, category, globalId, true);
                    Log.v("Received Information",newEvents.toString());
                    eventsonServer.add(newEvents);
                }
                Log.v("Firebase","Retreived Information");
                Intent intent = new Intent(InitialActivity.this,MainActivity.class);
//                Bundle bundle= new Bundle();
//                bundle.putParcelable("events",(ArrayList<? extends Parcelable>) eventsonServer.get(position).getSongs());
//                intent.putExtra("EventsonServer",eventsonServer);
                startActivity(intent);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                internetStatus();
            }
        });
        Log.v("InitialActivity","Started");

    }
    public void internetStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager)InitialActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()){
            Toast.makeText(InitialActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }

    }

}

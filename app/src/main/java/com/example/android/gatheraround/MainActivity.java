package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.custom_classes.Participants;
import com.example.android.gatheraround.custom_classes.People;
import com.example.android.gatheraround.data.ContactsDatabaseHelper;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    public static BottomSheetBehavior mBottomsheetbehvior;
    public static GoogleMap mMap;
    Button eventListButton;
    RecyclerView rv;
    LinearLayoutManager llm;
    Context context;
    FloatingActionButton zoomIn;
    FloatingActionButton zoomOut;

    DatabaseHelper eventsDBHelper;
    EventListCursorAdapter eventListCursorAdapter;
    Cursor eventCursor;
    Gson gson;
    DatabaseHelper eventManager;
    ListView eventListView;
    private Map<MarkerOptions,Events> eventMarkerMap;
    ArrayList<Events> returner = new ArrayList<>();

    int cYear = 2017;
    int cMonth = 7;
    int cDate = 20;
    int cHour = 23;
    int cMinute = 20;
    long unixTimestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Log.i("log", "starting getter");


        Log.i("log", "finished getter");

        eventsDBHelper = new DatabaseHelper(context);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LinearLayout bottomsheet =
                findViewById(R.id.bottomsheet);
        mBottomsheetbehvior = BottomSheetBehavior.from(bottomsheet);
        mBottomsheetbehvior.setHideable(true);
        mBottomsheetbehvior.setPeekHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomsheetbehvior.setState(BottomSheetBehavior.STATE_HIDDEN);

        eventListButton = findViewById(R.id.eventlistbutton);
        eventListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomsheetbehvior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomsheetbehvior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    eventListButton.setText("Hide");
                } else if (mBottomsheetbehvior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomsheetbehvior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    eventListButton.setText("Event List Button");
                } else if (mBottomsheetbehvior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomsheetbehvior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    eventListButton.setText("Peek");
                }
            }
        });

        mBottomsheetbehvior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    eventListButton.setText("Peek");
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    eventListButton.setText("Hide");
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    eventListButton.setText("Event List Button");
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        llm = new LinearLayoutManager(context);
        final ListView eventListView = (ListView) findViewById(R.id.eventlistview);
        final DatabaseHelper eventManager = new DatabaseHelper(context);

        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                eventCursor = eventManager.getAllEvents();
                eventListCursorAdapter = new EventListCursorAdapter(
                        MainActivity.this,
                        eventCursor,
                        0);

                eventListView.setAdapter(eventListCursorAdapter);
            }
        });
        zoomIn = (FloatingActionButton) findViewById(R.id.zoomIn);
        zoomOut = (FloatingActionButton) findViewById(R.id.zoomOut);

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float zoomlevel = mMap.getCameraPosition().zoom;
                zoomlevel = zoomlevel + 2;
                final CameraPosition zoomLocation = CameraPosition.builder().target(mMap.getCameraPosition().target).zoom(zoomlevel).build();
                MainActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(zoomLocation));
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float zoomlevel = mMap.getCameraPosition().zoom;
                zoomlevel = zoomlevel - 2;
                final CameraPosition zoomLocation = CameraPosition.builder().target(mMap.getCameraPosition().target).zoom(zoomlevel).build();
                MainActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(zoomLocation));
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng school = new LatLng(-34, 151);

        Cursor c = eventsDBHelper.getAllEvents();

        this.addEventMarkers(c);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));
        eventManager = new DatabaseHelper(context);
        eventListView =  (ListView) findViewById(R.id.eventlistview);

        ArrayList<Events> tempArrayList = new ArrayList<Events>();
        tempArrayList.add(new Events(234234323,"Hello",0,new LatLng(35.652832,139.839478),"Tokyo","hello"));
        tempArrayList.add(new Events(234234323,"Hello2",0,new LatLng(11,2),"Tokyo","hello"));
        tempArrayList.add(new Events(234234323,"Hello3",0,new LatLng(20.435,4),"Tokyo","hello"));


        for(Events x:tempArrayList){
            Log.v("tempEvent:",x.getName());
        }
        eventMarkerMap = new HashMap<>();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.edit_event_popup,null);

                final EditText eventNameEdit = (EditText) mView.findViewById(R.id.event_name_edit);
                TextView doneButton = (TextView) mView.findViewById(R.id.donebuttoneventadd);
                TextView cancelButton = (TextView) mView.findViewById(R.id.canclebuttoneventadd);
                final EditText locationNameEdit = (EditText) mView.findViewById(R.id.event_location__name_edit);
                final EditText summaryEdit = (EditText) mView.findViewById(R.id.event_summary_edit);
                final TextView eventTimeEdit = (TextView) mView.findViewById(R.id.event_time_edit);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                unixTimestamp = 0;
                eventTimeEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
                        final View timeView = getLayoutInflater().inflate(R.layout.datetimepicker,null);
                        myBuilder.setView(timeView);
                        final AlertDialog dialog = myBuilder.create();

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final TextView yplus = (TextView) timeView.findViewById(R.id.yplus);
                                final TextView y = (TextView) timeView.findViewById(R.id.y);
                                final TextView yminus = (TextView) timeView.findViewById(R.id.yminus);
                                final TextView mplus = (TextView) timeView.findViewById(R.id.mplus);
                                final TextView m = (TextView) timeView.findViewById(R.id.m);
                                final TextView mminus = (TextView) timeView.findViewById(R.id.mminus);
                                final TextView dplus = (TextView) timeView.findViewById(R.id.dplus);
                                final TextView d = (TextView) timeView.findViewById(R.id.d);
                                final TextView dminus = (TextView) timeView.findViewById(R.id.dminus);
                                final TextView hplus = (TextView) timeView.findViewById(R.id.hplus);
                                final TextView h = (TextView) timeView.findViewById(R.id.h);
                                final TextView hminus = (TextView) timeView.findViewById(R.id.hminus);
                                final TextView minplus = (TextView) timeView.findViewById(R.id.minplus);
                                final TextView min = (TextView) timeView.findViewById(R.id.min);
                                final TextView minminus = (TextView) timeView.findViewById(R.id.minminus);
                                final TextView cancel = (TextView) timeView.findViewById(R.id.timeCancel);
                                final TextView done = (TextView) timeView.findViewById(R.id.timeDone);

                                y.setText(cYear+"");
                                m.setText(cMonth+"");
                                d.setText(cDate+"");
                                h.setText(cHour+"");
                                min.setText(cMinute+"");

                                yplus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cYear++;
                                        y.setText(cYear+"");
                                        Log.v("Year: ",cYear+"");
                                    }
                                });
                                yplus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        cYear++;
                                        y.setText(cYear+"");
                                        Log.v("Year: ",cYear+"");
                                        return true;
                                    }
                                });
                                yminus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cYear--;
                                        y.setText(cYear+"");
                                        Log.v("Year: ",cYear+"");
                                    }
                                });
                                yminus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        cYear--;
                                        y.setText(cYear+"");
                                        Log.v("Year: ",cYear+"");
                                        return true;
                                    }
                                });
                                mplus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(cMonth < 12) {
                                            cMonth++;
                                            m.setText(cMonth + "");
                                        }else{
                                            cMonth = 1;
                                            m.setText(cMonth+"");
                                        }
                                        Log.v("Month: ",cMonth+"");
                                    }
                                });
                                mplus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if(cMonth < 12) {
                                            cMonth++;
                                            m.setText(cMonth + "");
                                        }else{
                                            cMonth = 1;
                                            m.setText(cMonth+"");
                                        }
                                        Log.v("Month: ",cMonth+"");
                                        return true;
                                    }
                                });
                                mminus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(cMonth > 0) {
                                            cMonth--;
                                            m.setText(cMonth + "");
                                        }else{
                                            cMonth = 12;
                                            m.setText(cMonth+"");
                                        }
                                        Log.v("Month: ",cMonth+"");
                                    }
                                });
                                mminus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if(cMonth > 0) {
                                            cMonth--;
                                            m.setText(cMonth + "");
                                        }else{
                                            cMonth = 12;
                                            m.setText(cMonth+"");
                                        }
                                        Log.v("Month: ",cMonth+"");
                                        return true;
                                    }
                                });
                                dplus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(cMonth == 1||
                                                cMonth == 3 ||
                                                cMonth == 5 ||
                                                cMonth == 7 ||
                                                cMonth == 8 ||
                                                cMonth == 10 ||
                                                cMonth == 12
                                                ){
                                            if(cDate <31){
                                                cDate++;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 1;
                                                d.setText(cDate+"");
                                            }
                                        }else if(cMonth == 2 && cYear%4==0){
                                            if(cDate <29){
                                                cDate++;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 1;
                                                d.setText(cDate+"");
                                            }
                                        }else if(cMonth == 2 && cYear%4!=0){
                                            if(cDate <28){
                                                cDate++;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 1;
                                                d.setText(cDate+"");
                                            }
                                        }else{
                                            if(cDate <30){
                                                cDate++;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 1;
                                                d.setText(cDate+"");
                                            }
                                        }
                                        Log.v("Day: ",cDate+"");
                                    }
                                });
                                dplus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if(cMonth == 1||
                                                cMonth == 3 ||
                                                cMonth == 5 ||
                                                cMonth == 7 ||
                                                cMonth == 8 ||
                                                cMonth == 10 ||
                                                cMonth == 12
                                                ){
                                            if(cDate <31){
                                                cDate++;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 1;
                                                d.setText(cDate+"");
                                            }
                                        }else if(cMonth == 2 && cYear%4==0){
                                            if(cDate <29){
                                                cDate++;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 1;
                                                d.setText(cDate+"");
                                            }
                                        }else if(cMonth == 2 && cYear%4!=0){
                                            if(cDate <28){
                                                cDate++;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 1;
                                                d.setText(cDate+"");
                                            }
                                        }else{
                                            if(cDate <30){
                                                cDate++;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 1;
                                                d.setText(cDate+"");
                                            }
                                        }
                                        Log.v("Day: ",cDate+"");

                                        return true;
                                    }
                                });
                                dminus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if(cDate > 1) {
                                            cDate--;
                                            d.setText(cDate + "");
                                        }else{
                                            if(cMonth == 1||
                                                    cMonth == 3 ||
                                                    cMonth == 5 ||
                                                    cMonth == 7 ||
                                                    cMonth == 8 ||
                                                    cMonth == 10 ||
                                                    cMonth == 12
                                                    ){
                                                cDate = 31;
                                                d.setText(cDate+"");
                                            }else if(cMonth == 2 && cYear%4==0){
                                                cDate = 29;
                                                d.setText(cDate+"");
                                            }else if(cMonth == 2 && cYear%4!=0){
                                                cDate = 28;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 30;
                                                d.setText(cDate+"");
                                            }
                                        }
                                        Log.v("Day: ",cDate+"");
                                    }
                                });
                                dminus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if(cDate > 1) {
                                            cDate--;
                                            d.setText(cDate + "");
                                        }else{
                                            if(cMonth == 1||
                                                    cMonth == 3 ||
                                                    cMonth == 5 ||
                                                    cMonth == 7 ||
                                                    cMonth == 8 ||
                                                    cMonth == 10 ||
                                                    cMonth == 12
                                                    ){
                                                cDate = 31;
                                                d.setText(cDate+"");
                                            }else if(cMonth == 2 && cYear%4==0){
                                                cDate = 29;
                                                d.setText(cDate+"");
                                            }else if(cMonth == 2 && cYear%4!=0){
                                                cDate = 28;
                                                d.setText(cDate+"");
                                            }else{
                                                cDate = 30;
                                                d.setText(cDate+"");
                                            }
                                        }
                                        Log.v("Day: ",cDate+"");

                                        return true;
                                    }
                                });
                                hplus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if(cHour < 23) {
                                            cHour++;
                                            h.setText(cHour + "");
                                        }else{
                                            cHour=0;
                                            h.setText(cHour+"");
                                        }
                                        Log.v("Hour: ",cHour+"");
                                    }
                                });
                                hplus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if(cHour < 23) {
                                            cHour++;
                                            h.setText(cHour + "");
                                        }else{
                                            cHour=0;
                                            h.setText(cHour+"");
                                        }
                                        Log.v("Hour: ",cHour+"");

                                        return true;
                                    }
                                });
                                hminus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(cHour > 0) {
                                            cHour--;
                                            h.setText(cHour + "");
                                        }else{
                                            cHour = 23;
                                            h.setText(cHour+"");
                                        }
                                        Log.v("Hour: ",cHour+"");
                                    }
                                });
                                hminus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if(cHour > 0) {
                                            cHour--;
                                            h.setText(cHour + "");
                                        }else{
                                            cHour = 23;
                                            h.setText(cHour+"");
                                        }
                                        Log.v("Hour: ",cHour+"");

                                        return true;
                                    }
                                });
                                minplus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(cMinute < 59) {
                                            cMinute++;
                                            min.setText(cMinute + "");
                                        }else{
                                            cMinute = 0;
                                            min.setText(cMinute+"");
                                        }
                                        Log.v("Minute: ",cMinute+"");
                                    }
                                });
                                minplus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if(cMinute < 59) {
                                            cMinute++;
                                            min.setText(cMinute + "");
                                        }else{
                                            cMinute = 0;
                                            min.setText(cMinute+"");
                                        }
                                        Log.v("Minute: ",cMinute+"");
                                        return true;
                                    }
                                });
                                minminus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(cMinute >= 1) {
                                            cMinute--;
                                            min.setText(cMinute + "");
                                        }else{
                                            cMinute = 59;
                                            min.setText(cMinute+"");
                                        }
                                        Log.v("Minute: ",cMinute+"");
                                    }
                                });
                                minminus.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if(cMinute >= 1) {
                                            cMinute--;
                                            min.setText(cMinute + "");
                                        }else{
                                            cMinute = 59;
                                            min.setText(cMinute+"");
                                        }
                                        Log.v("Minute: ",cMinute+"");
                                        return true;
                                    }
                                });
                                done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        unixTimestamp =  concatenateUnixtime(cYear,cMonth-1,cDate,cHour,cMinute);
                                        Log.v("Whole thing",
                                                cYear+ " / "
                                                        + cMonth+ " / " +
                                                        cDate+ " / " +
                                                        cHour+ " / " +
                                                        cMinute+ " / ");
                                        dialog.dismiss();
                                    }
                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                            }
                        });
                        thread.start();
                        dialog.show();
                    }
                });
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Long newUnixTime = unixTimestamp;
                        if(eventNameEdit.getText().toString() != "" && newUnixTime != 0 && latLng != null && locationNameEdit.getText().toString() != "" && summaryEdit.getText().toString() != "") {

                            boolean insertData = eventsDBHelper.addData(
                                    eventNameEdit.getText().toString(),
                                    unixTimestamp, 20,
                                    latLng,
                                    locationNameEdit.getText().toString(),
                                    summaryEdit.getText().toString()
                            );
                            if (insertData == true) {
                                Toast.makeText(MainActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
                                Log.v("Database","Data Successfully Inserted!");
                            } else {
                                Toast.makeText(MainActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
                                Log.v("Database","Data Insert Failed!");

                            }
                            upDateListView();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this, "Please fill in all the fields",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {


                if (marker.equals(newMarkerOptions))
                {
                    MarkerOptions currentMarker = newMarkerOptions;
                    Events currentEvent = eventMarkerMap.get(newMarkerOptions);
                    Log.v("Marker Clicked!:",currentEvent.getName());
                }
                return  true;
            }
        });
        Firebase firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/");

        final ArrayList<Events> eventsArrayList = new ArrayList<Events>();


        firebase.child("eventPostDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Firebase", dataSnapshot.getValue().toString());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.i("Firebase", "name="+snapshot.child("name").getValue());

                    long unixtime = (long)snapshot.child("unixTimeStamp").getValue();
                    String event_name = snapshot.child("name").getValue().toString();
                    int participants = Integer.parseInt(snapshot.child("participants").getValue().toString());
                    double longitude = (double)snapshot.child("location/longitude").getValue();
                    double latitude = (double)snapshot.child("location/latitude").getValue();
                    String locationName = snapshot.child("locationName").getValue().toString();
                    String summary = snapshot.child("eventSummary").getValue().toString();

                    LatLng location = new LatLng(longitude, latitude);

                    Events newEvents = new Events(unixtime,event_name,participants,location,locationName,summary);

                    Log.v("FromServer:",newEvents.toString());

//                    eventsArrayList.add(newEvents);
                    newMarkerOptions = new MarkerOptions().position(newEvents.getLocation()).title(newEvents.getName())
                            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("web_hi_res_512",100,100)));
                    mMap.addMarker(newMarkerOptions);

                }
                Log.v("MarkerTobeAdded",eventsArrayList.size()+"");
                addMarkers(eventsArrayList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Log.v("InsertedEntriesReturn:",eventsArrayList.size()+"");
    }
    public void addEventMarkers(Cursor c){

        gson = new Gson();
        c.moveToFirst();
        while (c.isAfterLast() == false)
        {
            Log.v("added Marker: ",c.getString(c.getColumnIndex(DatabaseHelper.COL_LOCATION)));
            Log.v("Cursor column-index",c.getColumnIndex(DatabaseHelper.COL_ID)+ "");

            final LatLng location = gson.fromJson(c.getString(c.getColumnIndex(DatabaseHelper.COL_LOCATION)),LatLng.class);
            mMap.addCircle(new CircleOptions().center(location)
                    .radius(10)
                    .fillColor(R.color.cardbackground2)
                    .strokeColor(R.color.cardbackground));
            c.moveToNext();
        }
    }

    public void appendMarkers(Events events){
        returner.add(events);
    }

    public long concatenateUnixtime(int Year,int Month, int Day, int Hour, int Min){

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Year);
        c.set(Calendar.MONTH, Month);
        c.set(Calendar.DAY_OF_MONTH, Day);
        c.set(Calendar.HOUR, Hour);
        c.set(Calendar.MINUTE, Min);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Log.v("Time Added:",(int) (c.getTimeInMillis() / 1000L)+"");

        return (int) (c.getTimeInMillis() / 1000L);
    }

    public void upDateListView(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                eventCursor = eventManager.getAllEvents();
                eventListCursorAdapter = new EventListCursorAdapter(
                        MainActivity.this,
                        eventCursor,
                        0);
                eventListView.setAdapter(eventListCursorAdapter);
            }
        });
    }

    private MarkerOptions newMarkerOptions;
    public void addMarkers(ArrayList<Events> eventList){

        for(Events x:eventList){
            newMarkerOptions = new MarkerOptions().position(x.getLocation()).title(x.getName())
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("web_hi_res_512",100,100)));

            String testMarker = newMarkerOptions.toString();
            Log.v("TestMarker",testMarker);
            eventMarkerMap.put(newMarkerOptions,x);
            Log.v("addMarkers","Markers added!" + x.getName());
            mMap.addMarker(newMarkerOptions);
            Log.v("InsertedEvents:",x.getName());
        }
        for(MarkerOptions x:eventMarkerMap.keySet()){
            Log.v("eventMarkerMap",eventMarkerMap.get(x).getName());
            Log.v("number ofEntries",eventMarkerMap.size()+"");
        }

    }
    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


}


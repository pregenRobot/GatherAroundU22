package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    public static BottomSheetBehavior mBottomsheetbehvior;
    public static GoogleMap mMap;
    Button eventListButton;
    RecyclerView rv;
    LinearLayoutManager llm;
    Context context;
    FloatingActionButton contactsbutton;
    FloatingActionButton testButton;
    Intent contactsintent;
    Intent testIntent;

    DatabaseHelper eventsDBHelper;
    EventListCursorAdapter eventListCursorAdapter;
    Cursor eventCursor;
    Gson gson;

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
                if(mBottomsheetbehvior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomsheetbehvior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    eventListButton.setText("Hide");
                }
                else if(mBottomsheetbehvior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomsheetbehvior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    eventListButton.setText("Event List Button");
                }
                else if(mBottomsheetbehvior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
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
                }
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    eventListButton.setText("Hide");
                }
                else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    eventListButton.setText("Event List Button");
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        llm = new LinearLayoutManager(context);
        final ListView eventListView =  (ListView) findViewById(R.id.eventlistview);
        final DatabaseHelper eventManager = new DatabaseHelper(context);

        Thread thread = new Thread(new Runnable(){
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
        thread.start();

        contactsbutton = (FloatingActionButton) findViewById(R.id.contactsbutton);
        contactsbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                contactsintent = new Intent(MainActivity.this,ContactActivity.class);
                MainActivity.this.startActivity(contactsintent);
            }
        });

        testButton = (FloatingActionButton) findViewById(R.id.chatbutton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testIntent = new Intent(MainActivity.this,testActivity.class);
                MainActivity.this.startActivity(testIntent);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng school = new LatLng(-34, 151);

        Cursor c = eventsDBHelper.getAllEvents();

        this.addEventMarkers(c);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));
        final DatabaseHelper eventManager = new DatabaseHelper(context);
        final ListView eventListView =  (ListView) findViewById(R.id.eventlistview);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                Log.v("Map Clicked",latLng.toString());

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
                                        yminus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                cYear--;
                                                y.setText(cYear+"");
                                                Log.v("Year: ",cYear+"");
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
                            eventListCursorAdapter.notifyDataSetChanged();
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
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this, "Please fill in all the fields",Toast.LENGTH_SHORT).show();
                        }

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
    }
    public void moveCamera(LatLng location){

        final CameraPosition movelocation  = CameraPosition.builder().
                target(location).zoom(14).build();
        MainActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(movelocation));
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

}


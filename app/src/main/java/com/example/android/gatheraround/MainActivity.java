package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import static com.example.android.gatheraround.R.id.map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    public static BottomSheetBehavior mBottomsheetbehvior;
    public static GoogleMap mMap;
    Button eventListButton;
    LinearLayoutManager llm;
    Context context;
    FloatingActionButton zoomIn;
    FloatingActionButton zoomOut;

    DatabaseHelper eventsDBHelper;
    EventListCursorAdapter eventListCursorAdapter;
    Cursor eventCursor;
    Gson gson;
    ListView eventListView;
    BottomNavigationView bottomNavigationView;
    Calculations calculations = new Calculations();

    int cYear = 2017;
    int cMonth = 7;
    int cDate = 20;
    int cHour = 23;
    int cMinute = 20;
    long unixTimestamp;
    SupportMapFragment mapFragment;
    Intent selfIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()){
            Toast.makeText(MainActivity.this, "You are not connected to the Internet.", Toast.LENGTH_LONG).show();
        }

        eventsDBHelper = new DatabaseHelper(context);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
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
        final ListView eventListView = findViewById(R.id.eventlistview);
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
        zoomIn = findViewById(R.id.zoomIn);
        zoomOut = findViewById(R.id.zoomOut);

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

        LatLng school = new LatLng(37.422006, -122.084095);

        Cursor c = eventsDBHelper.getAllEvents();

        this.addEventMarkers(c);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));
        eventListView = findViewById(R.id.eventlistview);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.edit_event_popup,null);

                final EditText eventNameEdit = mView.findViewById(R.id.event_name_edit);
                TextView doneButton = mView.findViewById(R.id.donebuttoneventadd);
                TextView cancelButton = mView.findViewById(R.id.canclebuttoneventadd);
                final EditText locationNameEdit = mView.findViewById(R.id.event_location__name_edit);
                final EditText summaryEdit = mView.findViewById(R.id.event_summary_edit);
                final TextView eventTimeEdit = mView.findViewById(R.id.event_time_edit);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                                final TextView yplus = timeView.findViewById(R.id.yplus);
                                final TextView y = timeView.findViewById(R.id.y);
                                final TextView yminus = timeView.findViewById(R.id.yminus);
                                final TextView mplus = timeView.findViewById(R.id.mplus);
                                final TextView m = timeView.findViewById(R.id.m);
                                final TextView mminus = timeView.findViewById(R.id.mminus);
                                final TextView dplus = timeView.findViewById(R.id.dplus);
                                final TextView d = timeView.findViewById(R.id.d);
                                final TextView dminus = timeView.findViewById(R.id.dminus);
                                final TextView hplus = timeView.findViewById(R.id.hplus);
                                final TextView h = timeView.findViewById(R.id.h);
                                final TextView hminus = timeView.findViewById(R.id.hminus);
                                final TextView minplus = timeView.findViewById(R.id.minplus);
                                final TextView min = timeView.findViewById(R.id.min);
                                final TextView minminus = timeView.findViewById(R.id.minminus);
                                final TextView cancel = timeView.findViewById(R.id.timeCancel);
                                final TextView done = timeView.findViewById(R.id.timeDone);

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
                                    }
                                });
//
                                yminus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cYear--;
                                        y.setText(cYear+"");
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

                                    }
                                });
                                done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        unixTimestamp =  concatenateUnixtime(cYear,cMonth-1,cDate,cHour,cMinute);

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
                                    unixTimestamp,
                                    0,
                                    latLng,
                                    locationNameEdit.getText().toString(),
                                    summaryEdit.getText().toString(),
                                    Events.CATEGORY_INDIVIDUAL
                            );

                            if (insertData) {
                                Toast.makeText(MainActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
                                upDateListView();
                                dialog.dismiss();
                                selfIntent = new Intent(MainActivity.this,MainActivity.class);
                                MainActivity.this.startActivity(selfIntent);
                            } else {
                                Toast.makeText(MainActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();

                            }
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
        Firebase firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/");

        final ArrayList<Events> eventsArrayList = new ArrayList<Events>();
        firebase.child("eventPostDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<Marker> markArray = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    long unixtime = (long)snapshot.child("unixTimeStamp").getValue();
                    String event_name = snapshot.child("name").getValue().toString();
                    int participants = Integer.parseInt(snapshot.child("participants").getValue().toString());
                    double longitude = (double)snapshot.child("location/longitude").getValue();
                    double latitude = (double)snapshot.child("location/latitude").getValue();
                    String locationName = snapshot.child("locationName").getValue().toString();
                    String summary = snapshot.child("eventSummary").getValue().toString();
                    String category = snapshot.child("category").getValue().toString();
                    String globalId = snapshot.child("key").getValue().toString();

                    LatLng location = new LatLng(latitude,longitude);

                    Events newEvents = new Events(unixtime, event_name, participants, location, locationName, summary, category, globalId);

                    eventsArrayList.add(newEvents);
                    if(newEvents.getCategory().equals(Events.CATEGORY_INDIVIDUAL)) {
                        newMarkerOptions = new MarkerOptions().position(newEvents.getLocation()).title(newEvents.getName())
                                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("individual", 100, 100)));
                    }else if(newEvents.getCategory().equals(Events.CATEGORY_CORPORATE)){
                        newMarkerOptions = new MarkerOptions().position(newEvents.getLocation()).title(newEvents.getName())
                                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("corporate", 100, 100)));
                    }else if(newEvents.getCategory().equals(Events.CATEGORY_NPO)){
                        newMarkerOptions = new MarkerOptions().position(newEvents.getLocation()).title(newEvents.getName())
                                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("npo", 100, 100)));
                    }
                    Marker mMarker = mMap.addMarker(newMarkerOptions);
                    mMarker.setTag(newEvents);
                    markArray.add(mMarker);
                }
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        for(Marker x:markArray){
                            if(marker.toString().equals(x.toString())){
                                final Events nowEvents = (Events) marker.getTag();
                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.markerdialog,null);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                TextView summaryText = mView.findViewById(R.id.summaryTextBrowser);
                                summaryText.setMovementMethod(new ScrollingMovementMethod());
                                summaryText.setText(nowEvents.getEventSummary());
                                TextView nameText = mView.findViewById(R.id.eventNameMark);
                                nameText.setText(nowEvents.getName());
                                String date = calculations.UnixTimeConverter(nowEvents.getUnixTimeStamp())[0];
                                String time = calculations.UnixTimeConverter(nowEvents.getUnixTimeStamp())[1];
                                TextView dateText = mView.findViewById(R.id.eventDateMark);
                                dateText.setText(date);
                                TextView timeText = mView.findViewById(R.id.eventTimeMark);
                                timeText.setText(time);
                                TextView locationText = mView.findViewById(R.id.eventLocationMark);
                                locationText.setText(nowEvents.getLocationName());
                                TextView participantText = mView.findViewById(R.id.eventParticipantsMark);
                                participantText.setText(nowEvents.getParticipants()+"");
                                FloatingActionButton followButton =  (FloatingActionButton) mView.findViewById(R.id.follow);

                                followButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        DataSenderToServer dataSenderToServer = new DataSenderToServer();
                                        dataSenderToServer.addOneParticipants(nowEvents.getGlobalId());

                                        Toast.makeText(MainActivity.this, "You have followed this Event.", Toast.LENGTH_LONG).show();
                                        boolean insertData = eventsDBHelper.addData(
                                                nowEvents.getName(),
                                                nowEvents.getUnixTimeStamp(),
                                                nowEvents.getParticipants(),
                                                nowEvents.getLocation(),
                                                nowEvents.getLocationName(),
                                                nowEvents.getEventSummary(),
                                                nowEvents.getCategory()
                                        );

                                        if (insertData) {
                                            Toast.makeText(MainActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
                                            upDateListView();
                                            dialog.dismiss();
                                            selfIntent = new Intent(MainActivity.this,MainActivity.class);
                                            MainActivity.this.startActivity(selfIntent);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
                                        }
                                        Toast.makeText(MainActivity.this, "You have followed this Event.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                dialog.show();

                            }else{
                            }
                        }
                        return true;
                    }
                });
                bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(
                        new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.action_individual:
                                        for(Marker x:markArray){
                                            Events tempEvent = (Events) x.getTag();
                                            String currentCategory = tempEvent.getCategory();
                                            if(currentCategory.equals(Events.CATEGORY_INDIVIDUAL)){
                                                x.setVisible(true);
                                            }else if(currentCategory.equals(Events.CATEGORY_CORPORATE)){
                                                x.setVisible(false);
                                            }else if(currentCategory.equals(Events.CATEGORY_NPO)){
                                                x.setVisible(false);
                                            }
                                        }
                                        break;
                                    case R.id.action_corporate:
                                        for(Marker x:markArray){
                                            Events tempEvent = (Events) x.getTag();
                                            String currentCategory = tempEvent.getCategory();
                                            if(currentCategory.equals(Events.CATEGORY_INDIVIDUAL)){
                                                x.setVisible(false);
                                            }else if(currentCategory.equals(Events.CATEGORY_CORPORATE)){
                                                x.setVisible(true);
                                            }else if(currentCategory.equals(Events.CATEGORY_NPO)){
                                                x.setVisible(false);
                                            }
                                        }
                                        break;
                                    case R.id.action_npo:
                                        for(Marker x:markArray){
                                            Events tempEvent = (Events) x.getTag();
                                            String currentCategory = tempEvent.getCategory();
                                            if(currentCategory.equals(Events.CATEGORY_INDIVIDUAL)){
                                                x.setVisible(false);
                                            }else if(currentCategory.equals(Events.CATEGORY_CORPORATE)){
                                                x.setVisible(false);
                                            }else if(currentCategory.equals(Events.CATEGORY_NPO)){
                                                x.setVisible(true);
                                            }
                                        }
                                        break;
                                }
                                return true;
                            }
                        });
                final Button searchButton = (Button)findViewById(R.id.SearchButton);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.search_dialog_layout,null);

                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button searchQuery = (Button) mView.findViewById(R.id.searchButton);
                        final EditText enterText = (EditText) mView.findViewById(R.id.searchTextEdit);

                        searchQuery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                    for(Marker x:markArray){
                                        Events queryingEvent =(Events) x.getTag();
                                        if(enterText.getText().toString().equals(queryingEvent.getGlobalId())){
                                            final Events nowEvents = (Events) queryingEvent;
                                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                                            View mView = getLayoutInflater().inflate(R.layout.markerdialog,null);

                                            mBuilder.setView(mView);
                                            final AlertDialog dialog = mBuilder.create();
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            TextView summaryText = mView.findViewById(R.id.summaryTextBrowser);
                                            summaryText.setMovementMethod(new ScrollingMovementMethod());
                                            summaryText.setText(nowEvents.getEventSummary());
                                            TextView nameText = mView.findViewById(R.id.eventNameMark);
                                            nameText.setText(nowEvents.getName());
                                            String date = calculations.UnixTimeConverter(nowEvents.getUnixTimeStamp())[0];
                                            String time = calculations.UnixTimeConverter(nowEvents.getUnixTimeStamp())[1];
                                            TextView dateText = mView.findViewById(R.id.eventDateMark);
                                            dateText.setText(date);
                                            TextView timeText = mView.findViewById(R.id.eventTimeMark);
                                            timeText.setText(time);
                                            TextView locationText = mView.findViewById(R.id.eventLocationMark);
                                            locationText.setText(nowEvents.getLocationName());
                                            TextView participantText = mView.findViewById(R.id.eventParticipantsMark);
                                            participantText.setText(nowEvents.getParticipants()+"");
                                            FloatingActionButton followButton =  (FloatingActionButton) mView.findViewById(R.id.follow);

                                            followButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    DataSenderToServer dataSenderToServer = new DataSenderToServer();
                                                    dataSenderToServer.addOneParticipants(nowEvents.getGlobalId());
                                                    boolean insertData = eventsDBHelper.addData(
                                                            nowEvents.getName(),
                                                            nowEvents.getUnixTimeStamp(),
                                                            nowEvents.getParticipants(),
                                                            nowEvents.getLocation(),
                                                            nowEvents.getLocationName(),
                                                            nowEvents.getEventSummary(),
                                                            nowEvents.getCategory()
                                                    );

                                                    if (insertData) {
                                                        Toast.makeText(MainActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();

                                                        upDateListView();
                                                        dialog.dismiss();
                                                        selfIntent = new Intent(MainActivity.this,MainActivity.class);
                                                        MainActivity.this.startActivity(selfIntent);
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();

                                                    }
                                                    Toast.makeText(MainActivity.this, "You have followed this Event.", Toast.LENGTH_LONG).show();

                                                }
                                            });
                                            dialog.show();
                                        }
                                    }
                            }
                        });
                        dialog.show();

                    }
                });

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mMap.setOnMarkerClickListener(this);



    }
    public void addEventMarkers(Cursor c){

        gson = new Gson();
        c.moveToFirst();
        while (c.isAfterLast() == false)
        {
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

        return (int) (c.getTimeInMillis() / 1000L);
    }

    public void upDateListView(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(eventCursor != null) {
                    eventCursor = eventsDBHelper.getAllEvents();
                    eventListCursorAdapter = new EventListCursorAdapter(
                            MainActivity.this,
                            eventCursor,
                            0);
                    eventListView.setAdapter(eventListCursorAdapter);
                }else{
                }
            }
        });
    }

    private MarkerOptions newMarkerOptions;
    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
    @Override
    public void onResume(){
        super.onResume();

        if(mMap != null){
            mMap.clear();
        }
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.markerdialog,null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView summaryText = mView.findViewById(R.id.summaryTextBrowser);
        summaryText.setMovementMethod(new ScrollingMovementMethod());
        dialog.show();

        return true;
    }

}


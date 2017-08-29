package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.custom_classes.Participants;
import com.example.android.gatheraround.custom_classes.People;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    Button eventListButton;
    public static BottomSheetBehavior mBottomsheetbehvior;
    RecyclerView rv;
    LinearLayoutManager llm;
    Context context;
    public static GoogleMap mMap;
    FloatingActionButton contactsbutton;
    Intent contactsintent;
    DatabaseHelper eventsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDataHolder dataHolder = new myDataHolder();
        context = this;

        eventsDB = new DatabaseHelper(context);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        android.support.v4.widget.NestedScrollView bottomsheet =
                findViewById(R.id.bottomsheet);
        mBottomsheetbehvior = BottomSheetBehavior.from(bottomsheet);
        mBottomsheetbehvior.setHideable(true);
        mBottomsheetbehvior.setPeekHeight(400);
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
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(context);
        //Temporary Data


        RVAdapter adapter = new RVAdapter(this,dataHolder.getEventList());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

        contactsbutton = (FloatingActionButton) findViewById(R.id.contactsbutton);
        contactsbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                contactsintent = new Intent(MainActivity.this,ContactActivity.class);
                MainActivity.this.startActivity(contactsintent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng school = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(school).title("Sample Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));
        for (Events x: myDataHolder.getEventList()){
            mMap.addCircle(new CircleOptions().center(x.getLocation())
                    .radius(50)
                    .fillColor(R.color.cardbackground2)
                    .strokeColor(R.color.cardbackground)
            );
        }
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

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                doneButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean insertData = eventsDB.addData(
                                eventNameEdit.getText().toString(),
                                1704419236,
                                new Participants(new ArrayList<People>(){{
                                    add(myDataHolder.getContactList().get(1));
                                    add(myDataHolder.getContactList().get(3));
                                    add(myDataHolder.getContactList().get(2));
                                    add(myDataHolder.getContactList().get(4));
                                }}),
                                latLng,
                                locationNameEdit.getText().toString()
                        );

                        if (insertData == true) {
                            Toast.makeText(MainActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
                            Log.v("Database","Data Successfully Inserted!");
                        } else {
                            Toast.makeText(MainActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
                            Log.v("Database","Data Insert Failed!");
                        }
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Log.v("Main Activty: ","Event Adder Dismissed");
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

}


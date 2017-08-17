package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.onClick;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    Button eventListButton;
    public static BottomSheetBehavior mBottomsheetbehvior;
    RecyclerView rv;
    LinearLayoutManager llm;
    Context context;
    public ArrayList<Events> events;
    //public ArrayList<People> people;
    public static GoogleMap mMap;
    ImageButton contactsbutton;
    Intent contactsintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        events = new ArrayList<Events>();
        //people = new ArrayList<People>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventListButton = findViewById(R.id.eventlistbutton);

        android.support.v4.widget.NestedScrollView bottomsheet =
                findViewById(R.id.bottomsheet);

        mBottomsheetbehvior = BottomSheetBehavior.from(bottomsheet);
        mBottomsheetbehvior.setHideable(true);
        mBottomsheetbehvior.setPeekHeight(400);
        mBottomsheetbehvior.setState(BottomSheetBehavior.STATE_HIDDEN);

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

        //Bottomsheet callbacks
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

        //Recyler View
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(context);
        //Temporary Data
        String[] participants = {"Tamim","Chiharu","Azmain","Miyoshi","Steve Jobs","Michael Jackson","手塚治虫"};

        //Temporary Data
        events.add(new Events(1302719286,"Tour de France",participants,new LatLng(48.864716,2.349014),"Paris"));
        events.add(new Events(1502419296,"Soccer Tournament",participants,new LatLng(-22.970722, -43.182365),"Rio de Janeiro"));
        events.add(new Events(1901719266,"Olympics",participants,new LatLng(49.246292,-123.116226),"Vancouver"));
        events.add(new Events(1204219286,"School",participants,new LatLng(35.654267, 139.722372),"Hiroo"));

        RVAdapter adapter = new RVAdapter(this,events);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

        contactsbutton = (ImageButton) findViewById(R.id.contactsbutton);
        contactsbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                contactsintent = new Intent(MainActivity.this,ContactActivity.class);
                MainActivity.this.startActivity(contactsintent);
            }
        });

//        Intent intent = getIntent();
//        double latitude = intent.getDoubleExtra("latitude",0);
//        double longitude = intent.getDoubleExtra("longitude",0);
//        Log.v("MainActivity",(String)latitude + (String) longitude);
//        if(longitude == 0 && latitude == 0) {
//            LatLng personLocation = new LatLng(latitude,longitude);
//            moveCamera(personLocation);
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng school = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(school).title("Sample Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));
        for (Events x: events) {
            mMap.addMarker(new MarkerOptions().position(x.getLocation()).title(x.getLocationName()));
        }
    }
    public void moveCamera(LatLng location){

        final CameraPosition movelocation  = CameraPosition.builder().
                target(location).zoom(14).build();
        MainActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(movelocation));
    }


}


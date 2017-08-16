package com.example.android.gatheraround;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.onClick;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    Button eventListButton;
    private BottomSheetBehavior mBottomsheetbehvior;
    RecyclerView rv;
    LinearLayoutManager llm;
    Context context;
    public List<Events> events;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        events = new ArrayList<Events>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Find Button
        eventListButton = findViewById(R.id.eventlistbutton);

        //Find bottom sheet
        android.support.v4.widget.NestedScrollView bottomsheet =
                findViewById(R.id.bottomsheet);

        //Add bottomsheet behavior to view
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
        events.add(new Events(1302719286,"U22 project",participants,new LatLng(37.652832,219.839478),"Saizeriya"));
        events.add(new Events(1502419296,"U22 project",participants,new LatLng(32.652832,19.839478),"Hiroo"));
        events.add(new Events(1901719266,"U22 project",participants,new LatLng(19.652832,39.839478),"Canada"));
        events.add(new Events(1204219286,"U22 project",participants,new LatLng(100.652832,13.839478),"Tokyo Monorail"));
        RVAdapter adapter = new RVAdapter(this,events);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Add a marker in Sydney and move the camera
        LatLng school = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(school).title("Sample Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));
    }
}


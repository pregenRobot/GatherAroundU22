package com.example.android.gatheraround;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.Events;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Created by tamimazmain on 2017/11/01.
 */

public class mapfeed extends AppCompatActivity {
    private static String tabname = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;
    public static FloatingActionButton scanButton;
    Calculations calculations = new Calculations();
    public static FloatingActionButton follow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapfeed);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ArrayList<String> testparcelstring = new ArrayList<>();
        ArrayList<Events> testparcelevents = new ArrayList<>();

        Bundle datafromServer = savedInstanceState;
        //temp

//        if(datafromServer != null){
//            testparcelstring = datafromServer.getStringArrayList("IdsonServer");
//            testparcelevents = datafromServer.getParcelableArrayList("EventsOnServer");
//
//        }
        for(Events x: testparcelevents){
            Log.v("myparcel",x.toString());
        }

        scanButton = findViewById(R.id.scanMain1);
        follow = findViewById(R.id.follow1);

        scanFunctionality();

    }

    public void scanFunctionality(){
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(mapfeed.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan QR Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();

            }

        });
    }
    Events newEvent;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        Log.v("data",result+"");
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(mapfeed.this,R.string.cancelledScan,Toast.LENGTH_SHORT).show();

            }else{
                boolean gatherAroundCode = false;
                for(Events x: MapFragmenttab.receivedEvents){
                    Log.v("This is stufff!",x.getName());
                    newEvent = x;
                    assert newEvent != null;
                    String testString = "gatheraround/"+newEvent.getGlobalId();
                    if(testString.equals(result.getContents())){
                        bottomSheetCreator(newEvent);
                    }
                }
                if(!gatherAroundCode){
                    Toast.makeText(mapfeed.this,R.string.invalidQRCode,Toast.LENGTH_SHORT).show();
                }
            }
        }
        Log.v("ScannedQR",result.getContents());

        super.onActivityResult(requestCode, resultCode, data);

    }


    public void setupViewPager(ViewPager pager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new ContactFragmentTab(),"Contacts");
        adapter.addFragment(new MapFragmenttab(),"Map");
        adapter.addFragment(new FeedFragmentTab(),"Drawer");


        pager.setAdapter(adapter);
    }
    public void bottomSheetCreator( Events events){
        final Events nowevents = events;


        if(MapFragmenttab.bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
            MapFragmenttab.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else if (MapFragmenttab.bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            MapFragmenttab.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        MapFragmenttab.eventNamebot.setText(nowevents.getName());

        String date1 = calculations.concatenate(nowevents.getDate(),false,false)[0];
        String date2 = calculations.concatenate(nowevents.getDate(),false,false)[1];

        MapFragmenttab.eventDatebot.setText(date1);
        MapFragmenttab.eventTimebot.setText(date2);
        MapFragmenttab.eventLocationbot.setText(nowevents.getLocationName());
        MapFragmenttab.eventSummarybot.setText(nowevents.getEventSummary());
        MapFragmenttab.eventfollowersbot.setText(nowevents.getParticipants()+"");
    }


}

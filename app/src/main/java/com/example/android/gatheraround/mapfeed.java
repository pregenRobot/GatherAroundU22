package com.example.android.gatheraround;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.EventMarker;
import com.example.android.gatheraround.custom_classes.Events;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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
//                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mapfeed.this);
//                        View mView = getLayoutInflater().inflate(R.layout.markerdialog,null);
//                        mBuilder.setView(mView);
//                        final AlertDialog dialog = mBuilder.create();
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        TextView summaryText = mView.findViewById(R.id.summaryTextBrowser);
//                        summaryText.setMovementMethod(new ScrollingMovementMethod());
//                        summaryText.setText(newEvent.getEventSummary());
//                        TextView nameText = mView.findViewById(R.id.eventNameMark);
//                        nameText.setText(newEvent.getName());
//                        TextView dateText = mView.findViewById(R.id.eventDateMark);
//                        String[] dateandTime = calculations.concatenate(newEvent.getDate(),false,false);
//                        dateText.setText(dateandTime[0]);
//                        TextView timeText = mView.findViewById(R.id.eventTimeMark);
//                        timeText.setText(dateandTime[1]);
//                        TextView locationText = mView.findViewById(R.id.eventLocationMark);
//                        locationText.setText(newEvent.getLocationName());
//                        TextView participantText = mView.findViewById(R.id.eventParticipantsMark);
//                        participantText.setText(newEvent.getParticipants()+"");
//                        FloatingActionButton followButton =  mView.findViewById(R.id.follow);
//
//                        followButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
////                                boolean insertData = eventsDBHelper.addParticipant(newEvent);
////
////                                if (insertData) {
////                                    dialog.dismiss();
//////                                    mapFragment.getMapAsync(MainActivity.this);
////                                    Toast.makeText(getContext(),R.string.addedParticipants,Toast.LENGTH_SHORT).show();
////                                    Intent intent = new Intent(getContext(),mapfeed.class);
////                                    startActivity(intent);
////                                } else {
////                                    Toast.makeText(getContext(),R.string.event_exists,Toast.LENGTH_SHORT).show();
////                                }
//                            }
//                        });
//                        dialog.show();
//                        gatherAroundCode = true;
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
        adapter.addFragment(new FeedFragmenttab(),"Drawer");


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

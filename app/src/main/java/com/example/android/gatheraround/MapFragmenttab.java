package com.example.android.gatheraround;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.EventMarker;
import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.example.android.gatheraround.Calculations;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by tamimazmain on 2017/11/01.
 */

public class MapFragmenttab extends Fragment {
    View rootView;



    MapView mMapView;
    public static GoogleMap mMap;
    EventDate eventDate;
    Calendar calendar = Calendar.getInstance();
    ClusterManager<EventMarker> mClusterManager;
    private Algorithm<EventMarker> clusterManagerAlgorithm;
    public static ArrayList<Events> receivedEvents;
    Calculations calculations = new Calculations();


    DatabaseHelper eventsDBHelper;
    float down = 0;

    public static BottomSheetBehavior bottomSheetBehavior;

    public static TextView eventNamebot;
    public static TextView eventDatebot;
    public static TextView eventTimebot;
    public static TextView eventLocationbot;
    public static TextView eventSummarybot;
    public static TextView eventfollowersbot;

    public static View bottomSheet;

    public static View maincontent;

    int width;
    int height;

    Events thisevent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tabmapfragment, container, false);


        bottomSheet = getActivity().findViewById(R.id.mapfragbottomsheet);
        maincontent = getActivity().findViewById(R.id.container);



        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        eventNamebot = (TextView) getActivity().findViewById(R.id.bottomsheeteventname);
        eventDatebot = (TextView) getActivity().findViewById(R.id.eventDateMark);
        eventTimebot = (TextView) getActivity().findViewById(R.id.eventTimeMark);
        eventLocationbot = (TextView) getActivity().findViewById(R.id.eventLocationMark);
        eventSummarybot = (TextView) getActivity().findViewById(R.id.summaryTextBrowser);
        eventfollowersbot = (TextView) getActivity().findViewById(R.id.eventParticipantsMark);


        eventsDBHelper = new DatabaseHelper(getContext());

        ///New content
        /**
         * expand
         * collapse
         * hide show
         *
         * **/
        final View cardback = (View) rootView.findViewById(R.id.scroller);
        final View cardfront = (View) rootView.findViewById(R.id.mapviewer);

        Button flip1 = (Button) rootView.findViewById(R.id.flipfront);
        Button flip2 = (Button) rootView.findViewById(R.id.flipback);

        flip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlipAnimation flipAnimation = new FlipAnimation(cardfront, cardback);

                if (cardfront.getVisibility() == View.GONE)
                {
                    flipAnimation.reverse();
                }
                rootView.startAnimation(flipAnimation);
            }
        });
        flip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlipAnimation flipAnimation = new FlipAnimation(cardback, cardfront);

                if (cardback.getVisibility() == View.GONE)
                {
                    flipAnimation.reverse();
                }
                rootView.startAnimation(flipAnimation);
            }
        });


        mMapView = (MapView) rootView.findViewById(R.id.map1);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_EVENT_URL);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<EventMarker> clusterItemArray = new ArrayList<>();

//                idsOnLocal = new ArrayList<>();
//                idsOnLocal = eventsDBHelper.getAllIds();

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

                    EventMarker eventMarker = new EventMarker(newEvents,getContext());
                    mClusterManager.addItem(eventMarker);
                    clusterItemArray.add(eventMarker);
                    receivedEvents.add(newEvents);
                }

//                searchFunctionality(receivedEvents);

                clusterItemFunctionality();
                mClusterManager.setRenderer(new OwnIconRendered(getContext(), mMap, mClusterManager));
                Log.v("serverevents",receivedEvents.toString());
                setList(receivedEvents);

                Log.i("idsOnServer", "count: " + idsOnServer.size());

//                deletedIds = new ArrayList<>();

//                int checkNumber = idsOnLocal.size();
//                for (int a = 0; a < checkNumber; a++){
//                    int index = idsOnServer.indexOf(idsOnLocal.get(a));
//                    Log.i("idsSearch", "searched local id: " + idsOnLocal.get(a) + ", result: " + index);
//                    if (index == -1){
//                        deletedIds.add(idsOnLocal.get(a));
//                        eventsDBHelper.updateDoesExitsOnServer(idsOnLocal.get(a), false);
//                    }
//                }
//                Log.i("Checked deleted ids", "Deleted ids count: " + deletedIds.size());
//                setList();

                TextView evName = getActivity().findViewById(R.id.bottomsheeteventname);
                LinearLayout linlear = getActivity().findViewById(R.id.bottomsheeteventdate);
                TextView evLoc = getActivity().findViewById(R.id.eventLocationMark);

                int peekheight = evName.getHeight() + linlear.getHeight() + evLoc.getHeight();
                Log.v("Bottomsheet ",peekheight+"");

                bottomSheet.getLayoutParams().height = maincontent.getHeight();
                bottomSheetBehavior.setPeekHeight(peekheight);
                width = mapfeed.scanButton.getWidth();
                height = mapfeed.scanButton.getHeight();



                bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (BottomSheetBehavior.STATE_DRAGGING == newState) {

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    if(down > 0) {
                                        mapfeed.follow.setVisibility(View.VISIBLE);
                                        mapfeed.scanButton.animate().scaleX(0).scaleY(0).setDuration(300).start();
                                        mapfeed.follow.animate().scaleX(1).scaleY(1).setDuration(300).start();
                                    }
                                }
                            }, 100);
                        } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                            mapfeed.scanButton.animate().scaleX(1).scaleY(1).setDuration(300).start();
                            mapfeed.follow.animate().scaleX(0).scaleY(0).setDuration(300).start();
                            mapfeed.follow.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        if(slideOffset > 0){
                            down = 1;
                        }else if (slideOffset < 0){
                            down = -1;
                        }else {
                            down = 0;
                        }
                    }
                });

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                internetStatus();
            }
        });

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                MapFragmenttab.this.mMap = mMap;
                receivedEvents = new ArrayList<Events>();

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                MapFragmenttab.this.mMap.setMyLocationEnabled(true);

                ///New info

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(final LatLng latLng) {
                        eventDate = new EventDate(EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME,
                                EventDate.DEFAULT_TIME);

                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        View mView = getActivity().getLayoutInflater().inflate(R.layout.edit_event_popup, null);

                        final EditText eventNameEdit = mView.findViewById(R.id.event_name_edit);
                        TextView doneButton = mView.findViewById(R.id.donebuttoneventadd);
                        final TextView cancelButton = mView.findViewById(R.id.canclebuttoneventadd);
                        final EditText locationNameEdit = mView.findViewById(R.id.event_location__name_edit);
                        final EditText summaryEdit = mView.findViewById(R.id.event_summary_edit);
                        final TextView eventTimeEdit = mView.findViewById(R.id.event_time_edit);

                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        eventTimeEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eventDate = new EventDate(EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME,
                                        EventDate.DEFAULT_TIME);

                                final AlertDialog.Builder myBuilder = new AlertDialog.Builder(getContext());
                                final View timeView = getActivity().getLayoutInflater().inflate(R.layout.datetimepicker_dialog,null);
                                myBuilder.setView(timeView);
                                final AlertDialog dialog2 = myBuilder.create();

                                final Switch wholeDaySwitch = timeView.findViewById(R.id.wholeDaySwitch);
                                final Switch oneDaySwitch = timeView.findViewById(R.id.oneDaySwitch);

                                final TextView day1 = timeView.findViewById(R.id.startDate);
                                final TextView time1 = timeView.findViewById(R.id.startTime);
                                final TextView day2 = timeView.findViewById(R.id.endDate);
                                final TextView time2 = timeView.findViewById(R.id.endTime);

                                final LinearLayout right = timeView.findViewById(R.id.VerticalRight);
                                final LinearLayout left = timeView.findViewById(R.id.VerticalLeft);

                                final Button timeDone = timeView.findViewById(R.id.doneTimeLOL);
                                final Button timeCancel = timeView.findViewById(R.id.cancelTimeLOL);

                                if(wholeDaySwitch != null){
                                    wholeDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                            if(isChecked&&oneDaySwitch.isChecked()){
                                                time1.setVisibility(View.GONE);
                                                time2.setVisibility(View.GONE);
                                                day2.setVisibility(View.GONE);
                                                right.setVisibility(View.GONE);
                                                left.setLayoutParams(new AppBarLayout.LayoutParams
                                                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                                                eventDate.defaultDay2();
                                                eventDate.defaultTime1();
                                                eventDate.defaultTime2();
                                            }else if(isChecked){
                                                time1.setVisibility(View.GONE);
                                                time2.setVisibility(View.GONE);
                                                left.setLayoutParams(new AppBarLayout.LayoutParams
                                                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                                                eventDate.defaultTime1();
                                                eventDate.defaultTime2();
                                            }else{
                                                time1.setVisibility(View.VISIBLE);
                                                time2.setVisibility(View.VISIBLE);
                                                day1.setVisibility(View.VISIBLE);
                                                day2.setVisibility(View.VISIBLE);
                                                right.setVisibility(View.VISIBLE);
                                                left.setLayoutParams(new AppBarLayout.LayoutParams
                                                        (0, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                                                time1.setText(R.string.select_start_time);
                                                time2.setText(R.string.select_end_time);
                                            }
                                        }
                                    });
                                }
                                if(oneDaySwitch != null){
                                    oneDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                            assert wholeDaySwitch != null;
                                            if(isChecked&&wholeDaySwitch.isChecked()){
                                                time1.setVisibility(View.GONE);
                                                time2.setVisibility(View.GONE);
                                                day2.setVisibility(View.GONE);
                                                right.setVisibility(View.GONE);
                                                left.setLayoutParams(new AppBarLayout.LayoutParams
                                                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                                                eventDate.defaultDay2();
                                                eventDate.defaultTime2();
                                                eventDate.defaultTime1();
                                            } else if (isChecked) {
                                                day2.setVisibility(View.GONE);
                                                time2.setVisibility(View.GONE);
                                                eventDate.defaultTime2();
                                                eventDate.defaultDay2();
                                            }else{
                                                time1.setVisibility(View.VISIBLE);
                                                time2.setVisibility(View.VISIBLE);
                                                day1.setVisibility(View.VISIBLE);
                                                day2.setVisibility(View.VISIBLE);
                                                right.setVisibility(View.VISIBLE);
                                                left.setLayoutParams(new AppBarLayout.LayoutParams
                                                        (0, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                                                day2.setText(R.string.select_end_date);
                                                time2.setText(R.string.select_end_time);
                                            }
                                        }
                                    });
                                }
                                time1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                                String iText;
                                                String i1Text;
                                                if (i == 0){
                                                    iText = "00";
                                                }else{
                                                    iText = String.valueOf(i);
                                                }
                                                if(i1 == 0){
                                                    i1Text = "00";
                                                }else{
                                                    i1Text = String.valueOf(i1);
                                                }
                                                eventDate.updateTime1(iText,i1Text);
                                                time1.setText(iText + " : " + i1Text);
                                            }
                                        };
                                        new TimePickerDialog(
                                                getContext(),
                                                listener,
                                                calendar.get(Calendar.HOUR_OF_DAY),
                                                calendar.get(Calendar.MINUTE),
                                                true).show();
                                    }
                                });
                                time2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                                String iText;
                                                String i1Text;
                                                if (i == 0){
                                                    iText = "00";
                                                }else{
                                                    iText = String.valueOf(i);
                                                }
                                                if(i1 == 0){
                                                    i1Text = "00";
                                                }else{
                                                    i1Text = String.valueOf(i1);
                                                }
                                                eventDate.updateTime2(iText,i1Text);
                                                time2.setText(iText + " : " + i1Text);
                                            }
                                        };
                                        new TimePickerDialog(
                                                getContext(),
                                                listener,
                                                calendar.get(Calendar.HOUR_OF_DAY),
                                                calendar.get(Calendar.MINUTE),
                                                true).show();
                                    }
                                });
                                day1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                                i1+= 1;
                                                String iText, i1Text, i2Text;

                                                iText = String.valueOf(i);

                                                if ((i1 - 10) < 0){
                                                    i1Text = "0" + (i1);
                                                }else{
                                                    i1Text = String.valueOf(i1);
                                                }

                                                if ((i2 - 10) < 0){
                                                    i2Text = "0" + i2;
                                                }else{
                                                    i2Text = String.valueOf(i2);
                                                }

                                                eventDate.updateDate1(iText, i1Text, i2Text);
                                                day1.setText(iText + " / " + i1Text + " / " + i2Text);
                                            }
                                        };
                                        new DatePickerDialog(getContext(),listener,
                                                calendar.get(Calendar.YEAR),
                                                calendar.get(Calendar.MONTH),
                                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                                    }
                                });
                                day2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                                i1+=1;
                                                String iText, i1Text, i2Text;

                                                iText = String.valueOf(i);

                                                if ((i1 - 10) < 0){
                                                    i1Text = "0" + i1;
                                                }else{
                                                    i1Text = String.valueOf(i1);
                                                }

                                                if ((i2 - 10) < 0){
                                                    i2Text = "0" + i2;
                                                }else{
                                                    i2Text = String.valueOf(i2);
                                                }

                                                eventDate.updateDate2(iText, i1Text, i2Text);
                                                day2.setText(iText + " / " + i1Text + " / " + i2Text);
                                            }
                                        };
                                        new DatePickerDialog(getContext(),listener,
                                                calendar.get(Calendar.YEAR),
                                                calendar.get(Calendar.MONTH),
                                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                                    }
                                });
                                timeDone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog2.dismiss();
                                    }
                                });
                                timeCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog2.dismiss();
                                        eventDate = new EventDate(
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME,
                                                EventDate.DEFAULT_TIME);
                                    }
                                });

                                dialog2.show();
                            }
                        });
                        doneButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!eventNameEdit.getText().toString().equals("")
                                        && !eventDate.getmDay().equals(EventDate.DEFAULT_TIME)
                                        && latLng != null
                                        && !locationNameEdit.getText().toString().equals("")
                                        && !summaryEdit.getText().toString().equals("")) {
                                    boolean insertData = eventsDBHelper.addData(
                                            eventNameEdit.getText().toString(),
                                            eventDate,
                                            0,
                                            latLng,
                                            locationNameEdit.getText().toString(),
                                            summaryEdit.getText().toString(),
                                            Events.CATEGORY_INDIVIDUAL,
                                            true
                                    );

                                    if (insertData) {
                                        dialog.dismiss();
                                        //Map async
                                        Toast.makeText(getContext(),R.string.createdEvent,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(),mapfeed.class);
                                        startActivity(intent);
//                                        Intent Main = new Intent(MainActivity.this,MainActivity.class);
//                                        startActivity(Main);
                                    } else {
                                        Toast.makeText(getContext(),R.string.failedToAddEvent,Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getContext(),R.string.fillInAllFields,Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(getContext(),"Hello you long pressed",Toast.LENGTH_LONG).show();
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

                mClusterManager = new ClusterManager<EventMarker>(getContext(),mMap);
                clusterManagerAlgorithm = new NonHierarchicalDistanceBasedAlgorithm();
                mClusterManager.setAlgorithm(clusterManagerAlgorithm);
                mMap.setOnCameraIdleListener(mClusterManager);
            }
        });

//        ArrayList<String> testparcelstring;
//        ArrayList<Events> testparcelevents;
//
//        Bundle datafromServer = this.getArguments();
//
////        if(datafromServer != null){
//            testparcelstring = datafromServer.getStringArrayList("IdsonServer");
//            testparcelevents = datafromServer.getParcelableArrayList("EventsOnServer");
//
////        }
//        for(Events x: testparcelevents){
//            Log.v("myparcel",receivedEvents.toString());
//        }




        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void clusterItemFunctionality(){
        mClusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<EventMarker>() {
                    @Override
                    public boolean onClusterItemClick(EventMarker eventMarker) {
                        Events dealingEvent = eventMarker.getTag();
//                        dialogCreator(dealingEvent);
                        bottomSheetCreator(dealingEvent);
                        Log.v("ItemClickedYo!","ClusterTest");
                        return true;
                    }
                }
        );
        mMap.setOnMarkerClickListener(mClusterManager);
    }

    public void bottomSheetCreator( Events events){
        final Events nowevents = events;


        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        eventNamebot.setText(nowevents.getName());

        String date1 = calculations.concatenate(nowevents.getDate(),false,false)[0];
        String date2 = calculations.concatenate(nowevents.getDate(),false,false)[1];

        eventDatebot.setText(date1);
        eventTimebot.setText(date2);
        eventLocationbot.setText(nowevents.getLocationName());
        eventSummarybot.setText(nowevents.getEventSummary());
        eventfollowersbot.setText(nowevents.getParticipants()+"");

        mapfeed.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean insertData = eventsDBHelper.addParticipant(nowevents);

                if (insertData) {
                    Toast.makeText(getContext(),R.string.addedParticipants,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(),mapfeed.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),R.string.event_exists,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void setList(ArrayList<Events> events){

        final RecyclerView twitfeed = (RecyclerView) rootView.findViewById(R.id.defaultscroller);
        final ScrollFeedAdapter scrollFeedAdapter = new ScrollFeedAdapter(events,getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        twitfeed.setLayoutManager(layoutManager);
        twitfeed.setItemAnimator(new DefaultItemAnimator());
        twitfeed.setAdapter(scrollFeedAdapter);
    }

    public void internetStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()){
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
        }

    }

}

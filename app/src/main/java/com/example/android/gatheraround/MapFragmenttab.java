package com.example.android.gatheraround;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.EventMarker;
import com.example.android.gatheraround.custom_classes.Events;
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

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tamimazmain on 2017/11/01.
 */

public class MapFragmenttab extends Fragment {
    View rootView;

    MapView mMapView;
    private GoogleMap mMap;
    EventDate eventDate;
    Calendar calendar = Calendar.getInstance();
    ClusterManager<EventMarker> mClusterManager;
    private Algorithm<EventMarker> clusterManagerAlgorithm;
    ArrayList<Events> receivedEvents;
    Calculations calculations = new Calculations();

    Button eventListButton;
    LinearLayoutManager llm;

    private BottomSheetBehavior bottomSheetBehavior;
    private TextView bottomSheetHeading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tabmapfragment, container, false);

        ///New content
        /**
         * expand
         * collapse
         * hide show
         *
         * **/


        mMapView = (MapView) rootView.findViewById(R.id.map1);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
//                                if(!eventNameEdit.getText().toString().equals("")
//                                        && !eventDate.getmDay().equals(EventDate.DEFAULT_TIME)
//                                        && latLng != null
//                                        && !locationNameEdit.getText().toString().equals("")
//                                        && !summaryEdit.getText().toString().equals("")) {
//                                    boolean insertData = eventsDBHelper.addData(
//                                            eventNameEdit.getText().toString(),
//                                            eventDate,
//                                            0,
//                                            latLng,
//                                            locationNameEdit.getText().toString(),
//                                            summaryEdit.getText().toString(),
//                                            Events.CATEGORY_INDIVIDUAL,
//                                            true
//                                    );
//
//                                    if (insertData) {
//                                        dialog.dismiss();
//                                        mapFragment.getMapAsync(MainActivity.this);
//                                        Toast.makeText(MainActivity.this,R.string.createdEvent,Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
//                                        startActivity(intent);
//                                        Intent Main = new Intent(MainActivity.this,MainActivity.class);
//                                        startActivity(Main);
//                                    } else {
//                                        Toast.makeText(MainActivity.this,R.string.failedToAddEvent,Toast.LENGTH_SHORT).show();
//                                    }
//                                }else{
//                                    Toast.makeText(MainActivity.this,R.string.fillInAllFields,Toast.LENGTH_SHORT).show();
//                                }
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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

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
//                scanFunctionality();
                clusterItemFunctionality();
                mClusterManager.setRenderer(new OwnIconRendered(getContext(), mMap, mClusterManager));

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
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
//                internetStatus();
            }
        });
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
                        dialogCreator(dealingEvent);
                        Log.v("ItemClickedYo!","ClusterTest");
                        return true;
                    }
                }
        );
        mMap.setOnMarkerClickListener(mClusterManager);
    }
    public void dialogCreator(Events events){

        final Events nowEvents = events;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.markerdialog,null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView summaryText = mView.findViewById(R.id.summaryTextBrowser);
        summaryText.setMovementMethod(new ScrollingMovementMethod());
        assert nowEvents != null;
        summaryText.setText(nowEvents.getEventSummary());
        TextView nameText = mView.findViewById(R.id.eventNameMark);
        nameText.setText(nowEvents.getName());
        TextView dateText = mView.findViewById(R.id.eventDateMark);
        try{
            Log.v("THisistotaly","Ok!");
        }catch (NullPointerException n){
            Log.v("THISistotaly","Not Ok!");
        }
        String[] dateandTime = calculations.concatenate(nowEvents.getDate(),false,false);
        dateText.setText(dateandTime[0]);
        TextView timeText = mView.findViewById(R.id.eventTimeMark);
        timeText.setText(dateandTime[1]);
        TextView locationText = mView.findViewById(R.id.eventLocationMark);
        locationText.setText(nowEvents.getLocationName());
        TextView participantText = mView.findViewById(R.id.eventParticipantsMark);
        participantText.setText(nowEvents.getParticipants()+"");
        FloatingActionButton followButton = mView.findViewById(R.id.follow);

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean insertData = eventsDBHelper.addParticipant(nowEvents);
//                if (insertData) {
//
//                    mapFragment.getMapAsync(MainActivity.this);
//                    dialog.dismiss();
//                    Toast.makeText(MainActivity.this,R.string.addedParticipants,Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(MainActivity.this,R.string.event_exists,Toast.LENGTH_SHORT).show();
//                }
            }
        });
        dialog.show();
    }
}

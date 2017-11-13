package com.example.android.gatheraround;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static com.example.android.gatheraround.R.id.map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    public static BottomSheetBehavior mBottomSheetBehavior;
    public static GoogleMap mMap;
    Button eventListButton;
    LinearLayoutManager llm;
    Context context;

    DatabaseHelper eventsDBHelper;
    EventListCursorAdapter eventListCursorAdapter;
    Cursor eventCursor;
    ListView eventListView;
    BottomNavigationView bottomNavigationView;
    Calculations calculations = new Calculations();

    long unixTimestamp;
    SupportMapFragment mapFragment;
    FloatingActionButton searchButton;
    FloatingActionButton scanButton;
    ArrayList<String> idsOnLocal;
    ArrayList<String> deletedIds;

    Events newEvent;
    private static final int REQEUST_PERMISSION = 10;

    Calendar calendar = Calendar.getInstance();
    EventDate eventDate;
    final String PREFS_NAME = "MyPrefsFile";
    ClusterManager<EventMarker> mClusterManager;
    ArrayList<Events> eventArr = new ArrayList<>();
    private Algorithm<EventMarker> clusterManagerAlgorithm;
    ArrayList<Events> receivedEvents;

    static final String prefs_isFirstActivation = "isFirstActivation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstActivation = preferences.getBoolean(prefs_isFirstActivation, true);
        if (isFirstActivation){
            // write function for first activation here
            preferences.edit().putBoolean(prefs_isFirstActivation, false).apply();
        }

        internetStatus();

//        eventArr = (ArrayList<Events>)getIntent().getSerializableExtra("EventsonServer");
//        for(Events x:eventArr){
//            Log.v("ReceivedatMainActivity",x.getName());
//        }

        eventsDBHelper = new DatabaseHelper(context);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        LinearLayout bottomsheet =
                findViewById(R.id.bottomsheet);
        //Bottom sheets and searhc buttons
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mErrorString = new SparseIntArray();
        requestAppPermissions(new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA
        },R.string.msg,REQEUST_PERMISSION);

        eventListButton = findViewById(R.id.eventlistbutton);
        eventListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    eventListButton.setText("Hide");
                } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    eventListButton.setText("Event List Button");
                } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    eventListButton.setText("Peek");
                }
            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
        eventListView = findViewById(R.id.eventlistview);

    }

    public void setList(){
        final DatabaseHelper eventManager = new DatabaseHelper(context);

        try {
            eventCursor = eventManager.getAllEvents();
            eventListCursorAdapter = new EventListCursorAdapter(
                    MainActivity.this,
                    eventCursor,
                    0);
            eventListView.setAdapter(eventListCursorAdapter);

            Log.i("list", String.valueOf(eventCursor));
        }catch (RuntimeException e){
            Log.v("NullPointerException", "Initialize Event");

            ((ActivityManager)context.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("Styling", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Styling", "Can't find style. Error: ", e);
        }

        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);

        try{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
        } catch(NullPointerException e) {
            Log.w("Location Error", "Failed to get location.");
        }

        eventListView = findViewById(R.id.eventlistview);

        //OnMapLongClickListener
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

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.edit_event_popup, null);

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
                unixTimestamp = 0;
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

                        final AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
                        final View timeView = getLayoutInflater().inflate(R.layout.datetimepicker_dialog,null);
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
                                        MainActivity.this,
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
                                        MainActivity.this,
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
                                new DatePickerDialog(MainActivity.this,listener,
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
                                new DatePickerDialog(MainActivity.this,listener,
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
                                mapFragment.getMapAsync(MainActivity.this);
                                Toast.makeText(MainActivity.this,R.string.createdEvent,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                                startActivity(intent);
                                Intent Main = new Intent(MainActivity.this,MainActivity.class);
                                startActivity(Main);
                            } else {
                                Toast.makeText(MainActivity.this,R.string.failedToAddEvent,Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this,R.string.fillInAllFields,Toast.LENGTH_SHORT).show();
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
        mClusterManager = new ClusterManager<EventMarker>(MainActivity.this,mMap);
        clusterManagerAlgorithm = new NonHierarchicalDistanceBasedAlgorithm();
        mClusterManager.setAlgorithm(clusterManagerAlgorithm);
        mMap.setOnCameraIdleListener(mClusterManager);

    }

    @Override
    public void onResume(){
        super.onResume();
        receivedEvents = new ArrayList<>();

        if(mMap != null){
            mMap.clear();
        }

        final Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_EVENT_URL);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<EventMarker> clusterItemArray = new ArrayList<>();

                idsOnLocal = new ArrayList<>();
                idsOnLocal = eventsDBHelper.getAllIds();

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

                    EventMarker eventMarker = new EventMarker(newEvents,MainActivity.this);
                    mClusterManager.addItem(eventMarker);
                    clusterItemArray.add(eventMarker);
                    receivedEvents.add(newEvents);
                }

                searchFunctionality(receivedEvents);
                scanFunctionality();
                clusterItemFunctionality();
                mClusterManager.setRenderer(new OwnIconRendered(MainActivity.this, mMap, mClusterManager));

                Log.i("idsOnServer", "count: " + idsOnServer.size());

                deletedIds = new ArrayList<>();

                int checkNumber = idsOnLocal.size();
                for (int a = 0; a < checkNumber; a++){
                    int index = idsOnServer.indexOf(idsOnLocal.get(a));
                    Log.i("idsSearch", "searched local id: " + idsOnLocal.get(a) + ", result: " + index);
                    if (index == -1){
                        deletedIds.add(idsOnLocal.get(a));
                        eventsDBHelper.updateDoesExitsOnServer(idsOnLocal.get(a), false);
                    }
                }
                Log.i("Checked deleted ids", "Deleted ids count: " + deletedIds.size());
                setList();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                internetStatus();
            }
        });
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

    public void internetStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()){
            Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }

    }
    public void searchFunctionality(final ArrayList<Events> gotEvents){

        searchButton = findViewById(R.id.searchMain);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.search_dialog_layout,null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button searchQuery = mView.findViewById(R.id.searchButton);
                final EditText enterText = mView.findViewById(R.id.searchTextEdit);

                // query by date search here

                searchQuery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(Events x:gotEvents){
                            Events queryingEvent = x;
                            assert queryingEvent != null;
                            if(enterText.getText().toString().equals(queryingEvent.getGlobalId())){
                                final Events nowEvents = queryingEvent;
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
                                TextView dateText = mView.findViewById(R.id.eventDateMark);
                                String[] dateandTime = calculations.concatenate(nowEvents.getDate(),false,false);
                                dateText.setText(dateandTime[0]);
                                TextView timeText = mView.findViewById(R.id.eventTimeMark);
                                timeText.setText(dateandTime[1]);
                                TextView locationText = mView.findViewById(R.id.eventLocationMark);
                                locationText.setText(nowEvents.getLocationName());
                                TextView participantText = mView.findViewById(R.id.eventParticipantsMark);
                                participantText.setText(nowEvents.getParticipants()+"");
                                FloatingActionButton followButton =  mView.findViewById(R.id.follow);

                                followButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        boolean insertData = eventsDBHelper.addParticipant(nowEvents);

                                        if (insertData) {
                                            dialog.dismiss();
                                            mapFragment.getMapAsync(MainActivity.this);
                                            Toast.makeText(MainActivity.this,R.string.addedParticipants,Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MainActivity.this,R.string.event_exists,Toast.LENGTH_SHORT).show();
                                        }

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
    public void scanFunctionality(){
        final Activity activity = this;
        scanButton = findViewById(R.id.scanMain);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(activity);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        Collection<EventMarker> items = clusterManagerAlgorithm.getItems();
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(MainActivity.this,R.string.cancelledScan,Toast.LENGTH_SHORT).show();

            }else{
                boolean gatherAroundCode = false;
                for(Events x: receivedEvents){
                    Log.v("This is stufff!",x.getName());
                    newEvent = x;
                    assert newEvent != null;
                    String testString = "gatheraround/"+newEvent.getGlobalId();
                    if(testString.equals(result.getContents())){
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.markerdialog,null);
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView summaryText = mView.findViewById(R.id.summaryTextBrowser);
                        summaryText.setMovementMethod(new ScrollingMovementMethod());
                        summaryText.setText(newEvent.getEventSummary());
                        TextView nameText = mView.findViewById(R.id.eventNameMark);
                        nameText.setText(newEvent.getName());
                        TextView dateText = mView.findViewById(R.id.eventDateMark);
                        String[] dateandTime = calculations.concatenate(newEvent.getDate(),false,false);
                        dateText.setText(dateandTime[0]);
                        TextView timeText = mView.findViewById(R.id.eventTimeMark);
                        timeText.setText(dateandTime[1]);
                        TextView locationText = mView.findViewById(R.id.eventLocationMark);
                        locationText.setText(newEvent.getLocationName());
                        TextView participantText = mView.findViewById(R.id.eventParticipantsMark);
                        participantText.setText(newEvent.getParticipants()+"");
                        FloatingActionButton followButton =  mView.findViewById(R.id.follow);

                        followButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                boolean insertData = eventsDBHelper.addParticipant(newEvent);

                                if (insertData) {
                                    dialog.dismiss();
                                    mapFragment.getMapAsync(MainActivity.this);
                                    Toast.makeText(MainActivity.this,R.string.addedParticipants,Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this,R.string.event_exists,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.show();
                        gatherAroundCode = true;
                    }
                }
                if(!gatherAroundCode){
                    Toast.makeText(MainActivity.this,R.string.invalidQRCode,Toast.LENGTH_SHORT).show();
                }
            }
        }
        Log.v("ScannedQR",result.getContents());

        super.onActivityResult(requestCode, resultCode, data);

    }
    private SparseIntArray mErrorString;

    public void onPermissionsGranted() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            // first time task
            Intent mainActivityIntent = new Intent(MainActivity.this,MainActivity.class);
            startActivity(mainActivityIntent);
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).apply();
        }
    }

    public void requestAppPermissions(final String[]requestedPermissions, final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermissions = false;
        for(String permission: requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermissions = showRequestPermissions || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }

        if (permissionCheck!=PackageManager.PERMISSION_GRANTED) {
            if(showRequestPermissions) {
                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MainActivity.this, requestedPermissions, requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }

        } else {
            onPermissionsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for(int permisson : grantResults) {
            permissionCheck = permissionCheck + permisson;
        }

        if( (grantResults.length > 0) && PackageManager.PERMISSION_GRANTED == permissionCheck) {
            onPermissionsGranted();
        } else {
            //Display message when contain some Dangerous permisson not accept
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }).show();
        }
    }
    @Override
    public void onBackPressed() {

        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED || mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            eventListButton.setText("Event List Button");
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override public void onPause(){
        super.onPause();
        eventsDBHelper.close();
        mClusterManager.clearItems();
    }

    public void dialogCreator(Events events){

        final Events nowEvents = events;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.markerdialog,null);

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
                boolean insertData = eventsDBHelper.addParticipant(nowEvents);
                if (insertData) {

                    mapFragment.getMapAsync(MainActivity.this);
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this,R.string.addedParticipants,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this,R.string.event_exists,Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){

            moveTaskToBack(true);

            return super.onKeyDown(keyCode, event);

        }else{
            return false;
        }
    }
}
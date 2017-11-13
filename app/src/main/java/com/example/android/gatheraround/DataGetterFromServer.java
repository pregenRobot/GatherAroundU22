package com.example.android.gatheraround;

import android.content.Context;
import android.util.Log;

import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.EventMarker;
import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.custom_classes.Post;
import com.example.android.gatheraround.custom_classes.UserProfile;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by chiharu_miyoshi on 2017/11/13.
 */

public class DataGetterFromServer {

    String name, profile;

    ArrayList<String> idsOnLocal, deletedIds;
    ArrayList<EventMarker> clusterItemArray;

    DatabaseHelper databaseHelper;
    ArrayList<EventMarker> eventMarkers = new ArrayList<>();

    Context context;

    public DataGetterFromServer(Context mContext){
        context = mContext;
    }


    public UserProfile getProfileFromUid(String uid){

        Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_PROFILE_URL).child(uid);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child(UserProfile.TITLE_NAME).getValue().toString();
                profile = dataSnapshot.child(UserProfile.TITLE_PROFILE).getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return new UserProfile(uid, "Not public", name, profile);
    }

    public ArrayList<EventMarker> getAllPosts(){

        final ArrayList<Post> postsList = new ArrayList<>();

        Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_POST_URL);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String uid = snapshot.child("posterUid").getValue().toString();
                    String postContent = snapshot.child("postContent").getValue().toString();

                    String year = snapshot.child("postDate").child("mYear").getValue().toString();
                    String month = snapshot.child("postDate").child("mMonth").getValue().toString();
                    String day = snapshot.child("postDate").child("mDay").getValue().toString();
                    String hour = snapshot.child("postDate").child("mHour").getValue().toString();
                    String minute = snapshot.child("postDate").child("mMinute").getValue().toString();
                    EventDate date = new EventDate(year, month, day, hour, minute, EventDate.DEFAULT_TIME, EventDate.DEFAULT_TIME, EventDate.DEFAULT_TIME, EventDate.DEFAULT_TIME, EventDate.DEFAULT_TIME);

                    double latitude = (double)snapshot.child("location").child("latitude").getValue();
                    double longitude = (double)snapshot.child("location").child("longitude").getValue();
                    LatLng location = new LatLng(latitude,longitude);

                    String locationName = snapshot.child("locationName").getValue().toString();

                    String postId = snapshot.child("postId").getValue().toString();

                    Post individualPost = new Post(uid, postContent, date, location, locationName, postId);

                    EventMarker eventMarker = new EventMarker(individualPost,context);
                    eventMarkers.add(eventMarker);

                    postsList.add(individualPost);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return eventMarkers;
    }

//    public ArrayList<EventMarker> getEventsList(){
//
//        final Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_EVENT_URL);
//        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                clusterItemArray = new ArrayList<>();
//
//                idsOnLocal = new ArrayList<>();
//                idsOnLocal = databaseHelper.getAllIds();
//
//                ArrayList<String> idsOnServer = new ArrayList<>();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                    String year = snapshot.child("date/mYear").getValue().toString();
//                    String month = snapshot.child("date/mMonth").getValue().toString();
//                    String day = snapshot.child("date/mDay").getValue().toString();
//                    String hour = snapshot.child("date/mHour").getValue().toString();
//                    String minute = snapshot.child("date/mMinute").getValue().toString();
//
//                    String year2 = snapshot.child("date/mYear2").getValue().toString();
//                    String month2 = snapshot.child("date/mMonth2").getValue().toString();
//                    String day2 = snapshot.child("date/mDay2").getValue().toString();
//                    String hour2 = snapshot.child("date/mHour2").getValue().toString();
//                    String minute2 = snapshot.child("date/mMinute2").getValue().toString();
//
//                    EventDate date = new EventDate(year, month, day, hour, minute, year2, month2, day2, hour2, minute2);
//
//                    String event_name = snapshot.child("name").getValue().toString();
//                    int participants = Integer.parseInt(snapshot.child("participants").getValue().toString());
//                    double longitude = (double)snapshot.child("location/longitude").getValue();
//                    double latitude = (double)snapshot.child("location/latitude").getValue();
//                    String locationName = snapshot.child("locationName").getValue().toString();
//                    String summary = snapshot.child("eventSummary").getValue().toString();
//                    String category = snapshot.child("category").getValue().toString();
//                    String globalId = snapshot.child("key").getValue().toString();
//
//                    idsOnServer.add(globalId);
//
//                    LatLng location = new LatLng(latitude,longitude);
//
//                    Events newEvents = new Events(date, event_name, participants, location, locationName, summary, category, globalId, true);
//                }
//
//                Log.i("idsOnServer", "count: " + idsOnServer.size());
//
//                deletedIds = new ArrayList<>();
//
//                int checkNumber = idsOnLocal.size();
//                for (int a = 0; a < checkNumber; a++){
//                    int index = idsOnServer.indexOf(idsOnLocal.get(a));
//                    Log.i("idsSearch", "searched local id: " + idsOnLocal.get(a) + ", result: " + index);
//                    if (index == -1){
//                        deletedIds.add(idsOnLocal.get(a));
//                        databaseHelper.updateDoesExitsOnServer(idsOnLocal.get(a), false);
//                    }
//                }
//                Log.i("Checked deleted ids", "Deleted ids count: " + deletedIds.size());
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//        return clusterItemArray;
//    }
}

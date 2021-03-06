package com.example.android.gatheraround;

import android.util.Log;

import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.custom_classes.UserProfile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by chiharu_miyoshi on 2017/08/30.
 */

public class DataSenderToServer{

    public static final String FIREBASE_EVENT_URL = "https://u22-project-gather-around.firebaseio.com/eventPostDetails";
    public static final String FIREBASE_PROFILE_URL = "https://u22-project-gather-around.firebaseio.com/users_profiles";

    public static final String FIREBASE_PROFILE_TITLE = "users_profiles";

    private DatabaseReference mDatabase;

    public String pushToServer(Events newEvent){

        Firebase firebase = new Firebase(FIREBASE_EVENT_URL);
        Firebase push = firebase.push();
        push.setValue(newEvent);
        String key = push.getKey();
        firebase.child(key + "/key").setValue(key);

        return key;
    }

    void eraseEntry(String key){

        Firebase firebase = new Firebase(FIREBASE_EVENT_URL + "/" + key);
        firebase.removeValue();
    }

    public void addOneParticipants(String key){
        Firebase firebase = new Firebase(FIREBASE_EVENT_URL + "/" + key + "/participants");
        firebase.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                if (mutableData.getValue() == null){
                    mutableData.setValue(1);
                }else{
                    int count = mutableData.getValue(Integer.class);
                    count++;
                    mutableData.setValue(count);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                if (!b){
                    Log.e("Failed to add", firebaseError.toString());
                }
            }
        });
    }

    // send profile to server
    public void addNewUser(UserProfile profile){
        // TODO: 2017/10/17 maybe this is not working correctly
        Firebase firebase = new Firebase(FIREBASE_PROFILE_URL);
        Firebase push = firebase.push();
        push.setValue(profile);
    }
}

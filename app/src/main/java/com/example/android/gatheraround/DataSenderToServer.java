package com.example.android.gatheraround;

import android.util.Log;

import com.example.android.gatheraround.custom_classes.Events;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.core.Context;

/**
 * Created by chiharu_miyoshi on 2017/08/30.
 */

public class DataSenderToServer{

    public static final String FIREBASE_TITLE_URL = "https://u22-project-gather-around.firebaseio.com/eventPostDetails";

    public String pushToServer(Events newEvent){

        Firebase firebase = new Firebase(FIREBASE_TITLE_URL);
        Firebase push = firebase.push();
        push.setValue(newEvent);
        Log.i("Firebase pushed", "pushed=" + newEvent.getName());

        String key = push.getKey();
        Log.i("Firebase event key", "key=" + key);

        firebase.child(key + "/key").setValue(key);

        return key;
    }

    public void eraseEntry(String key){

        Firebase firebase = new Firebase(FIREBASE_TITLE_URL + "/" + key);
        firebase.removeValue();
    }

    public void addOneParticipants(String key){
        Firebase firebase = new Firebase(FIREBASE_TITLE_URL + "/" + key + "/participants");
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
                if (b){
                    Log.i("Firebase add", dataSnapshot.getValue().toString());
                }else{
                    Log.e("Failed to add", firebaseError.toString());
                }
            }
        });
    }
}

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

//    Context mContext;
//    DataSenderToServer(Context context){
//        this.mContext = context;
//    }

    public String pushToServer(Events newEvent){

        Firebase firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/eventPostDetails");
        Firebase push = firebase.push();
        push.setValue(newEvent);
        Log.i("Firebase pushed", "pushed=" + newEvent.getName());

        String key = push.getKey();
        Log.i("Firebase event key", "key=" + key);

        firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/eventPostTitles");
        firebase.push().setValue(key);

        return key;
    }

    public void eraseEntry(String globalId){
        Firebase firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/eventPostDetails/" + globalId);
        firebase.removeValue();
    }

    public void addOneParticipants(String globalId){
        Firebase firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/eventPostDetails/" + globalId + "/participants");
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

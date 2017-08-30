package com.example.android.gatheraround;

import android.util.Log;

import com.example.android.gatheraround.custom_classes.Events;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.Context;

import java.util.ArrayList;

import static com.example.android.gatheraround.R.id.m;

/**
 * Created by chiharu_miyoshi on 2017/08/30.
 */

public class DataSenderToServer{

    //    Context mContext;
//    DataSenderToServer(Context context){
//        this.mContext = context;
//    }
    public static String FIREBASE_URL = "https://u22-project-gather-around.firebaseio.com/eventPostDetails";
    public DataSenderToServer(){
//        String FIREBASE_URL = "https://u22-project-gather-around.firebaseio.com/eventPostDetails";
//
//        Firebase firebase = new Firebase(FIREBASE_URL);
//        firebase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                ArrayList<Events> returner = new ArrayList<Events>();
//                Log.e("Count " ,""+snapshot.getChildrenCount());
//                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
//                    Events post = postSnapshot.getValue(Events.class);
//                    returner.add(post);
//                    Log.v("DataChanged!",post.getName());
//                }
//
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                Log.e("The read failed: " ,firebaseError.getMessage());
//            }
//        });

    }

    public void pushToServer(Events newEvent){


        Firebase firebase = new Firebase(FIREBASE_URL);
        firebase.push().setValue(newEvent);
        Log.i("Firebase pushed", "pushed=" + newEvent.getName());
        String key = firebase.getKey();
        Log.i("Firebase event key", "key=" + key);


        String[] test = {"Hello","Its","Me"};
        for(String x:test){
            Log.v("test",x);
        }
        //test:Hello
        //test:Its
        //test:Me

    }

}

package com.example.android.gatheraround;

import android.content.Context;

import com.example.android.gatheraround.custom_classes.UserProfile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by chiharu_miyoshi on 2017/11/13.
 */

public class DataGetterFromServer {

    String name, profile;

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
}

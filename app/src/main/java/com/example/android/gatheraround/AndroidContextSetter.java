package com.example.android.gatheraround;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by chiharu_miyoshi on 2017/08/30.
 */

public class AndroidContextSetter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

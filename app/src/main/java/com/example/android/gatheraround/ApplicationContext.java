package com.example.android.gatheraround;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by chiharu_miyoshi on 2017/09/18.
 */

public class ApplicationContext extends Application {

    private Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
        Firebase.setAndroidContext(this);
    }

    public Context getContext(){
        return context;
    }
}

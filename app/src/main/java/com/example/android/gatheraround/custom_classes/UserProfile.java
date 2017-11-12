package com.example.android.gatheraround.custom_classes;

/**
 * Created by chiharu_miyoshi on 2017/10/15.
 */

public class UserProfile {

    public String mUid;
    public String mEmail;
    public String mName;

    public UserProfile(String uid, String email, String name){
        mUid = uid;
        mEmail = email;
        mName = name;
    }

    public String getUid(){
        return mUid;
    }

    public String getEmail(){
        return mEmail;
    }

    public String getName(){
        return mName;
    }
}

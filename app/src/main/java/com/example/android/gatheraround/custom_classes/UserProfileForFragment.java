package com.example.android.gatheraround.custom_classes;

import android.graphics.Bitmap;

/**
 * Created by chiharu_miyoshi on 2017/11/11.
 */

public class UserProfileForFragment {

    public String mUid;
    public String mUserName;
    public Bitmap bitmap;

    public UserProfileForFragment(String uid, String userName, Bitmap profileImage){
        mUid = uid;
        mUserName = userName;
        bitmap = profileImage;
    }

    public String getUid(){
        return mUid;
    }

    public String getUserName(){
        return mUserName;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
}

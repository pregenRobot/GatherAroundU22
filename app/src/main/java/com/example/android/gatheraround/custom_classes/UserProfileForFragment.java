package com.example.android.gatheraround.custom_classes;

import android.graphics.Bitmap;

/**
 * Created by chiharu_miyoshi on 2017/11/11.
 */

public class UserProfileForFragment {

    public String mUid;
    public String mUserName;
    public Bitmap mProfileImage;
    public String mProfileText;

    public UserProfileForFragment(String uid, String userName, Bitmap profileImage, String profileText){
        mUid = uid;
        mUserName = userName;
        mProfileImage = profileImage;
        mProfileText = profileText;
    }

    public String getUid(){
        return mUid;
    }

    public String getUserName(){
        return mUserName;
    }

    public Bitmap getBitmap(){
        return mProfileImage;
    }

    public String getProfileText(){
        return mProfileText;
    }
}

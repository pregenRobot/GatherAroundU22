package com.example.android.gatheraround.custom_classes;

/**
 * Created by chiharu_miyoshi on 2017/10/15.
 */

public class UserProfile {

    public String mUid;
    public String mEmail;
    public String mName;
    public String mProfileText;

    public static final String TITLE_UID = "mUid";
    public static final String TITLE_EMAIL = "mEmail";
    public static final String TITLE_NAME = "mName";
    public static final String TITLE_PROFILE = "mProfileText";

    public UserProfile(String uid, String email, String name, String profileText){
        mUid = uid;
        mEmail = email;
        mName = name;
        mProfileText = profileText;
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

    public String getProfileText(){
        return mProfileText;
    }
}

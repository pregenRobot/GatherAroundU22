package com.example.android.gatheraround.custom_classes;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by chiharu_miyoshi on 2017/11/13.
 */

public class Post {

    String mPosterUid;
    String mPostContent;
    EventDate mPostDate;
    LatLng mLocation;
    String mLocationName;
    String mPostId;
    int followers;

    public Post(String posterUid, String postContent, EventDate postDate, LatLng location, String locationName, String postId) {
        mPosterUid = posterUid;
        mPostContent = postContent;
        mPostDate = postDate;
        mLocation = location;
        mLocationName = locationName;
        mPostId = postId;
    }


    public String getPosterUid(){
        return mPosterUid;
    }

    public String getPostContent(){
        return mPostContent;
    }

    public EventDate getPostDate(){
        return mPostDate;
    }

    public LatLng getLocation(){
        return mLocation;
    }

    public String getLocationName(){
        return mLocationName;
    }

    public String getPostId(){
        return mPostId;
    }
}
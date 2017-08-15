package com.example.android.gatheraround;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class People {

    private String mName;
    private int mImageResource;
    private LatLng mLatilong;

    People(String name,int ImageResourceId){
        mName = name;
        mImageResource = ImageResourceId;
    }
    People(String name,int ImageResourceId, LatLng initLatlng){
        mName = name;
        mImageResource = ImageResourceId;
        mLatilong = initLatlng;
    }

    public String getName(){
        return mName;
    }
    public int getImage(){
        return mImageResource;
    }
    public LatLng getLocation(){
        return mLatilong;
    }

}
package com.example.android.gatheraround;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class People {

    private String mName;
    private int mImageResource;
    private LatLng mLatilong;
    private String mUniqueId;

    People(String name,int ImageResourceId, String UniqueId){
        mName = name;
        mImageResource = ImageResourceId;
        mUniqueId = UniqueId;
    }
    People(String name,int ImageResourceId, LatLng initLatlng, String UniqueId){
        mName = name;
        mImageResource = ImageResourceId;
        mLatilong = initLatlng;
        mUniqueId = UniqueId;
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
    public String getUniqueId() {return mUniqueId;}

}